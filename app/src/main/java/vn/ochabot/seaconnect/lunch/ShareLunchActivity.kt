package vn.ochabot.seaconnect.lunch

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_ordering.*
import kotlinx.android.synthetic.main.activity_ordering_item.view.*
import timber.log.Timber
import vn.ochabot.seaconnect.R
import vn.ochabot.seaconnect.core.VerticalSpaceItemDecoration
import vn.ochabot.seaconnect.core.base.BaseActivity
import vn.ochabot.seaconnect.core.base.BaseRecyclerAdapter
import vn.ochabot.seaconnect.core.base.extension.failure
import vn.ochabot.seaconnect.core.base.extension.loading
import vn.ochabot.seaconnect.core.base.extension.observe
import vn.ochabot.seaconnect.core.extension.viewModel
import vn.ochabot.seaconnect.core.helpers.*
import java.lang.StringBuilder
import kotlin.collections.ArrayList

/**
 * @author linhtruong
 */
class ShareLunchActivity : BaseActivity(), RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    override fun title(): Int = R.string.label_share_lunch_activity
    override fun contentView(): Int = R.layout.activity_ordering

    private lateinit var shareLunchAdapter: ShareLunchAdapter
    private lateinit var lunchViewModel: ShareLunchViewModel
    private lateinit var lunchId: String
    private lateinit var itemTouchHelper: ItemTouchHelper
    private lateinit var dataToLoad: ArrayList<Lunch>

    companion object {
        private const val KEY_ID = "key_id"

        fun forActivity(activity: BaseActivity, id: String): Intent {
            val intent = Intent(activity, ShareLunchActivity::class.java)
            intent.putExtra(KEY_ID, id)

            return intent
        }
    }

    override fun onCreateView(savedInstanceState: Bundle?) {
        appComponent.inject(this)
        lunchViewModel = viewModel(viewModelFactory) {
            failure(failure, ::renderFailure)
            loading(loadingStatus, ::renderLoading)
            observe(shareLunchData, ::loadData)
            observe(isLunchShared, ::handleShareSuccess)
            observe(isLunchAccepted, ::handleAcceptSuccess)
        }

        initView()
        initSwipeAction()
    }

    private fun initSwipeAction() {
        var callback = RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this)
        itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(shareLunchList)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int, position: Int) {
        Timber.d("ACCEPTING.....")
        lunchViewModel.acceptLunch(dataToLoad[position])
    }

    private fun initView() {
        icBack.setOnClickListener { onBackPressed() }
        lunchId = intent.getStringExtra(KEY_ID)
        if (!TextUtils.isEmpty(lunchId)) {
            val lunch = lunchViewModel.getLunchForId(lunchId)
            lunchTitle.text = lunch.title
            if (!TextUtils.isEmpty(lunch.url)) {
                lunchImg.setImageResource(
                        this.resources.getIdentifier(
                                lunch.url,
                                "drawable",
                                this.packageName
                        )
                )
            }
        }

        shareFoodButton.setOnClickListener {
            DialogBuilder(this@ShareLunchActivity)
                    .titleText("Confirmation")
                    .contentText("Do you want to share this meal?")
                    .confirmText(R.string.label_ok)
                    .rejectText(R.string.label_cancel)
                    .onConfirmClick {
                        lunchViewModel.shareLunch(lunchId)
                    }
                    .onRejectClick { }
                    .createDialog().showDialog()
        }
        shareLunchAdapter = ShareLunchAdapter(object : ShareLunchItemInteractor {
            override fun onItemClick(data: Lunch, pos: Int) {
//                val holder = shareLunchList.findViewHolderForAdapterPosition(pos)
//                holder?.let {
//                    holder.itemView.acceptFoodButton.visibility = View.VISIBLE
//                    holder.itemView.acceptFoodButton.setOnClickListener {
//                        DialogBuilder(this@ShareLunchActivity)
//                                .titleText("Confirmation")
//                                .contentText("Do you want to accept this meal?")
//                                .confirmText(R.string.label_ok)
//                                .rejectText(R.string.label_cancel)
//                                .onConfirmClick {
//                                    lunchViewModel.acceptLunch(data)
//                                }
//                                .onRejectClick {
//                                }
//                                .createDialog().showDialog()
//                    }
//                }
            }
        })
        shareLunchList.apply {
            adapter = shareLunchAdapter
            layoutManager = LinearLayoutManager(this@ShareLunchActivity, LinearLayoutManager.VERTICAL, false)
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(VerticalSpaceItemDecoration(50))
        }

        lunchViewModel.getShareLunchData()
    }

    private fun handleShareSuccess(isSuccess: Boolean?) {
        renderLoading(false)
        if (isSuccess!!) {
            animateHideLayoutYourFood()
        }
    }

    private fun animateHideLayoutYourFood() {
        layoutYourFood.animate()
//                .translationY(-(layoutYourFood.height + 50).toFloat())
//                .translationY(-(layoutYourFood.width + 50).toFloat())
                .alpha(0.0f)
                .setDuration(700)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(p0: Animator?) {
                    }

                    override fun onAnimationEnd(p0: Animator?) {
                        layoutYourFood.visibility = View.GONE
                    }

                    override fun onAnimationCancel(p0: Animator?) {
                    }

                    override fun onAnimationStart(p0: Animator?) {
                    }
                }).start()

    }

    private fun handleAcceptSuccess(isSuccess: Boolean?) {
        renderLoading(false)
        if (isSuccess!!) {
            AvatarDialog(this, "Congratulation", "You have received a lunch from Phạm Hoàng Long", R.string.label_ok, "https://imgur.com/T634CHD", View.OnClickListener {
                layoutYourFood.visibility = View.VISIBLE
                shareFoodButton.visibility = View.GONE
            }).showDialog()
        }
    }

    private fun loadData(data: List<Lunch>?) {
        renderLoading(false)
        dataToLoad = ArrayList()
        var isAccepted = false
        var isShared = false
        lateinit var acceptedLunch: Lunch
        data?.let {
            it.forEach { item ->
                if (TextUtils.isEmpty(item.des)) {
                    dataToLoad.add(item)
                } else {
                    if (item.des.equals(UserHelper.getUserId(), true)) {
                        isAccepted = true
                        acceptedLunch = item
                    }
                }

                if (item.source.equals(UserHelper.getUserId(), true)) {
                    isShared = true
                }
            }
        }

        shareLunchAdapter.add(dataToLoad)

        if (isShared) {
            Timber.d("Shared, cant do anything")
            animateHideLayoutYourFood()
        } else {
            if (isAccepted) {
                Timber.d("Found accepted: %s", acceptedLunch.ref)
                layoutYourFood.visibility = View.VISIBLE
                lunchTitle.text = acceptedLunch.title
                if (!TextUtils.isEmpty(acceptedLunch.url)) {
                    lunchImg.setImageResource(
                            this.resources.getIdentifier(
                                    acceptedLunch.url,
                                    "drawable",
                                    this.packageName
                            )
                    )
                }
                shareLunchAdapter = ShareLunchAdapter(object : ShareLunchItemInteractor {
                    override fun onItemClick(data: Lunch, pos: Int) {
                    }
                })
                shareLunchList.adapter = shareLunchAdapter
                shareLunchAdapter.add(dataToLoad)
            }
        }
    }

    class ShareLunchAdapter constructor(private val listener: ShareLunchItemInteractor) :
            BaseRecyclerAdapter<Lunch, ShareLunchAdapter.ShareLunchHolder>() {
        override fun createHolder(parent: ViewGroup, viewType: Int): ShareLunchHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_ordering_item, parent, false)
            return ShareLunchHolder(view, listener)
        }

        inner class ShareLunchHolder(itemView: View, listener: ShareLunchItemInteractor) :
                BaseRecyclerAdapter.ViewHolder<Lunch>(itemView) {
            init {
                itemView.setOnClickListener { listener.onItemClick(data[adapterPosition], adapterPosition) }
            }

            override fun bindData(data: Lunch, position: Int) {
                itemView.apply {
                    itemLunchTitle.text = constructTitle(data)
                    if (!TextUtils.isEmpty(data.url)) {
                        itemLunchImg.setImageResource(
                                context.resources.getIdentifier(
                                        data.url,
                                        "drawable",
                                        context.packageName
                                )
                        )
                    }
                }
            }

            private fun constructTitle(data: Lunch): String {

                return StringBuilder()
                        .append(getRealName(data.source))
                        .append(" is sharing ")
                        .append(if (!TextUtils.isEmpty(data.title)) data.title else "Salmon")
                        .toString()

            }

            private fun getRealName(name: String): String {
                if (name.equals("hoangducthien")) {
                    return "Hoàng Đức Thiện"
                } else if (name.equals("truongngoclinh")) {
                    return "Trương Ngọc Linh"
                } else if (name.equals("phamhoanglong")) {
                    return "Phạm Hoàng Long"
                } else if (name.equals("leminhnhut")) {
                    return "Lê Minh Nhựt"
                }

                return "Ocha Bot"
            }
        }
    }

    interface ShareLunchItemInteractor {
        fun onItemClick(lunch: Lunch, pos: Int)
    }
}
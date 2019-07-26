package vn.ochabot.seaconnect.lunch

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_ordering.*
import kotlinx.android.synthetic.main.activity_ordering.view.lunchTitle
import kotlinx.android.synthetic.main.activity_ordering_item.view.*
import timber.log.Timber
import vn.ochabot.seaconnect.R
import vn.ochabot.seaconnect.core.base.BaseActivity
import vn.ochabot.seaconnect.core.base.BaseRecyclerAdapter
import vn.ochabot.seaconnect.core.base.extension.failure
import vn.ochabot.seaconnect.core.base.extension.loading
import vn.ochabot.seaconnect.core.base.extension.observe
import vn.ochabot.seaconnect.core.extension.viewModel
import java.lang.StringBuilder
import kotlin.collections.ArrayList

/**
 * @author linhtruong
 */
class ShareLunchActivity : BaseActivity() {
    override fun title(): Int = R.string.label_share_lunch_activity
    override fun contentView(): Int = R.layout.activity_ordering
    override fun enableToolbar(): Boolean = true
    override fun enableBack(): Boolean = true

    private lateinit var shareLunchAdapter: ShareLunchAdapter
    private lateinit var lunchViewModel: ShareLunchViewModel
    private lateinit var lunchId: String

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
    }

    private fun initView() {
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
            lunchViewModel.shareLunch(lunchId)
        }
        shareLunchAdapter = ShareLunchAdapter(object : ShareLunchItemInteractor {
            override fun onItemClick(data: Lunch, pos: Int) {
                val holder = shareLunchList.findViewHolderForAdapterPosition(pos)
                holder?.let {
                    holder.itemView.acceptFoodButton.visibility = View.VISIBLE
                    holder.itemView.acceptFoodButton.setOnClickListener {
                        lunchViewModel.acceptLunch(data)
                    }
                }
            }
        })
        shareLunchList.apply {
            adapter = shareLunchAdapter
            layoutManager = LinearLayoutManager(this@ShareLunchActivity, LinearLayoutManager.VERTICAL, false)
        }

        lunchViewModel.getShareLunchData()
    }

    private fun handleShareSuccess(isSuccess: Boolean?) {
        renderLoading(false)
        if (isSuccess!!) {
            layoutYourFood.visibility = View.GONE
        }
    }

    private fun handleAcceptSuccess(isSuccess: Boolean?) {
        renderLoading(false)
        if (isSuccess!!) {
            layoutYourFood.visibility = View.VISIBLE
            shareFoodButton.visibility = View.GONE
        }
    }

    private fun loadData(data: List<Lunch>?) {
        renderLoading(false)
        var dataToLoad = ArrayList<Lunch>()
        var isAccepted = false
        var isShared = false
        lateinit var acceptedLunch: Lunch
        data?.let {
            it.forEach { item ->
                if (TextUtils.isEmpty(item.des)) {
                    dataToLoad.add(item)
                } else {
                    if (item.des.equals("truongngoclinh", true)) {
                        isAccepted = true
                        acceptedLunch = item
                    }
                }

                if (item.source.equals("truongngoclinh", true)) {
                    isShared = true
                }
            }
        }

        shareLunchAdapter.add(dataToLoad)

        if (isShared) {
            Timber.d("Shared, cant do anything")
            layoutYourFood.visibility = View.GONE
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
                    lunchTitle.text = constructTitle(data)
                    if (!TextUtils.isEmpty(data.url)) {
                        lunchImg.setImageResource(
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
                return StringBuilder().append(data.source).append(" is sharing ").append(data.title).toString()
            }
        }
    }

    interface ShareLunchItemInteractor {
        fun onItemClick(lunch: Lunch, pos: Int)
    }
}
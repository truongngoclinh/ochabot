package vn.ochabot.seaconnect.lunch

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_lunch.*
import kotlinx.android.synthetic.main.activity_lunch_item.view.*
import timber.log.Timber
import vn.ochabot.seaconnect.R
import vn.ochabot.seaconnect.core.GridDividerItemDecoration
import vn.ochabot.seaconnect.core.ItemInteractor
import vn.ochabot.seaconnect.core.base.BaseActivity
import vn.ochabot.seaconnect.core.base.BaseRecyclerAdapter
import vn.ochabot.seaconnect.core.base.extension.failure
import vn.ochabot.seaconnect.core.base.extension.loading
import vn.ochabot.seaconnect.core.base.extension.observe
import vn.ochabot.seaconnect.core.extension.viewModel


/**
 * @author linhtruong
 */
class LunchActivity : BaseActivity() {
    override fun title(): Int = R.string.label_lunch_activity
    override fun contentView(): Int = R.layout.activity_lunch

    private lateinit var lunchAdapter: LunchAdapter
    private lateinit var lunchViewModel: LunchViewModel
    private lateinit var selectedImgView: ImageView
    private var selectedPos = 0
    private val SPAN_COUNT = 2

    override fun onCreateView(savedInstanceState: Bundle?) {
        appComponent.inject(this)
        lunchViewModel = viewModel(viewModelFactory) {
            failure(failure, ::renderFailure)
            loading(loadingStatus, ::renderLoading)
            observe(lunchData, ::loadData)
        }

        initView()
    }

    private fun initView() {
        icBack.setOnClickListener { onBackPressed() }
        title_1.text = "Lunch"
        lunchAdapter = LunchAdapter(object : ItemInteractor<Lunch> {
            override fun onItemClick(data: Lunch, pos: Int) {
                selectedPos = pos
                val viewHolder = lunchList.findViewHolderForAdapterPosition(pos)
                viewHolder!!.itemView.root.background = this@LunchActivity.getDrawable(R.drawable.btn_select_green)
                selectedImgView = viewHolder!!.itemView.thumbnail
            }
        })
        lunchList.apply {
            adapter = lunchAdapter
            layoutManager = GridLayoutManager(this@LunchActivity, SPAN_COUNT)
            addItemDecoration(GridDividerItemDecoration(SPAN_COUNT, 120))
        }

        submitButton.setOnClickListener {
            //            navigator.openShareLunchActivity(this@LunchActivity, selectedPos.toString(), selectedImgView)
            navigator.openShareLunchActivity(this@LunchActivity, selectedPos.toString())
        }

        lunchViewModel.getLunchData()
    }

    private fun loadData(data: ArrayList<Lunch>?) {
        renderLoading(false)
        lunchAdapter.add(data!!)
    }

    class LunchAdapter constructor(private val listener: ItemInteractor<Lunch>) :
            BaseRecyclerAdapter<Lunch, LunchAdapter.LunchHolder>() {
        override fun createHolder(parent: ViewGroup, viewType: Int): LunchHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_lunch_item, parent, false)
            return LunchHolder(view, listener)
        }

        inner class LunchHolder(itemView: View, listener: ItemInteractor<Lunch>) :
                BaseRecyclerAdapter.ViewHolder<Lunch>(itemView) {
            init {
                itemView.setOnClickListener { listener.onItemClick(data[adapterPosition], adapterPosition) }
            }

            override fun bindData(data: Lunch, position: Int) {
                itemView.apply {
                    Timber.d("title: " + data.title + " - url: " + data.url)
                    title.text = data.title + " (" + data.count + ")"
                    thumbnail.setImageResource(
                            context.resources.getIdentifier(
                                    data.url,
                                    "drawable",
                                    context.packageName
                            )
                    )
                }
            }
        }
    }
}
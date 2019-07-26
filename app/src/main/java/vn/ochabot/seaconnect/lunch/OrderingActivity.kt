package vn.ochabot.seaconnect.lunch

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_lunch.*
import kotlinx.android.synthetic.main.activity_lunch_item.view.*
import vn.ochabot.seaconnect.R
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
class OrderingActivity : BaseActivity() {
    override fun title(): Int = R.string.label_order_activity
    override fun contentView(): Int = R.layout.activity_ordering

    private lateinit var shareLunchAdapter: ShareLunchAdapter
    private lateinit var lunchViewModel: LunchViewModel

    override fun onCreateView(savedInstanceState: Bundle?) {
        appComponent.inject(this)
        lunchViewModel = viewModel(viewModelFactory) {
            failure(failure, ::renderFailure)
            loading(loadingStatus, ::renderLoading)
            observe(shareLunchData, ::loadData)
        }

        initList()
    }

    private fun initList() {
        shareLunchAdapter = ShareLunchAdapter(object : ItemInteractor<Lunch> {

            override fun onItemClick(data: Lunch) {
            }
        })
        lunchList.apply {
            adapter = shareLunchAdapter
            layoutManager = LinearLayoutManager(this@OrderingActivity, LinearLayoutManager.VERTICAL, false)
        }
        lunchViewModel.getLunchData()
    }

    private fun loadData(data: ArrayList<Lunch>?) {
        renderLoading(false)
        shareLunchAdapter.data = data!!
    }

    class ShareLunchAdapter constructor(private val listener: ItemInteractor<Lunch>) : BaseRecyclerAdapter<Lunch, ShareLunchAdapter.ShareLunchHolder>() {
        override fun createHolder(parent: ViewGroup, viewType: Int): ShareLunchHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_lunch_item, parent, false)
            return ShareLunchHolder(view, listener)
        }

        inner class ShareLunchHolder(itemView: View, listener: ItemInteractor<Lunch>) : BaseRecyclerAdapter.ViewHolder<Lunch>(itemView) {
            init {
                itemView.setOnClickListener { listener.onItemClick(data[adapterPosition]) }
            }

            override fun bindData(data: Lunch, position: Int) {
                itemView.apply {
                    title.text = data.title
                }
            }
        }
    }
}
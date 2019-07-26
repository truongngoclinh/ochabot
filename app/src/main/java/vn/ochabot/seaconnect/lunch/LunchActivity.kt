package vn.ochabot.seaconnect.lunch

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_lunch.*
import kotlinx.android.synthetic.main.activity_lunch_item.view.*
import kotlinx.android.synthetic.main.activity_ordering.view.*
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
    override fun enableToolbar(): Boolean = true
    override fun enableBack(): Boolean = true

    private lateinit var lunchAdapter: LunchAdapter
    private lateinit var lunchViewModel: LunchViewModel
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
        lunchAdapter = LunchAdapter(object : ItemInteractor<Lunch> {
            override fun onItemClick(data: Lunch, pos: Int) {
                selectedPos = pos
                val viewHolder = lunchList.findViewHolderForAdapterPosition(pos)
                viewHolder!!.itemView.root.background = this@LunchActivity.getDrawable(R.drawable.btn_select_green)
            }
        })
        lunchList.apply {
            adapter = lunchAdapter
            layoutManager = GridLayoutManager(this@LunchActivity, SPAN_COUNT)
            addItemDecoration(GridDividerItemDecoration(SPAN_COUNT, 100))
        }

        submitButton.setOnClickListener {
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
                    title.text = data.title
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
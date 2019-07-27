package vn.ochabot.seaconnect.challenges

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.util.Pair
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlinx.android.synthetic.main.activity_challenge_layout.*
import vn.ochabot.seaconnect.R
import vn.ochabot.seaconnect.core.base.BaseActivity
import vn.ochabot.seaconnect.core.helpers.VerticalSpaceItemDecoration
import java.util.*
import kotlin.collections.HashMap

/**
 * @author Thien.
 */
class ChallengesActivity : BaseActivity() {

    internal lateinit var db: FirebaseFirestore
    internal lateinit var matches: CollectionReference
    internal var adapter = MatchAdapter()
    internal lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        db = FirebaseFirestore.getInstance()
        matches = db.collection("matches")
        findViewById<View>(R.id.create_match).setOnClickListener { addMatch() }
        findViewById<View>(R.id.ic_back).setOnClickListener { onBackPressed() }
        recyclerView = findViewById(R.id.match_list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(VerticalSpaceItemDecoration((resources.displayMetrics.density * 8).toInt()))
        recyclerView.adapter = adapter

        title_2.setOnClickListener {
            title_2.typeface = ResourcesCompat.getFont(applicationContext, R.font.montserrat_bold)
            title_2.setTextColor(ContextCompat.getColor(applicationContext, R.color.primary_button_normal))
            title_1.typeface = ResourcesCompat.getFont(applicationContext, R.font.montserrat_regular)
            title_1.setTextColor(ContextCompat.getColor(applicationContext, R.color.text_dark_30))
            adapter.switch()
        }


        showMatches()
    }

    private fun addMatch() {
        navigator.openChallengesAddActivity(this)
    }

    private fun showMatches() {
        matches.whereGreaterThan(Match.MATCH_STATUS, Match.STATUS_CANCELED)
            .addSnapshotListener(EventListener { values, e ->
                if (e != null) {
                    Log.e(TAG, "Listen failed.", e)
                    return@EventListener
                }
                adapter.mData!!.clear()
                for (doc in values!!) {
                    adapter.mData!!.add(doc)
                }
                adapter.mData!!.sortWith(Comparator { d1, d2 ->
                    var time1: Long = 0
                    if (d1.get("timestamp") != null) {
                        time1 = java.lang.Long.valueOf(String.format("%s", d1.get("timestamp")))
                    }
                    var time2: Long = 0
                    if (d2.get("timestamp") != null) {
                        time2 = java.lang.Long.valueOf(String.format("%s", d2.get("timestamp")))
                    }
                    (time1 - time2).toInt()
                })
                adapter.notifyDataSetChanged()
            })
    }

    override fun contentView(): Int {
        return R.layout.activity_challenge_layout
    }

    override fun title(): Int {
        return 0
    }


    class MatchAdapter : RecyclerView.Adapter<MatchAdapter.MyViewHolder>() {
        internal var mData: MutableList<QueryDocumentSnapshot>? = ArrayList()
        internal var mData2: MutableList<Map<String, String>>? = null

        override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
            val item = LayoutInflater.from(viewGroup.context).inflate(R.layout.view_match_item, viewGroup, false)
            val viewHolder = MyViewHolder(item)
            item.setOnClickListener { view ->
                if (type == 1) {
                    return@setOnClickListener
                }
                val intent = Intent(view.context, ChallengesDetailActivity::class.java)
                intent.putExtra(ChallengesDetailActivity.MATCH_ID, view.tag as String)
                intent.putExtra(ChallengesDetailActivity.MATCH_HOST, viewHolder.hostTv.text)
                intent.putExtra(ChallengesDetailActivity.MATCH_TITLE, viewHolder.titleTv.text)
                val p1 = Pair.create(view.findViewById<View>(R.id.match_host), "match_host")
                val p2 = Pair.create(view.findViewById<View>(R.id.match_title), "match_title")
                val p3 = Pair.create(view.findViewById<View>(R.id.match_reward), "match_reward")
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(view.context as Activity, p1, p2, p3)
                ActivityCompat.startActivity(view.context, intent, options.toBundle())
            }
            return viewHolder
        }

        override fun getItemCount(): Int {
            if (type == 1) {
                return 4
            } else {
                return if (mData == null) {
                    0
                } else mData!!.size
            }
        }

        override fun onBindViewHolder(vh: MyViewHolder, i: Int) {
            if (type == 1) {
                vh.hostTv.text = String.format("%s", mData2!![i].get("1"))
                vh.titleTv.text = String.format("Phuc Long: %s", mData2!![i].get("2"))
                vh.titleRw.visibility = View.GONE
            } else {
                vh.itemView.tag = mData!![i].id
                vh.hostTv.text = String.format("%s", mData!![i].get(Match.HOST))
                vh.titleTv.text = String.format("Game: %s", mData!![i].get(Match.TITLE))
                vh.titleRw.text = String.format("Reward: %s", mData!![i].get(Match.MATCH_REWARD))
            }
        }


        class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var hostTv: TextView
            var titleTv: TextView
            var titleRw: TextView

            init {
                hostTv = itemView.findViewById(R.id.match_host)
                titleTv = itemView.findViewById(R.id.match_title)
                titleRw = itemView.findViewById(R.id.match_reward)
            }

        }

        var type = 0;

        fun switch() {
            type = 1
            mData2 = ArrayList()
            var d1 = HashMap<String, String>()
            d1.put("1", "Hoàng Đức Thiện")
            d1.put("2", "1")
            mData2!!.add(d1)
            var d2 = HashMap<String, String>()
            d2.put("1", "Lê Minh Nhựt")
            d2.put("2", "1")
            mData2!!.add(d2)
            var d3 = HashMap<String, String>()
            d3.put("1", "Trương Ngọc Linh")
            d3.put("2", "-1")
            mData2!!.add(d3)
            var d4 = HashMap<String, String>()
            d4.put("1", "Phạm Hoàng Long")
            d4.put("2", "-1")
            mData2!!.add(d4)
            notifyDataSetChanged()
        }
    }


}

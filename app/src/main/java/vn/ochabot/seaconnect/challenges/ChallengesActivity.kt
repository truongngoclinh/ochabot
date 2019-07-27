package vn.ochabot.seaconnect.challenges

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
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
import vn.ochabot.seaconnect.R
import vn.ochabot.seaconnect.core.base.BaseActivity
import vn.ochabot.seaconnect.core.helpers.VerticalSpaceItemDecoration
import java.util.*

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

        override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
            val item = LayoutInflater.from(viewGroup.context).inflate(R.layout.view_match_item, viewGroup, false)
            val viewHolder = MyViewHolder(item)
            item.setOnClickListener { view ->
                val intent = Intent(view.context, ChallengesDetailActivity::class.java)
                intent.putExtra(ChallengesDetailActivity.MATCH_ID, view.tag as String)
                intent.putExtra(ChallengesDetailActivity.MATCH_HOST, viewHolder.hostTv.text)
                intent.putExtra(ChallengesDetailActivity.MATCH_TITLE, viewHolder.titleTv.text)
                val p1 = Pair.create(view.findViewById<View>(R.id.match_host), "match_host")
                val p2 = Pair.create(view.findViewById<View>(R.id.match_title), "match_title")
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(view.context as Activity, p1, p2)
                ActivityCompat.startActivity(view.context, intent, options.toBundle())
            }
            return viewHolder
        }

        override fun getItemCount(): Int {
            return if (mData == null) {
                0
            } else mData!!.size
        }

        override fun onBindViewHolder(vh: MyViewHolder, i: Int) {
            vh.itemView.tag = mData!![i].id
            vh.hostTv.text = String.format("%s", mData!![i].get(Match.HOST))
            vh.titleTv.text = String.format("Game: %s", mData!![i].get(Match.TITLE))
            vh.titleRw.text = String.format("Reward: %s", mData!![i].get(Match.MATCH_REWARD))
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
    }


}

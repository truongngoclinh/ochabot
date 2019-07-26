package vn.ochabot.seaconnect.event

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract.Instances.EVENT_ID
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat
import android.support.v4.util.Pair
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import vn.ochabot.seaconnect.R
import vn.ochabot.seaconnect.challenges.ChallengesDetailActivity
import vn.ochabot.seaconnect.core.base.BaseActivity
import vn.ochabot.seaconnect.core.helpers.UserHelper
import vn.ochabot.seaconnect.model.Event
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author Thien.
 */
class EventsActivity : BaseActivity() {

    private lateinit var db: FirebaseFirestore
    lateinit var events: CollectionReference
    private var adapter = EventAdapter()
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appComponent.inject(this)
        db = FirebaseFirestore.getInstance()
        events = db.collection("events")
        showEvents()
        recyclerView = findViewById(R.id.event_list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(event: DocumentSnapshot) {

                navigator.openEventDetailActivity(this@EventsActivity, event.id)
            }
        })

        adapter.setOnJoinClickListener(object : OnJoinClickListener {
            override fun onJoinClick(data: DocumentSnapshot, memberList: ArrayList<String>) {
                events.document(data.id).update("members", memberList)
                    .addOnFailureListener({ e -> Log.e(BaseActivity.TAG, "add failed.", e) })
                    .addOnSuccessListener(
                        { Log.e(BaseActivity.TAG, "add success") })
            }
        })

//        findViewById<View>(R.id.create_match).setOnClickListener { addEvent() }
    }

    private fun showEvents() {
        events.addSnapshotListener(EventListener { values, e ->
            if (e != null) {
                Log.e(TAG, "Listen failed.", e)
                return@EventListener
            }

            adapter.mData.clear()
            for (doc in values!!) {
                adapter.mData.add(doc)
                Log.d(TAG, doc.toString())
            }
            adapter.mData.sortWith(Comparator { d1, d2 ->
                (d1["time_stamp"].toString().toLong() - d2["time_stamp"].toString().toLong()).toInt()
            })
            adapter.notifyDataSetChanged()
        })
    }

    override fun contentView(): Int {
        return R.layout.activity_event
    }

    override fun title(): Int {
        return 0
    }

    open class EventAdapter : RecyclerView.Adapter<EventAdapter.MyViewHolder>() {

        var mData = ArrayList<DocumentSnapshot>()

        var mOnItemClickListener: OnItemClickListener? = null
        var mOnJoinClickListener: OnJoinClickListener? = null

        fun setOnItemClickListener(listener: OnItemClickListener) {
            mOnItemClickListener = listener
        }

        fun setOnJoinClickListener(listener: OnJoinClickListener) {
            mOnJoinClickListener = listener
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
            val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.view_event_item, viewGroup, false)
            view.setOnClickListener {
                val data = it.tag
                data?.let {
                    mOnItemClickListener?.onItemClick(data as DocumentSnapshot)
                }
            }
            val viewHolder = MyViewHolder(view)
            viewHolder.joinBtn.setOnClickListener { view ->
                val data = view.tag
                data?.let {
                    val eventData = data as DocumentSnapshot
                    val event = convertData(eventData)
                    if ((view as TextView).text.equals(view.context.getString(R.string.label_joined))) {
                        val newMember = ArrayList<String>()
                        event.members?.forEach {
                            if (!it.equals(UserHelper.getUserName())) {
                                newMember.add(it)
                            }
                        }
                        event.members = newMember
                    } else {
                        event.members?.add(UserHelper.getUserName())
                    }
                    mOnJoinClickListener?.onJoinClick(eventData, event.members)

                }
            }
            return viewHolder
        }

        override fun getItemCount(): Int {
            return mData!!.size
        }

        override fun onBindViewHolder(viewHolder: MyViewHolder, i: Int) {
            val data = mData?.get(i)
            viewHolder.itemView.tag = data
            viewHolder.joinBtn.tag = data
            data?.let {
                val convertData = convertData(it)
                viewHolder.time.text =
                    SimpleDateFormat("MMM dd, YYYY hh:mm aa", Locale.ENGLISH).format(Date(convertData.time))
                viewHolder.location.text = convertData.location
                viewHolder.name.text = convertData.name
                viewHolder.detail.text = convertData.desc
                viewHolder.hostBy.text = convertData.host
                viewHolder.memberNum.text = convertData.members.size.toString()
                var joined = false
                convertData.members.forEach { member ->
                    if (member.equals(UserHelper.getUserName())) {
                        joined = true
                        return@forEach
                    }
                }
                viewHolder.joinBtn.text =
                    if (joined) viewHolder.itemView.context.getString(R.string.label_joined) else viewHolder.itemView.context.getString(
                        R.string.label_join
                    )
                viewHolder.joinBtn.background =
                    if (joined) ContextCompat.getDrawable(viewHolder.itemView.context, R.drawable.oc_bg_joined_btn)
                    else ContextCompat.getDrawable(viewHolder.itemView.context, R.drawable.oc_bg_join_btn)
            }
        }

        fun convertData(data: DocumentSnapshot): Event {
            val result = Event()
            result.time = data["time_stamp"].toString().toLong()
            result.desc = data["desc"].toString()
            result.host = data["host"].toString()
            result.name = data["name"].toString()
            result.location = data["location"].toString()
            data["members"]?.let {
                result.members = it as ArrayList<String>
            }
            data["comments"]?.let {
                result.comments = it as ArrayList<String>
            }
            return result
        }


        class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var time: TextView = itemView.findViewById(R.id.event_time)
            var name: TextView = itemView.findViewById(R.id.event_name)
            var location: TextView = itemView.findViewById(R.id.event_location)
            var detail: TextView = itemView.findViewById(R.id.event_detail)
            var hostBy: TextView = itemView.findViewById(R.id.event_host)
            var joinBtn: TextView = itemView.findViewById(R.id.join_btn)
            var memberNum: TextView = itemView.findViewById(R.id.event_member_num)
        }
    }

    companion object {

        val TAG = "SeaConnectLog"
    }

    interface OnItemClickListener {
        fun onItemClick(data: DocumentSnapshot)
    }

    interface OnJoinClickListener {
        fun onJoinClick(data: DocumentSnapshot, memberList: ArrayList<String>)
    }
}

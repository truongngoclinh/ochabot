package vn.ochabot.seaconnect.event

import android.os.Bundle
import android.support.v4.content.ContextCompat
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
    private lateinit var events: CollectionReference
    private var adapter = EventAdapter()
    lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = FirebaseFirestore.getInstance()
        events = db.collection("events")
        showEvents()
        recyclerView = findViewById(R.id.event_list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
//        findViewById<View>(R.id.create_match).setOnClickListener { addEvent() }
    }

    private fun addEvent() {
//        val data = HashMap<String, Any>()
//        data["host"] = "thien"
//        data["title"] = "Bi lac - tran dau dinh cao"
//        data["timestamp"] = System.currentTimeMillis()
//        events.document(System.nanoTime().toString()).set(data)
//            .addOnFailureListener { e -> Log.e(TAG, "add failed.", e) }
//            .addOnSuccessListener { Log.e(TAG, "add success") }
    }

    private fun showEvents() {
        events.addSnapshotListener(EventListener { values, e ->
            if (e != null) {
                Log.e(TAG, "Listen failed.", e)
                return@EventListener
            }

            adapter.mData.clear()
            for (doc in values!!) {
                adapter.mData.add(convertData(doc))
                Log.d(TAG, doc.toString())
            }
            adapter.mData.sortWith(Comparator { d1, d2 ->
                (d1.time - d2.time).toInt()
            })
            adapter.notifyDataSetChanged()
        })
    }

    private fun convertData(data: DocumentSnapshot): Event {
        val result = Event()
        result.time = data["time_stamp"].toString().toLong()
        result.desc = data["desc"].toString()
        result.host = data["host"].toString()
        result.name = data["name"].toString()
        result.location = data["location"].toString()
        result.members = data["members"] as ArrayList<String>
//        result.comments = data["comments"] as ArrayList<Comment>
        return result
    }

    override fun contentView(): Int {
        return R.layout.activity_event
    }

    override fun title(): Int {
        return 0
    }


    class EventAdapter : RecyclerView.Adapter<EventAdapter.MyViewHolder>() {

        var mData = ArrayList<Event>()

        override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
            val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.view_event_item, viewGroup, false)

            return MyViewHolder(view)
        }

        override fun getItemCount(): Int {
            return mData!!.size
        }

        override fun onBindViewHolder(viewHolder: MyViewHolder, i: Int) {
            val data = mData?.get(i)
            data?.let {
                viewHolder.time.text = SimpleDateFormat("MMM dd, YYYY hh:mm aa", Locale.ENGLISH).format(Date(it.time))
                viewHolder.location.text = it.location
                viewHolder.name.text = it.name
                viewHolder.detail.text = it.desc
                viewHolder.hostBy.text = it.host
                viewHolder.memberNum.text = it.members.size.toString()
                var joined = false
                it.members.forEach { member ->
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


}

package vn.ochabot.seaconnect.event

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_event_detail.*
import vn.ochabot.seaconnect.R
import vn.ochabot.seaconnect.core.App
import vn.ochabot.seaconnect.core.base.BaseActivity
import vn.ochabot.seaconnect.core.helpers.UserHelper
import vn.ochabot.seaconnect.model.Comment
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Thien.
 */
open class EventDetailActivity : BaseActivity() {
    final val EVENT_ID = "event_id"

    private lateinit var db: FirebaseFirestore
    lateinit var events: DocumentReference
    lateinit var comments: CollectionReference

    lateinit var eventId: String
    internal var matchSnapShot: DocumentSnapshot? = null

    var adapter = CommentAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        eventId = intent.getStringExtra(EVENT_ID)

        db = FirebaseFirestore.getInstance()
        events = db.collection("events").document(eventId)
        comments = db.collection("comments")

        findViewById<View>(R.id.ic_back).setOnClickListener { onBackPressed() }

        send_btn.setOnClickListener {
            addComment(comment_input.text.toString())
            hideKeyboard(comment_input)
            comment_input.text.clear()
        }

        comments_list.layoutManager = LinearLayoutManager(this)
        comments_list.adapter = adapter

        getEventDetail()
    }

    private fun addComment(cmt: String) {
        val data = HashMap<String, Any>()
        data["user"] = UserHelper.getUserName()
        data["cmt"] = cmt
        data["eventId"] = eventId
        data["time_stamp"] = System.currentTimeMillis()
        comments.document(System.nanoTime().toString()).set(data)
            .addOnFailureListener(OnFailureListener { e -> Log.e(BaseActivity.TAG, "add failed.", e) })
            .addOnSuccessListener(
                OnSuccessListener<Void> {
                    Log.e(BaseActivity.TAG, "add success")
                })
    }

    private fun getEventDetail() {
        events
        events.addSnapshotListener(EventListener { values, e ->
            if (e == null && values != null && values.exists()) {
                updateUI(values)
            } else {
                finish()
            }
        })
        comments.addSnapshotListener(EventListener { values, e ->
            if (e != null) {
                Log.e(EventsActivity.TAG, "Listen failed.", e)
                return@EventListener
            }

            adapter.mData.clear()
            for (doc in values!!) {
                if (doc["eventId"].toString().equals(eventId)) {
                    adapter.mData.add(doc)
                }
            }
            adapter.mData.sortWith(Comparator { d1, d2 ->
                (d2["time_stamp"].toString().toLong() - d1["time_stamp"].toString().toLong()).toInt()
            })

            comments_label.text = getString(R.string.label_comments, adapter.mData.size)

            adapter.notifyDataSetChanged()
        })
    }

    private fun updateUI(documentSnapshot: DocumentSnapshot?) {

        matchSnapShot = documentSnapshot

        matchSnapShot?.let {
            event_host.text = it["host"].toString()
            event_location.text = it["location"].toString()
            event_desc.text = it["desc"].toString()
            event_name.text = it["name"].toString()
            val time = it["time_stamp"].toString().toLong()
            event_time.text = SimpleDateFormat("hh:mm aa, MMM dd", Locale.ENGLISH).format(Date(time)) + " - " +
                    SimpleDateFormat("hh:mm aa, MMM dd", Locale.ENGLISH).format(Date(time + 3 * 60 * 60 * 1000))
            val members = (it["members"] as ArrayList<String>)
            attendances_label.text = getString(R.string.label_attendances, members.size)
            for (member in members) {
                val avatarView = CircleImageView(this)
                Glide.with(App.appContext).load(UserHelper.getAvatarUrl(member))
                    .apply(RequestOptions().override(100, 100))
                    .into(avatarView)
                val layoutParams =
                    LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                layoutParams.setMargins(0, 20, 20, 0)
                attendances_container.addView(avatarView, layoutParams)
            }
        }
    }

    override fun contentView(): Int {
        return R.layout.activity_event_detail
    }

    override fun title(): Int {
        return 0
    }

    fun hideKeyboard(v: View?) {
        if (v == null) {
            return
        }

        val context = v.context ?: return

        val token = v.windowToken ?: return

        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(token, 0)
    }

    open class CommentAdapter : RecyclerView.Adapter<CommentAdapter.MyViewHolder>() {

        var mData = ArrayList<DocumentSnapshot>()

        override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
            val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.view_comment_item, viewGroup, false)
            return MyViewHolder(view)
        }

        override fun getItemCount(): Int {
            return mData!!.size
        }

        override fun onBindViewHolder(viewHolder: MyViewHolder, i: Int) {
            val data = mData?.get(i)
            viewHolder.itemView.tag = data
            data?.let {
                val convertData = convertData(it)
                viewHolder.user.text = convertData.user
                viewHolder.cmt.text = convertData.cmt
            }
        }

        fun convertData(data: DocumentSnapshot): Comment {
            val result = Comment()
            result.user = data["user"].toString()
            result.cmt = data["cmt"].toString()
            return result
        }


        class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var user: TextView = itemView.findViewById(R.id.user)
            var cmt: TextView = itemView.findViewById(R.id.cmt)
        }
    }

}

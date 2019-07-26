package vn.ochabot.seaconnect.event

import android.os.Bundle
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_event_detail.*
import vn.ochabot.seaconnect.R
import vn.ochabot.seaconnect.core.base.BaseActivity
import vn.ochabot.seaconnect.core.helpers.UserHelper
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Thien.
 */
open class EventDetailActivity : BaseActivity() {
    final val EVENT_ID = "event_id"

    private lateinit var db: FirebaseFirestore
    lateinit var events: DocumentReference
    lateinit var eventId: String
    internal var matchSnapShot: DocumentSnapshot? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        eventId = intent.getStringExtra(EVENT_ID)

        db = FirebaseFirestore.getInstance()
        events = db.collection("events").document(eventId)

        getEventDetail()
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
    }

    private fun updateUI(documentSnapshot: DocumentSnapshot?) {

        matchSnapShot = documentSnapshot

        matchSnapShot?.let {
            event_host.text = it["host"].toString()
            event_location.text = it["location"].toString()
            event_desc.text = it["desc"].toString()
            event_name.text = it["name"].toString()
            val time = it["time_stamp"].toString().toLong()
            event_time.text = SimpleDateFormat("hh:mm aa, DD MMM", Locale.ENGLISH).format(Date(time)) + " - " +
                    SimpleDateFormat("hh:mm aa, DD MMM", Locale.ENGLISH).format(Date(time + 3 * 60 * 60 * 1000))
            val members = (it["members"] as ArrayList<String>)
            attendances_label.text = getString(R.string.label_attendances, members.size)
            for (member in members) {
                val avatarView = CircleImageView(this)
                Glide.with(this).load(UserHelper.getAvatarUrl(member)).apply(RequestOptions().override(100, 100))
                    .into(avatarView)
                val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                layoutParams.topMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, resources.displayMetrics).toInt()
                layoutParams.marginEnd = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, resources.displayMetrics).toInt()
                attendances_container.addView(avatarView)
            }
        }
    }

    override fun contentView(): Int {
        return R.layout.activity_event_detail
    }

    override fun title(): Int {
        return 0
    }
}

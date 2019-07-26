package vn.ochabot.seaconnect.challenges

import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_challenge_add.*
import vn.ochabot.seaconnect.R
import vn.ochabot.seaconnect.core.base.BaseActivity
import vn.ochabot.seaconnect.core.helpers.UserHelper
import java.util.*

/**
 * @author Thien.
 */
class ChallengesAddActivity : BaseActivity() {

    internal lateinit var db: FirebaseFirestore
    internal lateinit var matches: CollectionReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = FirebaseFirestore.getInstance()
        matches = db.collection("matches")
        submit_btn.setOnClickListener { addMatch() }
        ic_back.setOnClickListener { onBackPressed() }
        match_host.setText("Host: " + UserHelper.getUserName())
    }

    private fun addMatch() {
        val data = HashMap<String, Any>()
        data[Match.HOST] = UserHelper.getUserName()
        data[Match.TITLE] = match_title.text.toString()
        data[Match.MATCH_REWARD] = match_reward.text.toString()
        data[Match.MATCH_STATUS] = 0
        data[Match.TIME_STAMP] = System.currentTimeMillis()
        data[Match.TEAM_1] = ArrayList<String>()
        data[Match.TEAM_2] = ArrayList<String>()
        matches.document(System.nanoTime().toString()).set(data)
            .addOnFailureListener { e -> Log.e(BaseActivity.TAG, "add failed.", e) }
            .addOnSuccessListener { finish() }
    }


    override fun contentView(): Int {
        return R.layout.activity_challenge_add
    }

    override fun title(): Int {
        return 0
    }


}

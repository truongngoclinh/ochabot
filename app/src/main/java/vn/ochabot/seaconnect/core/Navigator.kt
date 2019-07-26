package vn.ochabot.seaconnect.core

import android.app.Activity
import android.content.Intent
import android.provider.CalendarContract
import android.support.v4.app.ActivityOptionsCompat
import android.widget.ImageView
import vn.ochabot.seaconnect.R
import vn.ochabot.seaconnect.challenges.ChallengesActivity
import vn.ochabot.seaconnect.core.base.BaseActivity
import vn.ochabot.seaconnect.event.EventDetailActivity
import vn.ochabot.seaconnect.event.EventsActivity
import vn.ochabot.seaconnect.lunch.LunchActivity
import vn.ochabot.seaconnect.lunch.ShareLunchActivity
import javax.inject.Inject

/**
 * @author linhtruong
 */
class Navigator @Inject constructor() {
    private fun activityTransitionSlide(activity: Activity) {
        activity.overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left)
    }

    fun openLunchActivity(activity: BaseActivity) {
        activity.startActivity(Intent(activity, LunchActivity::class.java))
        activityTransitionSlide(activity)
    }

    fun openShareLunchActivity(activity: BaseActivity, id: String) {
        activity.startActivity(ShareLunchActivity.forActivity(activity, id))
        activityTransitionSlide(activity)
    }

    fun openShareLunchActivity(activity: BaseActivity, id: String, view: ImageView) {
        var activityCompats = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, "image_your_meal")
        activity.startActivity(ShareLunchActivity.forActivity(activity, id), activityCompats.toBundle())
//        activityTransitionSlide(activity)
    }


    fun openEventsActivity(activity: BaseActivity) {
        activity.startActivity(Intent(activity, EventsActivity::class.java))
        activityTransitionSlide(activity)
    }

    fun openChallengesActivity(activity: BaseActivity) {
        activity.startActivity(Intent(activity, ChallengesActivity::class.java))
        activityTransitionSlide(activity)
    }

    fun openEventDetailActivity(activity: BaseActivity, eventId: String) {
        val intent = Intent(activity, EventDetailActivity::class.java)
        intent.putExtra(CalendarContract.Instances.EVENT_ID, eventId)
        activity.startActivity(intent)
        activityTransitionSlide(activity)
    }
}


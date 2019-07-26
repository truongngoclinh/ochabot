package vn.ochabot.seaconnect.core

import android.app.Activity
import android.content.Intent
import vn.ochabot.seaconnect.R
import vn.ochabot.seaconnect.core.base.BaseActivity
import vn.ochabot.seaconnect.event.EventsActivity
import vn.ochabot.seaconnect.lunch.LunchActivity
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

    fun openEventsActivity(activity: BaseActivity) {
        activity.startActivity(Intent(activity, EventsActivity::class.java))
        activityTransitionSlide(activity)
    }
}


package vn.ochabot.seaconnect.core

import android.app.Activity
import vn.ochabot.seaconnect.R
import javax.inject.Inject

/**
 * @author linhtruong
 */
class Navigator @Inject constructor() {
    private fun activityTransitionSlide(activity: Activity) {
        activity.overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left)
    }
//
//    fun openDetailActivity(activity: BaseActivity, newsDetail: NewsDetailEntity) {
//        activity.startActivity(DetailActivity.forDetail(activity, newsDetail))
//        activityTransitionSlide(activity)
//    }
}


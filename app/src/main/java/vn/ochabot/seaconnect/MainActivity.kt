package vn.ochabot.seaconnect

import android.os.Bundle
import android.view.View
import android.widget.TextView
import vn.ochabot.seaconnect.core.base.BaseActivity
import vn.ochabot.seaconnect.core.helpers.UserHelper

class MainActivity : BaseActivity() {
    override fun title(): Int = R.string.label_app_name
    override fun contentView(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        findViewById<TextView>(R.id.user_name).text = UserHelper.getUserName()

        findViewById<View>(R.id.lunch_menu).setOnClickListener {
            navigator.openLunchActivity(this)
        }

        findViewById<View>(R.id.challenge_menu).setOnClickListener {
            //Todo
        }

        findViewById<View>(R.id.event_menu).setOnClickListener {
            navigator.openEventsActivity(this)
        }

        findViewById<View>(R.id.setting_menu).setOnClickListener {
            //Todo
        }

        findViewById<View>(R.id.logout_btn).setOnClickListener {
            finish()
        }

    }

}

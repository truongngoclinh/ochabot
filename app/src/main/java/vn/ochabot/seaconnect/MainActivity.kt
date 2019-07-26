package vn.ochabot.seaconnect

import android.os.Bundle
import android.view.View
import android.widget.TextView
import vn.ochabot.seaconnect.core.base.BaseActivity
import vn.ochabot.seaconnect.model.User

class MainActivity : BaseActivity() {
    override fun title(): Int = R.string.label_app_name
    override fun contentView(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user = User("1", "nhutlm", "Le\nMinh Nhut", "minhnhut.le@ocha.vn", "0968373869")

        findViewById<TextView>(R.id.user_name).text = user.fullName

        findViewById<View>(R.id.lunch_menu).setOnClickListener {
            navigator.openLunchActivity(this)
        }

        findViewById<View>(R.id.challenge_menu).setOnClickListener {
            //Todo
        }

        findViewById<View>(R.id.event_menu).setOnClickListener {
            //Todo
        }

        findViewById<View>(R.id.setting_menu).setOnClickListener {
            //Todo
        }

        findViewById<View>(R.id.logout_btn).setOnClickListener {
            finish()
        }

    }

}

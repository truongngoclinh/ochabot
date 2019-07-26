package vn.ochabot.seaconnect

import android.os.Bundle
import vn.ochabot.seaconnect.core.base.BaseActivity
import vn.ochabot.seaconnect.model.User

class MainActivity : BaseActivity() {

    override fun title(): Int = R.string.label_app_name
    override fun contentView(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user = User("1", "nhutlm", "Le Minh Nhut", "minhnhut.le@ocha.vn", "0968373869")


    }
}

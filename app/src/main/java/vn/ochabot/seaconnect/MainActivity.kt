package vn.ochabot.seaconnect

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import vn.ochabot.seaconnect.core.Navigator
import vn.ochabot.seaconnect.core.base.BaseActivity
import vn.ochabot.seaconnect.model.User
import javax.inject.Inject

class MainActivity : BaseActivity() {
    override fun title(): Int = R.string.label_app_name
    override fun contentView(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user = User("1", "nhutlm", "Le Minh Nhut", "minhnhut.le@ocha.vn", "0968373869")
    }

    override fun onCreateView(savedInstanceState: Bundle?) {
        appComponent.inject(this)
        oc_menu_group_1.setOnClickListener {
            navigator.openLunchActivity(this)
        }
    }
}

package vn.ochabot.seaconnect

import vn.ochabot.seaconnect.core.base.BaseActivity

class MainActivity : BaseActivity() {
    override fun title(): Int = R.string.label_app_name
    override fun contentView(): Int = R.layout.activity_main
}

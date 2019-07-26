package vn.ochabot.seaconnect

import android.content.Intent
import android.os.Bundle
import android.view.View
import vn.ochabot.seaconnect.core.base.BaseActivity

class LoginActivity : BaseActivity() {
    override fun title(): Int = R.string.label_app_name
    override fun contentView(): Int = R.layout.activity_login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findViewById<View>(R.id.login_btn).setOnClickListener {
            renderLoading(true)
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            hideLoading()
        }

    }
}

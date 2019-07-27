package vn.ochabot.seaconnect.core.helpers

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import vn.ochabot.seaconnect.R

class AvatarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.oc_view_bill_duration)

        findViewById<View>(R.id.oc_btn_ok).setOnClickListener {
            finish()
        }
    }

}

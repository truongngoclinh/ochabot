package vn.ochabot.seaconnect

import android.os.Bundle
import android.text.TextUtils
import kotlinx.android.synthetic.main.activity_main.*
import vn.ochabot.seaconnect.core.base.BaseActivity
import vn.ochabot.seaconnect.core.base.extension.loading
import vn.ochabot.seaconnect.core.base.extension.observe
import vn.ochabot.seaconnect.core.extension.viewModel
import vn.ochabot.seaconnect.core.helpers.UserHelper

class MainActivity : BaseActivity() {
    override fun title(): Int = R.string.label_app_name
    override fun contentView(): Int = R.layout.activity_main

    private lateinit var viewModel: MainViewModel
    private lateinit var selectedId: String

    override fun onCreateView(savedInstanceState: Bundle?) {
        appComponent.inject(this)
        viewModel = viewModel(viewModelFactory) {
            loading(loadingStatus, ::renderLoading)
            observe(isTimeExpired, ::handleConfig)
            observe(foodSelectedId, ::handleSelectedId)
        }
        viewModel.getCurrentSelectedFoodIfAny()

        user_name.text = UserHelper.getUserName()
        challenge_menu.setOnClickListener {
            navigator.openChallengesActivity(this)
        }
        event_menu.setOnClickListener {
            navigator.openEventsActivity(this)
        }
        setting_menu.setOnClickListener {
        }
        logout_btn.setOnClickListener {
            finish()
        }
    }

    private fun handleSelectedId(id: String?) {
        selectedId = id!!
        if (TextUtils.isEmpty(id)) {
            viewModel.getConfigs()
        } else {
            renderLoading(false)
            lunch_menu.setOnClickListener {
                navigator.openShareLunchActivity(this@MainActivity, selectedId)
            }
        }
    }

    private fun handleConfig(isTimeExpired: Boolean?) {
        renderLoading(false)
        lunch_menu.setOnClickListener {
            isTimeExpired?.let {
                if (!isTimeExpired) {
                    navigator.openLunchActivity(this@MainActivity)
                } else {
                    navigator.openShareLunchActivity(this@MainActivity, selectedId)
                }
            }
        }
    }
}

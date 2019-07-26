package vn.ochabot.seaconnect.core

import android.app.Application
import timber.log.Timber
import vn.ochabot.seaconnect.BuildConfig
import vn.ochabot.seaconnect.core.di.AppComponent


/**
 * @author linhtruong
 */
class App : Application() {
    val appComponent: AppComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        AppComponent.init(this)
    }

    companion object {
        lateinit var appContext: App
    }

    override fun onCreate() {
        super.onCreate()
        initUtils()
        initAppComponent()
    }

    private fun initUtils() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initAppComponent() {
        appComponent.inject(this)
        appContext = this
    }
}
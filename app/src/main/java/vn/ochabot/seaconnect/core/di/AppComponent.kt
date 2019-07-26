package vn.ochabot.seaconnect.core.di

import android.app.Application
import com.google.gson.Gson
import dagger.Component
import vn.ochabot.seaconnect.core.App
import vn.ochabot.seaconnect.core.base.BaseActivity
import vn.ochabot.seaconnect.core.base.BaseViewModel
import vn.ochabot.seaconnect.core.di.module.AppModule
import vn.ochabot.seaconnect.core.di.module.RepositoryModule
import vn.ochabot.seaconnect.core.di.viewmodel.ViewModelModule
import javax.inject.Singleton

/**
 * @author linhtruong
 */
@Singleton
@Component(modules = [AppModule::class, RepositoryModule::class, ViewModelModule::class])
interface AppComponent {
    companion object Initializer {
        fun init(app: App): AppComponent {
            return DaggerAppComponent.builder().appModule(AppModule(app)).build()
        }
    }

    fun application(): Application
    fun gson(): Gson

    fun inject(app: App)
    fun inject(activity: BaseActivity)
    fun inject(viewModel: BaseViewModel)
}
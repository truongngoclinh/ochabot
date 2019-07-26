package vn.ochabot.seaconnect.core.di.module

import android.app.Application
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import vn.ochabot.seaconnect.core.App
import javax.inject.Singleton

/**
 * @author linhtruong
 */
@Module
class AppModule(private val application: App) {
    @Provides
    @Singleton
    fun provideApplicationContext(): Application = application

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()
}
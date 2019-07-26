package vn.ochabot.seaconnect.core.di.module

import android.app.Application
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
}
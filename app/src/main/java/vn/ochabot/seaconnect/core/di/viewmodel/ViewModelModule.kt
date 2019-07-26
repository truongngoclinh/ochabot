package vn.ochabot.seaconnect.core.di.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import vn.ochabot.seaconnect.lunch.LunchViewModel
import javax.inject.Singleton

/**
 * @author linhtruong
 */
@Module
abstract class ViewModelModule {
    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @Singleton
    @ViewModelKey(LunchViewModel::class)
    abstract fun bindsLunchViewModel(lunchViewModel: LunchViewModel): ViewModel
}

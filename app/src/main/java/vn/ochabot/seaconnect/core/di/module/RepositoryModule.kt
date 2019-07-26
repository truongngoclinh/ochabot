package vn.ochabot.seaconnect.core.di.module

import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import vn.ochabot.seaconnect.core.helpers.ResourcesHelper
import vn.ochabot.seaconnect.lunch.Lunch
import javax.inject.Singleton


/**
 * @author linhtruong
 */
@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun provideMealsData(): ArrayList<Lunch> {
        val json = ResourcesHelper.getAsset("meals.json")
        val typeToken = object : TypeToken<List<Lunch>>() {}.type

        return Gson().fromJson<ArrayList<Lunch>>(json, typeToken)
    }
}
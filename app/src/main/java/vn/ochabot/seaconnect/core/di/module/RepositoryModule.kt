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
    private var meals: ArrayList<Lunch>

    init {
        val json = ResourcesHelper.getAsset("meals.json")
        val typeToken = object : TypeToken<List<Lunch>>() {}.type

        meals = Gson().fromJson<ArrayList<Lunch>>(json, typeToken)
    }

    @Provides
    @Singleton
    fun provideMealsData(): ArrayList<Lunch> {
        return meals
    }
}
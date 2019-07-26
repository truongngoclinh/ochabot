package vn.ochabot.seaconnect.lunch

import android.arch.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import vn.ochabot.seaconnect.core.base.BaseViewModel
import vn.ochabot.seaconnect.core.helpers.ResourcesHelper
import javax.inject.Inject

/**
 * @author linhtruong
 */
class LunchViewModel
@Inject constructor() : BaseViewModel() {
    var lunchData = MutableLiveData<ArrayList<Lunch>>()
    var shareLunchData = MutableLiveData<ArrayList<Lunch>>()

    fun getLunchData() {
        loadingStatus.postValue(true)
        val json = ResourcesHelper.fromAsset(Lunch::class.java, "meals.json")
        val typeToken = object : TypeToken<List<Lunch>>() {}.type

        lunchData.postValue(Gson().fromJson<ArrayList<Lunch>>(json, typeToken))
    }

    fun getShareLunchData() {
        loadingStatus.postValue(true)
        val json = ResourcesHelper.fromAsset(Lunch::class.java, "meals.json")
        val typeToken = object : TypeToken<List<Lunch>>() {}.type

        shareLunchData.postValue(Gson().fromJson<ArrayList<Lunch>>(json, typeToken))
    }
}
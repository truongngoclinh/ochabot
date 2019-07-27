package vn.ochabot.seaconnect

import android.arch.lifecycle.MutableLiveData
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber
import vn.ochabot.seaconnect.core.AppConst
import vn.ochabot.seaconnect.core.base.BaseViewModel
import vn.ochabot.seaconnect.core.di.AppComponent
import vn.ochabot.seaconnect.core.helpers.UserHelper
import javax.inject.Inject


/**
 * @author linhtruong
 */
class MainViewModel
@Inject constructor() : BaseViewModel() {
    @Inject
    lateinit var db: FirebaseFirestore

    var isTimeExpired = MutableLiveData<Boolean>()
    var foodSelectedId = MutableLiveData<String>()

    fun getConfigs() {
        loadingStatus.postValue(true)
        db.collection(AppConst.CONFIG).addSnapshotListener { values, ex ->
            if (ex != null) {
                Timber.e("Cant get config")
            } else {
                values!!.documents.forEach {
                    val result = it.data!![AppConst.TIME_EXPIRED] as Boolean
                    isTimeExpired.postValue(result)
                }
            }
        }
    }

    fun getCurrentSelectedFoodIfAny() {
        loadingStatus.postValue(true)
        db.collection(AppConst.FOOD_SELECTED).addSnapshotListener { values, ex ->
            if (ex != null) {
                Timber.e("Get food selected table error")
            } else {
                var isFoodSelected = false
                values!!.documents.forEach {
                    val user = it.data!![AppConst.USER].toString()
                    if (user.equals(UserHelper.getUserId(), true)) {
                        foodSelectedId.postValue(it.data!![AppConst.MEAL_ID].toString())
                        isFoodSelected = true
                    }
                }

                if (!isFoodSelected) {
                    foodSelectedId.postValue("")
                }
            }
        }
    }
}
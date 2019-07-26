package vn.ochabot.seaconnect.lunch

import android.arch.lifecycle.MutableLiveData
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import vn.ochabot.seaconnect.core.base.BaseViewModel
import javax.inject.Inject

/**
 * @author linhtruong
 */
class LunchViewModel
@Inject constructor() : BaseViewModel() {
    @Inject
    lateinit var mokeData: ArrayList<Lunch>

    var lunchData = MutableLiveData<ArrayList<Lunch>>()

    fun getLunchData() {
        loadingStatus.postValue(true)
        lunchData.postValue(mokeData)
    }
}
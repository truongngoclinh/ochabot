package vn.ochabot.seaconnect

import android.arch.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber
import vn.ochabot.seaconnect.core.AppConst
import vn.ochabot.seaconnect.core.base.BaseViewModel
import javax.inject.Inject


/**
 * @author linhtruong
 */
class MainViewModel
@Inject constructor() : BaseViewModel() {
    @Inject
    lateinit var db: FirebaseFirestore

    var isTimeExpired = MutableLiveData<Boolean>()

    fun getConfigs() {
        loadingStatus.postValue(true)
        db.collection(AppConst.TIME_EXPIRED).addSnapshotListener { values, ex ->
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
}
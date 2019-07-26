package vn.ochabot.seaconnect.lunch

import android.arch.lifecycle.MutableLiveData
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber
import vn.ochabot.seaconnect.core.base.BaseViewModel
import javax.inject.Inject

/**
 * @author linhtruong
 */
class ShareLunchViewModel
@Inject constructor() : BaseViewModel() {
    @Inject
    lateinit var db: FirebaseFirestore

    @Inject
    lateinit var mokeData: ArrayList<Lunch>

    var shareLunchData = MutableLiveData<ArrayList<Lunch>>()
    var isLunchShared = MutableLiveData<Boolean>()
    var isLunchAccepted = MutableLiveData<Boolean>()

    fun getShareLunchData() {
        loadingStatus.postValue(true)
        db.collection(Lunch.DB).addSnapshotListener { values, ex ->
            if (ex != null) {
                shareLunchData.postValue(ArrayList())
            } else {
                var result = ArrayList<Lunch>()
                values!!.documents.forEach {
                    val ref = it.id
                    it.data?.let { map ->
                        val id = map[Lunch.ID].toString()
                        val item = getLunchForId(id)
                        item.source = map[Lunch.SOURCE].toString()
                        item.des = map[Lunch.DES].toString()
                        item.ref = ref

                        result.add(item)
                    }
                }

                shareLunchData.postValue(result)
            }
        }
    }

    fun shareLunch(id: String) {
        loadingStatus.postValue(true)
        db.collection(Lunch.DB).document(System.nanoTime().toString()).set(getLunchForId(id)).addOnSuccessListener {
            loadingStatus.postValue(false)
            isLunchShared.postValue(true)
        }.addOnFailureListener {
            Timber.e("Sharing failed: " + it.localizedMessage)
        }
    }

    fun acceptLunch(lunch: Lunch) {
        Timber.d("Accepting ref: " + lunch.ref)
        loadingStatus.postValue(true)
        lunch.des = "truongngoclinh"
        val map = HashMap<String, Any>()
        map[Lunch.DES] = lunch.des
        db.collection(Lunch.DB).document(lunch.ref).update(map).addOnFailureListener {
            Timber.e("Accepted failed: %s", it)
        }.addOnSuccessListener {
            isLunchAccepted.postValue(true)
        }

    }

    fun getLunchForId(id: String): Lunch {
        for (item in mokeData) {
            if (item.id.equals(id, true)) {
                item.source = "truongngoclinh"
                return item
            }
        }

        return Lunch()
    }
}
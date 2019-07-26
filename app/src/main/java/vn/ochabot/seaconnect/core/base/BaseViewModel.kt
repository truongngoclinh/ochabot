//package com.linhtruong.sample.core.platform.base
//
//import android.arch.lifecycle.MutableLiveData
//import android.arch.lifecycle.ViewModel
////import io.reactivex.disposables.CompositeDisposable
////import io.reactivex.disposables.Disposable
//import javax.inject.Inject
//
//
///**
// * @author linhtruong
// */
//abstract class BaseViewModel : ViewModel() {
//    @Inject lateinit var networkHelper: NetworkHelper
//
//    private val disposables = CompositeDisposable()
//    var failure: MutableLiveData<Failure> = MutableLiveData()
//    var loadingStatus: MutableLiveData<Boolean> = MutableLiveData()
//
//    open fun handleFailure(failure: Failure?) {
//        this.failure.postValue(failure)
//    }
//
//    fun addDisposable(disposable: Disposable) {
//        if (networkHelper.isConnected()) {
//            disposables.add(disposable)
//        } else {
//            handleFailure(NetworkError())
//        }
//    }
//
//    override fun onCleared() {
//        super.onCleared()
//        disposables.clear()
//    }
//}
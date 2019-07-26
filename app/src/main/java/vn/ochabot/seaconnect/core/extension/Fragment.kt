package vn.ochabot.seaconnect.core.base.extension

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import vn.ochabot.seaconnect.R


/**
 * @author linhtruong
 */
inline fun FragmentManager.inTransaction(animated: Boolean, func: FragmentTransaction.() -> FragmentTransaction) {
    if (animated) beginTransaction()
        .setCustomAnimations(
            R.anim.anim_slide_in_right, R.anim.anim_slide_out_left,
            R.anim.anim_slide_in_left, R.anim.anim_slide_out_right
        )
        .func().commit()
    else beginTransaction().func().commit()
}

inline fun <reified T : ViewModel> Fragment.viewModel(factory: ViewModelProvider.Factory, body: T.() -> Unit): T {
    val vm = ViewModelProviders.of(this, factory)[T::class.java]
    vm.body()
    return vm
}

//val BaseFragment.baseActivity: BaseActivity get() = (activity as BaseActivity)

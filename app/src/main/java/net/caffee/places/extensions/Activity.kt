package net.caffee.places.extensions

import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import net.caffee.places.R
import net.caffee.places.architecture.ViewModelFactory

fun AppCompatActivity.replaceFragment(
        containerResId: Int,
        fragment: Fragment,
        isAddToBackStack: Boolean = false,
        tag: String = "",
        animation: Boolean = false
) {
    if (supportFragmentManager.findFragmentByTag(fragment::class.simpleName+tag) != null) return
    val transaction = supportFragmentManager.beginTransaction()
    if (animation) {
        transaction.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_from_right,
                R.anim.slide_in_from_left, R.anim.slide_out_from_left)
    }
    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
    transaction.replace(containerResId, fragment, if (tag == "") fragment::class.simpleName else tag)
    if (isAddToBackStack) transaction.addToBackStack(if (tag == "") fragment::class.simpleName else tag)
    transaction.commit()
}

fun AppCompatActivity.replaceFragmentFromBottom(
        containerResId: Int,
        fragment: Fragment,
        tag: String = "",
        isAddToBackStack: Boolean = false
    ) {
    if (supportFragmentManager.findFragmentByTag(fragment::class.simpleName+tag) != null) return
    val transaction = supportFragmentManager.beginTransaction()
    transaction.setCustomAnimations(R.anim.slide_in_from_bottom, R.anim.slide_out_to_top/*, R.anim.slide_in_from_left, R.anim.slide_out_from_left*/)
    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
    transaction.replace(containerResId, fragment, fragment::class.simpleName+tag)
    if (isAddToBackStack) transaction.addToBackStack(fragment::class.simpleName)
    transaction.commit()
}

fun AppCompatActivity.popBackStack() {
    supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
}

fun <T : AndroidViewModel> FragmentActivity.obtainViewModel(viewModelClass: Class<T>) =
        ViewModelProviders.of(this, ViewModelFactory.getInstance(application)).get(viewModelClass)

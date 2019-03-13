package net.caffee.places.extensions

import android.annotation.SuppressLint
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import android.util.Log

const val TAG = "BottomNavigationView"

@SuppressLint("RestrictedApi")
fun BottomNavigationView.removeShiftingMode() {
    val menuView = this.getChildAt(0) as BottomNavigationMenuView
    try {
        val shiftingMode = menuView.javaClass.getDeclaredField("mShiftingMode")
        shiftingMode.isAccessible = true
        shiftingMode.setBoolean(menuView, false)
        shiftingMode.isAccessible = false
        for (i in 0 until menuView.childCount) {
            val bottomNavigationItemView = menuView.getChildAt(i) as BottomNavigationItemView
            bottomNavigationItemView.setShiftingMode(false)
            bottomNavigationItemView.setChecked(bottomNavigationItemView.itemData.isChecked)
        }
    } catch (noSuchFieldException: NoSuchFieldException) {
        Log.e(TAG, "NoSuchFieldException", noSuchFieldException)
    } catch (illegalAccessException: IllegalAccessException) {
        Log.e(TAG, "IllegalAccessException", illegalAccessException)
    }
}
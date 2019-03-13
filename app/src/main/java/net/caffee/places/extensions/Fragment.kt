package net.caffee.places.extensions

import android.arch.lifecycle.Observer
import android.databinding.ViewDataBinding
import android.support.v4.app.Fragment
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.PopupWindow
import android.widget.TextView
import net.caffee.places.R
import net.caffee.places.util.SingleLiveEvent


fun Fragment.showMenuItem(fragment: Fragment, menuItem: MenuItem?, event: SingleLiveEvent<Int>, function: (Int) -> Unit) {
    event.observe(fragment, Observer {
        it?.let {
            val basketsCount = it
            menuItem?.isVisible = (basketsCount > 0)
            if (basketsCount > 0) {
                val badgeContainer = menuItem?.actionView as FrameLayout
                val badgeTextView = badgeContainer.findViewById(R.id.badgeTextView) as TextView
                badgeTextView.text = basketsCount.toString()
                badgeContainer.setOnClickListener { function(basketsCount) }
            }
        }
    })
}



fun Fragment.initPopap(popupWindow : PopupWindow?, binding: ViewDataBinding, view: View?) : PopupWindow? {
    popupWindow?.isFocusable = true
    popupWindow?.width = WindowManager.LayoutParams.MATCH_PARENT
    popupWindow?.height = WindowManager.LayoutParams.WRAP_CONTENT
    popupWindow?.contentView = binding.root
    popupWindow?.setBackgroundDrawable(null)
    popupWindow?.showAsDropDown(view)
    return popupWindow;
}
package net.caffee.places.extensions

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

fun ViewGroup.inflate(
        @LayoutRes layoutResId: Int,
        attachToRoot: Boolean = false
): View = LayoutInflater.from(context).inflate(layoutResId, this, attachToRoot)
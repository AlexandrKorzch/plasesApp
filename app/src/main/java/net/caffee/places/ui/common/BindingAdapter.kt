package net.caffee.places.ui.common

import android.graphics.Typeface
import android.support.v7.widget.AppCompatTextView
import android.databinding.BindingAdapter
import android.widget.ImageView


@BindingAdapter("android:customTypeface")
fun setTypeface(v: AppCompatTextView, style: String) {
    when (style) {
        "bold" -> v.setTypeface(null, Typeface.BOLD)
        else -> v.setTypeface(null, Typeface.NORMAL)
    }
}

@BindingAdapter("android:customResource")
fun setImageResource(imageView: ImageView, resource: Int) {
    imageView.setImageResource(resource)
}
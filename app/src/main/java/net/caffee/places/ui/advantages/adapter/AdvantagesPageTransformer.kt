package net.caffee.places.ui.advantages.adapter

import android.support.v4.view.ViewPager
import android.view.View


class AdvantagesPageTransformer : ViewPager.PageTransformer {

    private val MIN_SCALE = 0.85f

    override fun transformPage(view: View, position: Float) {
        val pageWidth = view.width
        val pageHeight = view.height
        if (position <= 1) {
            val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
            val vertMargin = pageHeight * (1 - scaleFactor) / 2
            val horzMargin = pageWidth * (1 - scaleFactor) / 2
            if (position < 0) {
                view.rotation = -horzMargin + vertMargin / 2
            } else {
                view.rotation = horzMargin - vertMargin / 2
            }
            view.translationY = -pageHeight * position
            view.scaleX = scaleFactor
            view.scaleY = scaleFactor
        }
    }
}
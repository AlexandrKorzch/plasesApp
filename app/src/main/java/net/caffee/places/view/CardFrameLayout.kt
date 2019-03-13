package net.caffee.places.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import net.caffee.places.R

class CardFrameLayout(context: Context, attr: AttributeSet) : FrameLayout(context, attr) {

    override fun onFinishInflate() {
        setBackgroundResource(R.drawable.shape_card)
        super.onFinishInflate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec/2)
        val containerWidth = View.MeasureSpec.getSize(widthMeasureSpec)
        setMeasuredDimension(containerWidth, (containerWidth*0.58f).toInt())
    }
}
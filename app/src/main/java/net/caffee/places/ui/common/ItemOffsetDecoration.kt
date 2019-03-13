package net.caffee.places.ui.common

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class ItemOffsetDecoration(
        private val position: Int,
        private val offset: Int?
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
            outRect: Rect?,
            view: View?,
            parent: RecyclerView?,
            state: RecyclerView.State?
    ) {
        if (parent?.getChildAdapterPosition(view) == position) {
            outRect?.bottom = offset
        }
    }
}
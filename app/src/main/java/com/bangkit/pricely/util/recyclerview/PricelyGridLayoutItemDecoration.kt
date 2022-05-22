package com.bangkit.pricely.util.recyclerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class PricelyGridLayoutItemDecoration(
    private val spanCount: Int,
    private val spacing: Int

    ) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val columnPos = position % spanCount
        val rowPos = position.floorDiv(spanCount)

        val columnWidth = parent.width / spanCount
        val availableWidth: Int = if(columnPos == 0 || columnPos == spanCount - 1){
            columnWidth - (spacing / 2)
        }else{
            columnWidth - spacing
        }
        outRect.apply {
            val margin = (width() - availableWidth) / 2
            left = margin
            right = margin
            if(rowPos > 0) top = spacing
        }
    }
}
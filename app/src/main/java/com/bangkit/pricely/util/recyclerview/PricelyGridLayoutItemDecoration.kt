package com.bangkit.pricely.util.recyclerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.ceil

class PricelyGridLayoutItemDecoration(
    private val spanCount: Int,
    private val spacing: Int,
    private val edge: Int = 0,
    private val orientation: Int = GridLayoutManager.VERTICAL
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val columnPos = position % spanCount
        val rowPos = position.floorDiv(spanCount)
        val rowCount = ceil((parent.adapter?.itemCount ?: 0).toDouble() / spanCount.toDouble()).toInt()

        val columnWidth = parent.width / spanCount
        val availableWidth: Int = if(columnPos == 0 || columnPos == spanCount - 1){
            columnWidth - (spacing / 2)
        }else{
            columnWidth - spacing
        }
        outRect.apply {
            when(rowPos){
                0 -> {
                    top = edge
                    bottom = spacing / 2
                }
                rowCount - 1 -> {
                    top = spacing / 2
                    bottom = edge
                }
                else -> {
                    top = spacing / 2
                    bottom = spacing / 2
                }
            }
            val margin = (width() - availableWidth)
            when(columnPos){
                0 -> {
                    left = edge
                    right = (margin / 1.5).toInt()
                }
                spanCount - 1 -> {
                    left = (margin / 1.5).toInt()
                    right = edge
                }
                else -> {
                    left = (margin / 1.5).toInt()
                    right = (margin / 1.5).toInt()
                }
            }
        }
    }
}
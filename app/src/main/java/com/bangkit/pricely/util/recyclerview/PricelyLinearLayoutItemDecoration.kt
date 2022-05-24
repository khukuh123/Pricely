package com.bangkit.pricely.util.recyclerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PricelyLinearLayoutItemDecoration(
    private val spacing: Int,
    private val orientation: Int = LinearLayoutManager.VERTICAL,
    private val edge: Int = 0
): RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount ?: 0

        if(orientation == LinearLayoutManager.VERTICAL){
            when(position){
                0 -> {
                    outRect.top += edge
                    outRect.bottom += spacing / 2
                }
                itemCount - 1 -> {
                    outRect.top += spacing / 2
                    outRect.bottom += edge
                }
                else -> {
                    outRect.bottom += spacing / 2
                    outRect.top += spacing / 2
                }
            }
            outRect.left += edge
            outRect.right += edge
        }else{
            when (position) {
                0 -> {
                    outRect.left += edge
                    outRect.right += spacing / 2
                }
                itemCount - 1 -> {
                    outRect.left += spacing / 2
                    outRect.right += edge
                }
                else -> {
                    outRect.left += spacing / 2
                    outRect.right += spacing / 2
                }
            }
            outRect.top += edge / 2
            outRect.bottom += edge / 2
        }
    }
}
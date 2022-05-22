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

        }else{
            when (position) {
                0 -> {
                    outRect.left = edge
                }
                itemCount - 1 -> {
                    outRect.left = spacing
                    outRect.right = edge
                }
                else -> {
                    outRect.left = spacing
                }
            }
            (spacing / 2).let {
                outRect.top = it
                outRect.bottom = it
            }
        }
    }
}
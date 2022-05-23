package com.bangkit.pricely.base

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class BaseFooterAdapter<VH : BaseFooterViewHolder> : RecyclerView.Adapter<VH>() {

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = 1

    fun show(isShow: Boolean) {
        if (isShow) notifyItemInserted(0) else notifyItemRemoved(0)
    }
}
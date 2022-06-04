package com.bangkit.pricely.base

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class BaseFooterAdapter<VH : BaseFooterViewHolder> : RecyclerView.Adapter<VH>() {

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind()
    }

    override fun onBindViewHolder(holder: VH, position: Int, payloads: MutableList<Any>) {
        if(payloads.isEmpty())
            super.onBindViewHolder(holder, position, payloads)
        else
            holder.bind(payloads)
    }

    override fun getItemCount(): Int = 1
}
package com.bangkit.pricely.base

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseAdapter<T, VB : ViewBinding, VH : BaseViewHolder<T>>(
    diffUtil: DiffUtil.ItemCallback<T>,
) : ListAdapter<T, VH>(
    diffUtil
) {
    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }
}
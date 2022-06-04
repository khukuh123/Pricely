package com.bangkit.pricely.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseFooterViewHolder(private val _binding: ViewBinding): RecyclerView.ViewHolder(_binding.root) {
    protected val binding
        get() = _binding
    abstract fun bind()

    abstract fun bind(payload: MutableList<Any>)
}
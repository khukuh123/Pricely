package com.bangkit.pricely.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding


abstract class BaseViewHolder<T, VB : ViewBinding>(private val mBinding: VB) : RecyclerView.ViewHolder(mBinding.root) {
    val binding: VB
        get() = mBinding

    abstract fun bind(data: T)
}
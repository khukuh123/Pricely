package com.bangkit.pricely.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding


abstract class BaseViewHolder<T>(private val mBinding: ViewBinding) : RecyclerView.ViewHolder(mBinding.root) {
    val binding: ViewBinding
        get() = mBinding

    abstract fun bind(data: T)
}
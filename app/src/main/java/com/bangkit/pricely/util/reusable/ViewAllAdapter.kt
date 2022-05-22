package com.bangkit.pricely.util.reusable

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.bangkit.pricely.base.BaseFooterAdapter
import com.bangkit.pricely.base.BaseFooterViewHolder
import com.bangkit.pricely.databinding.ItemViewAllBinding

class ViewAllAdapter(
    private val onViewAllButtonClicked: () -> Unit
): BaseFooterAdapter<ViewAllAdapter.ViewAllViewHolder>() {
    inner class ViewAllViewHolder(_binding: ViewBinding): BaseFooterViewHolder(_binding){
        override fun bind() {
            with(binding as ItemViewAllBinding){
                btnViewAll.setOnClickListener {
                    onViewAllButtonClicked.invoke()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewAllViewHolder =
        ViewAllViewHolder(ItemViewAllBinding.inflate(LayoutInflater.from(parent.context), parent, false))
}
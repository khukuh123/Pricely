package com.bangkit.pricely.presentation.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.bangkit.pricely.base.BaseAdapter
import com.bangkit.pricely.base.BaseViewHolder
import com.bangkit.pricely.databinding.ItemSuggestionBinding
import com.bangkit.pricely.util.PricelyDiffUtil

class SuggestionAdapter(
    private val onItemClicked: (String) -> Unit
): BaseAdapter<String, ItemSuggestionBinding, SuggestionAdapter.SuggestionViewHolder>(
    PricelyDiffUtil.suggestionDiffUtil
) {
    inner class SuggestionViewHolder(mBinding: ViewBinding): BaseViewHolder<String>(mBinding){
        override fun bind(data: String) {
            with(binding as ItemSuggestionBinding){
                tvSuggestion.text = data
                root.setOnClickListener {
                    onItemClicked.invoke(data)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionViewHolder =
        SuggestionViewHolder(ItemSuggestionBinding.inflate(LayoutInflater.from(parent.context), parent, false))
}
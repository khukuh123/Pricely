package com.bangkit.pricely.presentation.main

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bangkit.pricely.base.BaseAdapter
import com.bangkit.pricely.base.BaseViewHolder
import com.bangkit.pricely.databinding.ItemCardBinding
import com.bangkit.pricely.domain.product.model.Product
import com.bangkit.pricely.util.PricelyDiffUtil
import com.bangkit.pricely.util.formatPrice

class SearchProductAdapter(private val onItemClicked: (Product) -> Unit) :
    BaseAdapter<Product, ItemCardBinding, SearchProductAdapter.SearchProductViewHolder>
        (PricelyDiffUtil.allProductDiffUtil) {

    inner class SearchProductViewHolder(mBinding: ItemCardBinding) :
        BaseViewHolder<Product, ItemCardBinding>(mBinding) {
        override fun bind(data: Product) {
            with(binding) {
                tvTitleItem.text = data.name
                tvPriceItem.text = formatPrice(data.price)
                tvWeightItem.text = data.unit
                root.setOnClickListener {
                    onItemClicked.invoke(data)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchProductAdapter.SearchProductViewHolder {
        val view = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchProductViewHolder(view)
    }
}
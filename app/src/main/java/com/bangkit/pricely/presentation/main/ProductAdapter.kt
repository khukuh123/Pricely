package com.bangkit.pricely.presentation.main

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bangkit.pricely.base.BaseAdapter
import com.bangkit.pricely.base.BaseViewHolder
import com.bangkit.pricely.databinding.ItemProductBinding
import com.bangkit.pricely.domain.product.model.Product
import com.bangkit.pricely.util.PricelyDiffUtil
import com.bangkit.pricely.util.formatPrice

class ProductAdapter(private val onItemClicked: (Product) -> Unit)
    : BaseAdapter<Product, ItemProductBinding, ProductAdapter.ProductViewHolder>
    (PricelyDiffUtil.productDiffUtil) {
    inner class ProductViewHolder(mBinding: ItemProductBinding):
        BaseViewHolder<Product, ItemProductBinding>(mBinding) {
        override fun bind(data: Product) {
            with(binding){
                tvTitleProduct.text = data.name
                tvPriceProduct.text = formatPrice(data.price)
                root.setOnClickListener {
                    onItemClicked.invoke(data)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder =
        ProductViewHolder(ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))
}

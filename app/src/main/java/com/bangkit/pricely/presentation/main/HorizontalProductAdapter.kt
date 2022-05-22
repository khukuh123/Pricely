package com.bangkit.pricely.presentation.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.bangkit.pricely.base.BaseAdapter
import com.bangkit.pricely.base.BaseViewHolder
import com.bangkit.pricely.databinding.ItemProductBinding
import com.bangkit.pricely.databinding.ItemViewAllBinding
import com.bangkit.pricely.domain.product.model.Product
import com.bangkit.pricely.util.PricelyDiffUtil
import com.bangkit.pricely.util.formatPrice

class HorizontalProductAdapter(
    private val onItemClicked: (Product) -> Unit,
    private val onViewAllButtonClicked: () -> Unit,
) : BaseAdapter<Product, ItemProductBinding, BaseViewHolder<Product>>
    (PricelyDiffUtil.productDiffUtil) {
    inner class ProductViewHolder(mBinding: ViewBinding) :
        BaseViewHolder<Product>(mBinding) {
        override fun bind(data: Product) {
            with(binding as ItemProductBinding) {
                tvProductName.text = data.name
                tvPriceProduct.text = formatPrice(data.price)
                root.setOnClickListener {
                    onItemClicked.invoke(data)
                }
            }
        }
    }

    inner class ViewAllViewHolder(mBinding: ViewBinding) : BaseViewHolder<Product>(mBinding) {
        override fun bind(data: Product) {
            with(binding as ItemViewAllBinding) {
                btnViewAll.setOnClickListener {
                    onViewAllButtonClicked.invoke()
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1) VIEW_ALL else PRODUCT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Product> =
        if (viewType == VIEW_ALL) {
            ViewAllViewHolder(ItemViewAllBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            ProductViewHolder(ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }

    companion object {
        private const val VIEW_ALL = 111
        private const val PRODUCT = 112
    }
}

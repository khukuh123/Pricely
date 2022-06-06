package com.bangkit.pricely.presentation.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bangkit.pricely.base.BaseAdapter
import com.bangkit.pricely.base.BaseFooterAdapter
import com.bangkit.pricely.base.BaseViewHolder
import com.bangkit.pricely.databinding.ItemProductBinding
import com.bangkit.pricely.domain.product.model.Product
import com.bangkit.pricely.util.PricelyDiffUtil
import com.bangkit.pricely.util.formatPrice
import com.bangkit.pricely.util.reusable.ViewAllAdapter
import com.bangkit.pricely.util.setImage

class ProductAdapter(
    private val onItemClicked: (Product) -> Unit,
) : BaseAdapter<Product, ItemProductBinding, BaseViewHolder<Product>>
    (PricelyDiffUtil.productDiffUtil) {
    inner class ProductViewHolder(mBinding: ViewBinding) :
        BaseViewHolder<Product>(mBinding) {
        override fun bind(data: Product) {
            with(binding as ItemProductBinding) {
                tvProductName.text = data.name
                val weight = "${data.weight} ${data.unit}"
                tvUnitProduct.text = weight
                tvPriceProduct.text = formatPrice(data.price)
                imgProduct.setImage(data.imageUrl, 130, 98, pbProduct)
                root.setOnClickListener {
                    onItemClicked.invoke(data)
                }
            }
        }
    }

    private var footerAdapter: ViewAllAdapter? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Product> =
        ProductViewHolder(ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    fun <VH : RecyclerView.ViewHolder, A : BaseFooterAdapter<VH>> withFooter(adapter: A): ConcatAdapter {
        footerAdapter = adapter as ViewAllAdapter
        return ConcatAdapter(this, adapter)
    }

    fun setIsDoneLoading(isDoneLoading: Boolean){
        footerAdapter?.showFooter(isDoneLoading)
    }
}

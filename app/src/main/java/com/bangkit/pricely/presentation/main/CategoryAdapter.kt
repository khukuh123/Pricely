package com.bangkit.pricely.presentation.main

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bangkit.pricely.base.BaseAdapter
import com.bangkit.pricely.base.BaseViewHolder
import com.bangkit.pricely.databinding.ItemCategoryBinding
import com.bangkit.pricely.domain.product.model.Category
import com.bangkit.pricely.util.PricelyDiffUtil
import com.bangkit.pricely.util.dp
import com.bangkit.pricely.util.setImageFromUrl

class CategoryAdapter(private val onItemClicked: (Category) -> Unit)
    : BaseAdapter<Category, ItemCategoryBinding, CategoryAdapter.CategoryViewHolder>
    (PricelyDiffUtil.categoryDiffUtil) {
    inner class CategoryViewHolder(mBinding: ItemCategoryBinding):
        BaseViewHolder<Category>(mBinding) {
        override fun bind(data: Category) {
            with(binding as ItemCategoryBinding){
                imgIconCategory.setImageFromUrl(data.imgUrl, 48.dp)
                tvCategory.text = data.name
                root.setOnClickListener {
                    onItemClicked.invoke(data)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder =
        CategoryViewHolder(ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
}

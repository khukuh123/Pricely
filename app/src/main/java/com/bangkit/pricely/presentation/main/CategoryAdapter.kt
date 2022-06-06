package com.bangkit.pricely.presentation.main

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bangkit.pricely.base.BaseAdapter
import com.bangkit.pricely.base.BaseViewHolder
import com.bangkit.pricely.databinding.ItemCategoryBinding
import com.bangkit.pricely.domain.category.model.Category
import com.bangkit.pricely.util.PricelyDiffUtil
import com.bangkit.pricely.util.dp
import com.bangkit.pricely.util.setImageFromUrl

class CategoryAdapter(
    private val isSelectable: Boolean = true
) : BaseAdapter<Category, ItemCategoryBinding, CategoryAdapter.CategoryViewHolder>
    (PricelyDiffUtil.categoryDiffUtil) {

    private var selectedCategory: Int = 0
    private var previousCategory: Int? = null
    private var onItemClicked: ((Category, Int) -> Unit)? = null

    inner class CategoryViewHolder(mBinding: ItemCategoryBinding) :
        BaseViewHolder<Category>(mBinding) {
        override fun bind(data: Category) {
            with(binding as ItemCategoryBinding) {
                imgIconCategory.setImageFromUrl(data.imgUrl, 48.dp, pbCategory)
                tvCategory.text = data.name
                root.setOnClickListener {
                    onItemClicked?.invoke(data, bindingAdapterPosition)
                }
                if(bindingAdapterPosition == 0 && isSelectable) imgIconCategory.borderWidth = 2.dp
            }
        }

        @Suppress("UNCHECKED_CAST")
        fun bind(payloads: MutableList<Any>) {
            with(binding as ItemCategoryBinding) {
                (payloads.firstOrNull() as? Map<String, Boolean>)?.let { map ->
                    map[IS_SELECTED]?.let { isSelected ->
                        if (isSelected) imgIconCategory.borderWidth = 2.dp else imgIconCategory.borderWidth = 0.dp
                    }
                }
            }
        }
    }

    fun setOnClickedItem(listener: (Category, Int) -> Unit) {
        onItemClicked = listener
    }

    fun selectCategory(position: Int) {
        previousCategory = selectedCategory
        selectedCategory = position
        notifyItemChanged(position, mapOf(IS_SELECTED to true))
        if (previousCategory != null)
            notifyItemChanged(previousCategory!!, mapOf(IS_SELECTED to false))
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) super.onBindViewHolder(holder, position, payloads) else holder.bind(payloads)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder =
        CategoryViewHolder(ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    companion object{
        private const val IS_SELECTED = "is_selected"
    }
}

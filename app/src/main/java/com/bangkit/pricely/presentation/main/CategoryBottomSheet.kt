package com.bangkit.pricely.presentation.main

import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.bangkit.pricely.base.BaseBottomSheetDialog
import com.bangkit.pricely.databinding.FragmentCategoryListBinding
import com.bangkit.pricely.domain.category.model.Category
import com.bangkit.pricely.util.BundleKeys
import com.bangkit.pricely.util.dp
import com.bangkit.pricely.util.recyclerview.PricelyGridLayoutItemDecoration

class CategoryBottomSheet private constructor(
    private val onItemClicked: (Category) -> Unit,
) : BaseBottomSheetDialog<FragmentCategoryListBinding>() {

    private lateinit var categories: ArrayList<Category>
    private lateinit var categoryAdapter: CategoryAdapter

    override val TAG: String = CategoryBottomSheet::class.java.simpleName

    override fun getViewBinding(container: ViewGroup?): FragmentCategoryListBinding =
        FragmentCategoryListBinding.inflate(layoutInflater, container, false)

    override fun initIntent() {
        categories = arguments?.getParcelableArrayList<Category>(BundleKeys.CATEGORY) as ArrayList<Category>
    }

    override fun setupUI() {
        with(binding) {
            categoryAdapter = CategoryAdapter(isSelectable = false)

            rvCategoryList.apply {
                adapter = categoryAdapter
                layoutManager = GridLayoutManager(requireContext(), 4)
                addItemDecoration(PricelyGridLayoutItemDecoration(4, 8.dp))
                categoryAdapter.submitList(categories)
            }
        }
    }

    override fun setupAction() {
        categoryAdapter.setOnClickedItem { category, _ ->
            onItemClicked.invoke(category)
        }
    }

    companion object {
        fun newInstance(data: ArrayList<Category>, onItemClicked: (Category) -> Unit): CategoryBottomSheet =
            CategoryBottomSheet(onItemClicked).apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(BundleKeys.CATEGORY, data)
                }
            }
    }
}
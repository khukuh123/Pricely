package com.bangkit.pricely.presentation.main

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.bangkit.pricely.base.BaseBottomSheetDialog
import com.bangkit.pricely.databinding.FragmentCategoryListBinding
import com.bangkit.pricely.domain.product.model.Category
import com.bangkit.pricely.util.BundleKeys
import com.bangkit.pricely.util.dp
import com.bangkit.pricely.util.recyclerview.PricelyGridLayoutItemDecoration
import com.google.android.material.bottomsheet.BottomSheetDialog

class CategoryBottomSheet private constructor(
    private val onItemClicked: (Category) -> Unit,
) : BaseBottomSheetDialog<FragmentCategoryListBinding>() {

    private lateinit var categories: ArrayList<Category>
    private val categoryAdapter by lazy {
        CategoryAdapter {
            onItemClicked.invoke(it)
        }
    }

    override val TAG: String = CategoryBottomSheet::class.java.simpleName

    override fun getViewBinding(container: ViewGroup?): FragmentCategoryListBinding =
        FragmentCategoryListBinding.inflate(layoutInflater, container, false)

    override fun initIntent() {
        categories = arguments?.getParcelableArrayList<Category>(BundleKeys.CATEGORIES) as ArrayList<Category>
    }

    override fun setupAction() {

    }

    override fun setupUI() {
        with(binding) {
            rvCategoryList.apply {
                adapter = categoryAdapter
                layoutManager = GridLayoutManager(requireContext(), 4)
                addItemDecoration(PricelyGridLayoutItemDecoration(4, 8.dp))
                categoryAdapter.submitList(categories)
            }
        }
    }

    companion object {
        fun newInstance(data: ArrayList<Category>, onItemClicked: (Category) -> Unit): CategoryBottomSheet =
            CategoryBottomSheet(onItemClicked).apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(BundleKeys.CATEGORIES, data)
                }
            }
    }
}
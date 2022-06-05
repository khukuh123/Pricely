package com.bangkit.pricely.presentation.main

import android.content.Context
import android.content.Intent
import android.view.MenuItem
import androidx.recyclerview.widget.GridLayoutManager
import com.bangkit.pricely.base.BaseActivity
import com.bangkit.pricely.databinding.ActivityCategoryDetailBinding
import com.bangkit.pricely.domain.category.model.Category
import com.bangkit.pricely.presentation.detail.ProductDetailActivity
import com.bangkit.pricely.presentation.viewmodel.CategoryViewModel
import com.bangkit.pricely.presentation.viewmodel.ProductViewModel
import com.bangkit.pricely.util.BundleKeys
import com.bangkit.pricely.util.dialog.getErrorDialog
import com.bangkit.pricely.util.dialog.getLoadingDialog
import com.bangkit.pricely.util.dp
import com.bangkit.pricely.util.observe
import com.bangkit.pricely.util.recyclerview.PricelyGridLayoutItemDecoration
import com.bangkit.pricely.util.setupToolbar
import org.koin.android.ext.android.inject

class CategoryDetailActivity :  BaseActivity<ActivityCategoryDetailBinding>() {

    private lateinit var category: Category
    private val productViewModel: ProductViewModel by inject()
    private val categoryViewModel: CategoryViewModel by inject()

    private val productAdapter by lazy {
        ProductVerticalAdapter {
            ProductDetailActivity.start(this, it.id)
        }
    }

    override fun getViewBinding(): ActivityCategoryDetailBinding =
        ActivityCategoryDetailBinding.inflate(layoutInflater)

    override fun setupIntent() {
        category = intent.getParcelableExtra<Category>(BundleKeys.CATEGORY) as Category
    }

    override fun setupUI() {
        setLoadingDialog(getLoadingDialog(this))
        setErrorDialog(getErrorDialog(this))
        setupToolbar(
            binding.toolbar.toolbar,
            category.name,
            true
        )
        setupRecyclerView()
    }

    override fun setupAction() {

    }

    override fun setupProcess() {
        if(category.type > 0) getCategoryDetail() else binding.tvCategoryDescription.text = category.description
        getProductsByCategory()
        getProductsRecommendationByCategory()
    }

    override fun setupObserver() {
        categoryViewModel.categoryDetail.observe(this,
            onLoading = {
                showLoading()
            },
            onError = {
                dismissLoading()
                showErrorDialog(it) { getCategoryDetail() }
            },
            onSuccess = {
                dismissLoading()
                binding.tvCategoryDescription.text = it.description
            }
        )
        if(category.type > 0){
            productViewModel.productsByCategory.observe(this,
                onLoading = {
                    showLoading()
                },
                onError = {
                    dismissLoading()
                    showErrorDialog(it) { getProductsByCategory() }
                },
                onSuccess = {
                    dismissLoading()
                    productAdapter.submitList(it)
                }
            )
            productViewModel.productsRecommendationByCategory.observe(this,
                onLoading = {
                    showLoading()
                },
                onError = {
                    dismissLoading()
                    showErrorDialog(it) { getProductsRecommendationByCategory() }
                },
                onSuccess = {
                    binding.viewRecommendationSection.setProducts(it.take(3))
                }
            )
        }else{
            productViewModel.products.observe(this,
                onLoading = {
                    showLoading()
                },
                onError = {
                    dismissLoading()
                    showErrorDialog(it) { getProductsByCategory() }
                },
                onSuccess = {
                    dismissLoading()
                    productAdapter.submitList(it)
                }
            )
            productViewModel.productsRecommendation.observe(this,
                onLoading = {
                    showLoading()
                },
                onError = {
                    dismissLoading()
                    showErrorDialog(it) { getProductsRecommendationByCategory() }
                },
                onSuccess = {
                    binding.viewRecommendationSection.setProducts(it.take(3))
                }
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    private fun getCategoryDetail() {
        categoryViewModel.getCategoryDetail(category.id)
    }

    private fun getProductsByCategory() {
        if(category.type > 0)
            productViewModel.getProductsByCategory(category.id)
        else
            productViewModel.getProducts()
    }

    private fun getProductsRecommendationByCategory() {
        if(category.type > 0)
            productViewModel.getProductsRecommendationByCategory(category.id, true)
        else
            productViewModel.getProductsRecommendation(true)
    }

    private fun setupRecyclerView() {
        binding.rvVerticalProdcuts.apply {
            adapter = productAdapter
            layoutManager = GridLayoutManager(this@CategoryDetailActivity, 2)
            addItemDecoration(PricelyGridLayoutItemDecoration(2, 16.dp, edge = 16.dp))
        }
    }

    companion object {

        @JvmStatic
        fun start(context: Context, category: Category) =
            context.startActivity(Intent(context, CategoryDetailActivity::class.java)
                .putExtra(BundleKeys.CATEGORY, category))
    }
}
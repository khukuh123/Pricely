package com.bangkit.pricely.presentation.main

import android.content.Context
import android.content.Intent
import android.view.MenuItem
import androidx.recyclerview.widget.GridLayoutManager
import com.bangkit.pricely.R
import com.bangkit.pricely.base.BaseActivity
import com.bangkit.pricely.databinding.ActivityCategoryDetailBinding
import com.bangkit.pricely.domain.category.model.Category
import com.bangkit.pricely.presentation.detail.ProductDetailActivity
import com.bangkit.pricely.presentation.viewmodel.CategoryViewModel
import com.bangkit.pricely.presentation.viewmodel.ProductViewModel
import com.bangkit.pricely.util.*
import com.bangkit.pricely.util.dialog.getErrorDialog
import com.bangkit.pricely.util.dialog.getLoadingDialog
import com.bangkit.pricely.util.recyclerview.PricelyGridLayoutItemDecoration
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import org.koin.android.ext.android.inject

class CategoryDetailActivity :  BaseActivity<ActivityCategoryDetailBinding>() {

    private lateinit var category: Category
    private val productViewModel: ProductViewModel by inject()
    private val categoryViewModel: CategoryViewModel by inject()
    private val remoteConfig: FirebaseRemoteConfig by inject()

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
            binding.toolbarContainer.toolbar,
            category.name,
            true
        )
        setupRecyclerView()
        if(category.type == CategoryType.RECOMMENDATION.type) binding.viewRecommendationSection.gone()
    }

    override fun setupAction() {
        binding.viewRecommendationSection.setOnViewAllButtonClicked {
            val description = remoteConfig.getString(RemoteConfigKey.RECOMMENDATION_DESCRIPTION)
            start(this, category.copy(name = getString(R.string.label_recommendation), description = description, type = -1))
        }
    }

    override fun setupProcess() {
        getCategoryDetail()
        getProductsByCategory()
        getProductsRecommendationByCategory()
    }

    override fun setupObserver() {
        with(binding){
            categoryViewModel.categoryDetail.observe(this@CategoryDetailActivity,
                onLoading = {
                    if(category.type != CategoryType.RECOMMENDATION.type && category.type != CategoryType.ALL_PRODUCT.type) showLoading()
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
            productViewModel.productsByCategory.observe(this@CategoryDetailActivity,
                onLoading = {
                    msvCategoryDetail.showLoading()
                },
                onError = {
                    msvCategoryDetail.showError(message = it, onRetry = ::getProductsByCategory)
                },
                onSuccess = {
                    binding.msvCategoryDetail.showContent()
                    productAdapter.submitList(it)
                }
            )
            productViewModel.productsRecommendationByCategory.observe(this@CategoryDetailActivity,
                onLoading = {
                    viewRecommendationSection.showLoading()
                },
                onError = {
                    viewRecommendationSection.showError(message = it, onRetry = ::getProductsRecommendationByCategory)
                },
                onSuccess = {
                    viewRecommendationSection.setProducts(it.take(3))
                }
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    private fun getCategoryDetail() {
        when(category.type){
            CategoryType.NORMAL.type -> {
                categoryViewModel.getCategoryDetail(category.id)
            }
            else -> {
                dismissLoading()
                binding.tvCategoryDescription.text = category.description
            }
        }
    }

    private fun getProductsByCategory() {
        when(category.type){
            CategoryType.NORMAL.type -> {
                productViewModel.getProductsByCategory(category.id)
            }
            else -> {
                productViewModel.getProducts()
            }
        }
    }

    private fun getProductsRecommendationByCategory() {
        when(category.type){
            CategoryType.NORMAL.type -> {
                productViewModel.getProductsRecommendationByCategory(category.id, true)
            }
            CategoryType.ALL_PRODUCT.type -> {
                productViewModel.getProductsRecommendation(true)
            }
            else -> { } // Recommendation detail will not show the recommendation again
        }
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
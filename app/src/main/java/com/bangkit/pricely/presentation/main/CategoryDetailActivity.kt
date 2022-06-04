package com.bangkit.pricely.presentation.main

import android.content.Context
import android.content.Intent
import android.view.MenuItem
import androidx.recyclerview.widget.GridLayoutManager
import com.bangkit.pricely.R
import com.bangkit.pricely.base.BaseActivity
import com.bangkit.pricely.databinding.ActivityCategoryDetailBinding
import com.bangkit.pricely.domain.product.model.Category
import com.bangkit.pricely.domain.product.model.Product
import com.bangkit.pricely.presentation.detail.ProductDetailActivity
import com.bangkit.pricely.presentation.viewmodel.ProductViewModel
import com.bangkit.pricely.util.*
import com.bangkit.pricely.util.recyclerview.PricelyGridLayoutItemDecoration
import org.koin.android.ext.android.inject

class CategoryDetailActivity :  BaseActivity<ActivityCategoryDetailBinding>() {

    private lateinit var category: Category
    private var isViewAllIntent: Boolean = false
    private var categoryId: Int = 0

    private val productViewModel: ProductViewModel by inject()

    private val productAdapter by lazy {
        ProductVerticalAdapter {
            ProductDetailActivity.start(this, it.id)
        }
    }

    private val recommendationByCategoryAdapter by lazy {
        ProductVerticalAdapter {
            ProductDetailActivity.start(this, it.id)
        }
    }

    override fun getViewBinding(): ActivityCategoryDetailBinding =
        ActivityCategoryDetailBinding.inflate(layoutInflater)

    override fun setupIntent() {
        category = intent.getParcelableExtra(BundleKeys.CATEGORIES)!!

        // TODO: Change categoryId value to category.id and delete when(category.name) blocks
        when(category.name) {
            getString(R.string.label_fruit) -> categoryId = 1
            getString(R.string.label_seasoning) -> categoryId = 2
            getString(R.string.label_meat) -> categoryId = 3
            getString(R.string.label_food) -> categoryId = 4
            getString(R.string.label_drink) -> categoryId = 5
            getString(R.string.label_vegetable) -> categoryId = 6
            getString(R.string.label_seafood) -> categoryId = 7
            getString(R.string.label_grocery) -> categoryId = 8
        }
    }

    override fun setupUI() {
        val title = category.name.ifEmpty { "Vegetables" }
        setupToolbar(
            binding.toolbar.toolbar,
            title,
            true
        )
        val desc = category.description.ifEmpty { getString(R.string.samples_description) }
        binding.tvCategoryDescription.text = desc
        val recommendation = getString(R.string.label_recommendation)
        val sectionTitle = "$recommendation ${category.name}"
        binding.tvSectionTitle.text = sectionTitle
        setupRecyclerView()
    }

    override fun setupAction() {

    }

    override fun setupProcess() {
        when(category.name) {
            RECOMMENDATION -> {
                isViewAllIntent = true
                getRecommendation()
                hideRecommendationByCategory()
            }
            ALL_PRODUCT -> {
                isViewAllIntent = true
                getAllProducts()
                hideRecommendationByCategory()
            }
            else -> {
                setCategory()
                getRecommendationByCategoryId(categoryId)
            }
        }

    }

    override fun setupObserver() {
        if (isViewAllIntent) {
            productViewModel.listRecommendation.observe(this,
                onLoading = {
                    showLoading()
                },
                onError = {
                    dismissLoading()
                    showErrorDialog(it, ::getRecommendation)
                },
                onSuccess = {
                    dismissLoading()
                    setRecommendation(it)
                }
            )

            productViewModel.listAllProduct.observe(this,
                onLoading = {
                    showLoading()
                },
                onError = {
                    dismissLoading()
                    showErrorDialog(it, ::getAllProducts)
                },
                onSuccess = {
                    dismissLoading()
                    setAllProduct(it)
                }
            )
        } else {
            productViewModel.listRecommendation.observe(this,
                onLoading = {
                    showLoading()
                },
                onError = {
                    dismissLoading()
                    showErrorDialog(it, ::getRecommendation)
                },
                onSuccess = {
                    dismissLoading()
                    setRecommendationByCategory(it)
                }
            )
        }
    }

    private fun getRecommendationByCategoryId(categoryId: Int) {
        productViewModel.getListRecommendationByCategory(categoryId, true)
    }

    private fun hideRecommendationByCategory() {
        with(binding) {
            tvSectionTitle.gone()
            sectionRecommendation.gone()
            rvVerticalRecommendation.gone()
        }
    }

    private fun getAllProducts() {
        productViewModel.getListAllProduct()
    }

    private fun setAllProduct(list: List<Product>) {
        productAdapter.submitList(list)
    }

    private fun getRecommendation() {
        productViewModel.getListRecommendation(true)
    }

    private fun setRecommendation(list: List<Product>) {
        productAdapter.submitList(list)
    }

    private fun setRecommendationByCategory(list: List<Product>) {
        recommendationByCategoryAdapter.submitList(null)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    private fun setCategory() {
        productAdapter.submitList(getDummyAllProducts())
    }

    private fun setupRecyclerView(){
        binding.rvVerticalProdcuts.apply {
            adapter =  productAdapter
            layoutManager = GridLayoutManager(this@CategoryDetailActivity, 2)
            addItemDecoration(PricelyGridLayoutItemDecoration(2, 16.dp, edge = 16.dp))
        }
        binding.rvVerticalRecommendation.apply {
            adapter =  recommendationByCategoryAdapter
            layoutManager = GridLayoutManager(this@CategoryDetailActivity, 2)
            addItemDecoration(PricelyGridLayoutItemDecoration(2, 16.dp, edge = 16.dp))
        }
    }

    private fun getDummyAllProducts() =
        Array(8){
            val rand = (300..500).random()
            val price = "${rand}00".toInt()
            Product(
                id = it,
                name = "Product #$it",
                price = price,
                unit = "gram / pack"
            )
        }.toList()

    companion object {
        const val RECOMMENDATION = "Recommendation"
        const val ALL_PRODUCT = "All Product"

        @JvmStatic
        fun start(context: Context, category: Category) =
            context.startActivity(Intent(context, CategoryDetailActivity::class.java)
                .putExtra(BundleKeys.CATEGORIES, category))
    }
}
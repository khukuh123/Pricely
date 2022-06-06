package com.bangkit.pricely.presentation.main

import android.content.Intent
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.GridLayoutManager
import com.bangkit.pricely.R
import com.bangkit.pricely.base.BaseActivity
import com.bangkit.pricely.databinding.ActivityMainBinding
import com.bangkit.pricely.domain.category.model.Category
import com.bangkit.pricely.domain.product.model.Product
import com.bangkit.pricely.presentation.viewmodel.CategoryViewModel
import com.bangkit.pricely.presentation.viewmodel.ProductViewModel
import com.bangkit.pricely.util.dialog.getErrorDialog
import com.bangkit.pricely.util.dialog.getLoadingDialog
import com.bangkit.pricely.util.dp
import com.bangkit.pricely.util.observe
import com.bangkit.pricely.util.recyclerview.PricelyGridLayoutItemDecoration
import com.bangkit.pricely.util.setupToolbar
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val productViewModel: ProductViewModel by inject()
    private val categoryViewModel: CategoryViewModel by inject()

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var selectedCategory: Category
    private val categoryList: MutableList<Category> = mutableListOf()

    override fun getViewBinding(): ActivityMainBinding {
        installSplashScreen()
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun setupIntent() {
        selectedCategory = getAllProductCategory()
    }

    override fun setupUI() {
        setupToolbar(
            binding.toolbar.toolbar,
            "",
            false
        )
        setLoadingDialog(getLoadingDialog(this))
        setErrorDialog(getErrorDialog(this))
        setupRecyclerView()
    }

    override fun setupAction() {
        with(binding) {
            viewProductSection.setOnViewAllButtonClicked {
                CategoryDetailActivity.start(this@MainActivity, selectedCategory)
            }
            viewRecommendationSection.setOnViewAllButtonClicked {
                CategoryDetailActivity.start(this@MainActivity, selectedCategory)
            }

            categoryAdapter.setOnClickedItem { category, position ->
                when(category.type) {
                    0 -> {
                        categoryAdapter.selectCategory(position)
                        selectedCategory = category
                        getProductsByCategory()
                        getProductsRecommendationByCategory()
                    }
                    1 -> {
                        categoryAdapter.selectCategory(position)
                        selectedCategory = category
                        getProductsByCategory()
                        getProductsRecommendationByCategory()
                    }
                    2 -> {
                        CategoryBottomSheet.newInstance(ArrayList(categoryList)) { newCategory ->
                            CategoryDetailActivity.start(this@MainActivity, newCategory)
                        }.showDialog(supportFragmentManager)
                    }
                }
            }
        }
    }

    override fun setupProcess() {
        getCategoryList()
        getProductsRecommendationByCategory()
        getProductsByCategory()
    }

    override fun setupObserver() {
        categoryViewModel.categoryList.observe(this,
            onLoading = {
                showLoading()
            },
            onError = {
                dismissLoading()
                showErrorDialog(it, ::getCategoryList)
            },

            onSuccess = {
                dismissLoading()
                setCategories(it)
            }
        )
        productViewModel.productsByCategory.observe(this,
            onLoading = {
                showLoading()
                binding.viewProductSection.setProducts(null)
            },
            onError = {
                dismissLoading()
                showErrorDialog(it, ::getProductsByCategory)
            },
            onSuccess = {
                dismissLoading()
                setProductsByCategory(it.take(3))
            }
        )
        productViewModel.productsRecommendationByCategory.observe(this,
            onLoading = {
                showLoading()
                binding.viewProductSection.setProducts(null)
            },
            onError = {
                dismissLoading()
                showErrorDialog(it, ::getProductsRecommendationByCategory)
            },
            onSuccess = {
                dismissLoading()
                setProductsRecommendation(it.take(3))
            }
        )
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_search -> SearchActivity.start(this)
            R.id.menu_language -> startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerView() {
        with(binding) {
            categoryAdapter = CategoryAdapter()

            rvCategory.apply {
                adapter = categoryAdapter
                layoutManager = GridLayoutManager(this@MainActivity, 4)
                addItemDecoration(PricelyGridLayoutItemDecoration(4, 8.dp))
            }
        }
    }

    private fun getCategoryList() {
        categoryViewModel.getCategoryList()
    }

    private fun getProductsByCategory() {
        if (selectedCategory.type > 0)
            productViewModel.getProductsByCategory(selectedCategory.id)
        else
            productViewModel.getProducts()
    }

    private fun getProductsRecommendationByCategory() {
        if (selectedCategory.type > 0)
            productViewModel.getProductsRecommendationByCategory(selectedCategory.id, true)
        else
            productViewModel.getProductsRecommendation(true)
    }

    private fun setCategories(list: List<Category>) {
        categoryList.addAll(list)
        val newList = categoryList.take(6).toMutableList()
        newList.add(0, getAllProductCategory())
        newList.add(getOthersCategory())
        categoryAdapter.submitList(newList)
    }

    private fun setProductsByCategory(list: List<Product>) {
        binding.viewProductSection.setProducts(list)
    }

    private fun setProductsRecommendation(list: List<Product>) {
        binding.viewRecommendationSection.setProducts(list)
    }

    private fun getAllProductCategory(): Category =
        Category(
            id = 0,
            name = resources.getString(R.string.label_all_product),
            description = getString(R.string.label_all_products_description),
            type = 0,
            imgUrl = "https://cdn-icons-png.flaticon.com/512/291/291893.png"
        )

    private fun getOthersCategory(): Category =
        Category(
            2,
            resources.getString(R.string.label_other),
            "",
            2,
            "https://cdn-icons-png.flaticon.com/512/291/291893.png"
        )
}
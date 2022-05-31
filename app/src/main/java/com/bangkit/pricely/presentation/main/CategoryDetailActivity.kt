package com.bangkit.pricely.presentation.main

import android.content.Context
import android.content.Intent
import android.view.MenuItem
import androidx.recyclerview.widget.GridLayoutManager
import com.bangkit.pricely.R
import com.bangkit.pricely.base.BaseActivity
import com.bangkit.pricely.databinding.ActivityCategoryDetailBinding
import com.bangkit.pricely.domain.product.model.Product
import com.bangkit.pricely.presentation.detail.ProductDetailActivity
import com.bangkit.pricely.util.dp
import com.bangkit.pricely.util.recyclerview.PricelyGridLayoutItemDecoration
import com.bangkit.pricely.util.setupToolbar

class CategoryDetailActivity :  BaseActivity<ActivityCategoryDetailBinding>() {

    private val productAdapter by lazy {
        ProductVerticalAdapter {
            ProductDetailActivity.start(this, it.id)
        }
    }

    override fun getViewBinding(): ActivityCategoryDetailBinding =
        ActivityCategoryDetailBinding.inflate(layoutInflater)

    override fun setupIntent() {
        
    }

    override fun setupUI() {
        setupToolbar(
            binding.toolbar.toolbar,
            "Vegetables",
            true
        )
        setupRecyclerView()
    }

    override fun setupAction() {

    }

    override fun setupProcess() {
        setCategory()
    }

    override fun setupObserver() {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    private fun setCategory() {
        binding.tvCategoryDescription.text = getString(R.string.samples_description)
        productAdapter.submitList(getDummyAllProducts())
    }

    private fun setupRecyclerView(){
        binding.rvVerticalProdcuts.apply {
            adapter =  productAdapter
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

    companion object{
        @JvmStatic
        fun start(context: Context) =
            context.startActivity(Intent(context, CategoryDetailActivity::class.java))
    }
}
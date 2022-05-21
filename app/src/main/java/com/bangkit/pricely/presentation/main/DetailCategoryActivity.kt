package com.bangkit.pricely.presentation.main

import androidx.recyclerview.widget.GridLayoutManager
import com.bangkit.pricely.base.BaseActivity
import com.bangkit.pricely.databinding.ActivityDetailCategoryBinding
import com.bangkit.pricely.domain.product.model.Product
import com.bangkit.pricely.util.showToast

class DetailCategoryActivity :  BaseActivity<ActivityDetailCategoryBinding>() {

    private val allProductAdapter by lazy {
        AllProductAdapter(
            onItemClicked = { product ->
                showToast("Terpilih: ${product.name} dengan id ${product.id}")
                // TODO: Intent with Object to ProductDetailActivity
            }
        )
    }

    override fun getViewBinding(): ActivityDetailCategoryBinding =
        ActivityDetailCategoryBinding.inflate(layoutInflater)

    override fun setupIntent() {
        
    }

    override fun setupUI() {
        setupRecyclerView()
        supportActionBar?.hide()
    }

    private fun setupRecyclerView(){
        binding.rvItem.apply {
            adapter =  allProductAdapter
            layoutManager = GridLayoutManager(this@DetailCategoryActivity, 2)
        }
    }

    override fun setupAction() {

    }

    override fun setupProcess() {
        allProductAdapter.submitList(getDummyAllProducts())
    }

    override fun setupObserver() {
        
    }

    private fun getDummyAllProducts() =
        Array(5){
            val rand = (300..500).random()
            val price = "${rand}00".toInt()
            Product(
                id = it.toString(),
                "Product #$it",
                price,
                "500 gram / pack"
            )
        }.toList()

}
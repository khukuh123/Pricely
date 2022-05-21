package com.bangkit.pricely.presentation.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.bangkit.pricely.R
import com.bangkit.pricely.base.BaseActivity
import com.bangkit.pricely.base.BaseAdapter
import com.bangkit.pricely.databinding.ActivitySearchScreenBinding
import com.bangkit.pricely.databinding.ItemCardBinding
import com.bangkit.pricely.domain.product.model.Product
import com.bangkit.pricely.util.showToast

class SearchScreenActivity :  BaseActivity<ActivitySearchScreenBinding>() {

    private lateinit var text: String

    private val searchProductAdapter by lazy{
        SearchProductAdapter(
            onItemClicked = { product ->
                showToast("Terpilih: ${product.name} dengan id ${product.id}")
            }
        )
    }

    override fun getViewBinding(): ActivitySearchScreenBinding =
        ActivitySearchScreenBinding.inflate(layoutInflater)

    override fun setupIntent() {

    }

    override fun setupUI() {
        setupRecyclerView()
        supportActionBar?.hide()
    }

    override fun setupAction() {
        binding.toolbarSearch.btnSearch.setOnClickListener {
            text = binding.toolbarSearch.etSearch.text.toString()
            searchItem(text)


        }
        binding.toolbarSearch.etSearch.setOnKeyListener { _, i, keyEvent ->
            if(keyEvent.action == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER){
                text = binding.toolbarSearch.etSearch.text.toString()
                searchItem(text)
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }
    fun searchItem(text: String){
        val list = getDummyData()
        for(i in list.indices){
            if(text.equals(list[i].name, ignoreCase = true)){
                showEmpty(false)
                searchProductAdapter.submitList(getDummyData())
            }else{
                showEmpty(true)
            }
        }
    }

    fun showEmpty(action: Boolean){
        if(action){
            binding.layoutEmptySearch.imgEmpty.visibility = View.VISIBLE
            binding.layoutEmptySearch.tvEmptyMessage.visibility = View.VISIBLE
            binding.layoutEmptySearch.tvSubEmptyMessage.visibility = View.VISIBLE
        }else{
            binding.layoutEmptySearch.imgEmpty.visibility = View.GONE
            binding.layoutEmptySearch.tvEmptyMessage.visibility = View.GONE
            binding.layoutEmptySearch.tvSubEmptyMessage.visibility = View.GONE
        }
    }

    override fun setupProcess() {

    }

    override fun setupObserver() {

    }

    private fun setupRecyclerView(){
        binding.rvItem.apply {
            adapter = searchProductAdapter
            layoutManager = GridLayoutManager(this@SearchScreenActivity, 2)
        }
    }

    private fun getDummyData(): List<Product>{
        return listOf(
            Product("0", "Tomat", 3000, "500 gram / pack"),
            Product("1", "Tomat", 3000, "500 gram / pack")
        )
    }

}
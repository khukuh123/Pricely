package com.bangkit.pricely.presentation.main

import android.content.Context
import android.content.Intent
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.pricely.R
import com.bangkit.pricely.base.BaseActivity
import com.bangkit.pricely.databinding.ActivitySearchScreenBinding
import com.bangkit.pricely.domain.category.model.Category
import com.bangkit.pricely.domain.product.model.Product
import com.bangkit.pricely.presentation.detail.ProductDetailActivity
import com.bangkit.pricely.presentation.viewmodel.ProductViewModel
import com.bangkit.pricely.util.*
import com.bangkit.pricely.util.recyclerview.PricelyGridLayoutItemDecoration
import com.bangkit.pricely.util.recyclerview.PricelyLinearLayoutItemDecoration
import org.koin.android.ext.android.inject

class SearchActivity : BaseActivity<ActivitySearchScreenBinding>() {

    private val productViewModel: ProductViewModel by inject()

    private val searchProductAdapter by lazy {
        ProductVerticalAdapter {
            ProductDetailActivity.start(this, it.id)
        }
    }
    private val suggestionAdapter by lazy {
        SuggestionAdapter {
            with(binding) {
                toolbar.tilSearch.editText?.setText(it)
                searchProduct(it)
            }
        }
    }

    override fun getViewBinding(): ActivitySearchScreenBinding =
        ActivitySearchScreenBinding.inflate(layoutInflater)

    override fun setupIntent() {

    }

    override fun setupUI() {
        setupRecyclerView()
        setupToolbar(
            binding.toolbar.toolbar,
            "",
            true
        )
        setupSearch()
    }

    override fun setupAction() {
        with(binding) {
            viewRecommendationSection.setOnViewAllButtonClicked {
                val recommendationCategory = Category(1, getString(R.string.label_description_recommendation),
                    "",1, "")
                CategoryDetailActivity.start(this@SearchActivity, recommendationCategory)
            }
        }
    }

    override fun setupProcess() {
        getRecommendation()
        getSuggestions()
    }

    override fun setupObserver() {
        with(binding){
            productViewModel.productsRecommendationByCategory.observe(this@SearchActivity,
                onLoading = {
                    msvSearch.showLoading()
                },
                onError = {
                    msvSearch.showError(message = it, onRetry = ::getRecommendation)
                },
                onSuccess = {
                    msvSearch.showContent()
                    if (it.size > 3) setRecommendation(it.take(3)) else setRecommendation(it)
                }
            )
            productViewModel.suggestions.observe(this@SearchActivity,
                onLoading = {
                    // TODO: add msv
                },
                onError = {
                    // TODO: add msv
                },
                onSuccess = {
                    suggestionAdapter.submitList(it.map { suggestion -> suggestion.name })
                }
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    private fun getRecommendation() {
        productViewModel.getProductsRecommendation(true)
    }

    private fun getSuggestions() {
        productViewModel.getSuggestions()
    }

    private fun setRecommendation(list: List<Product>) {
        binding.viewRecommendationSection.setProducts(list)
    }


    private fun setupRecyclerView() {
        with(binding) {
            rvVerticalProducts.apply {
                adapter = searchProductAdapter
                layoutManager = GridLayoutManager(this@SearchActivity, 2)
                addItemDecoration(PricelyGridLayoutItemDecoration(2, 16.dp, 16.dp))
            }
            rvSuggestion.apply {
                adapter = suggestionAdapter
                layoutManager = LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
                addItemDecoration(PricelyLinearLayoutItemDecoration(8.dp, edge = 16.dp))
            }
        }
    }

    private fun getDummyData(): List<Product> {
        return listOf(
            Product(0, name = "Tomat", price= 3000, unit = "gram / pack"),
            Product(1, name = "Tomat", price= 3000, unit = "gram / pack"),
            Product(2, name = "Tomat", price= 3000, unit = "gram / pack"),
            Product(3, name = "Tomat", price= 3000, unit = "gram / pack"),
        )
    }

    private fun TextView.setPatternSpan(text: String, textPattern: String){
        val buffer = StringBuffer()
        val spannable = SpannableStringBuilder()
        val pattern = textPattern.toPattern()
        val matcher = pattern.matcher(text)
        while(matcher.find()){
            buffer.setLength(0)
            val group = matcher.group()
            val spanText = group.substring(2 , group.length - 2)
            matcher.appendReplacement(buffer, spanText)
            spannable.append(buffer)
            val start = spannable.length - spanText.length
            spannable.setSpan(ForegroundColorSpan(ResourcesCompat.getColor(resources, R.color.greenLeaf, null)),
                start,
                spannable.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        buffer.setLength(0)
        matcher.appendTail(buffer)
        spannable.append(buffer)

        this.text = spannable
    }


    private fun searchProduct(text: String) {
        with(binding) {
            searchProductAdapter.submitList(getDummyData())
            when (text) {
                "empty" -> {
                    groupSearchResult.visible()
                    groupSuggestion.gone()
                    msvSearch.showEmptyList(getString(R.string.empty_search), getString(R.string.message_search))
                    tvSearchResult.setPatternSpan(getString(R.string.label_search_result, 0, text), "\\_{2}.*?\\_{2}")
                    viewRecommendationSection.visible()
                }
                else -> {
                    groupSearchResult.visible()
                    groupSuggestion.gone()
                    msvSearch.showContent()
                    tvSearchResult.setPatternSpan(getString(R.string.label_search_result, getDummyData().size, text), "\\_{2}.*?\\_{2}")
                    viewRecommendationSection.gone()
                }
            }
        }
    }

    private fun showSuggestions() {
        with(binding) {
            groupSearchResult.gone()
            groupSuggestion.visible()
            viewRecommendationSection.visible()
        }
    }

    private fun setupSearch() {
        with(binding) {
            toolbar.tilSearch.apply {
                editText?.let { editText ->
                    setEndIconOnClickListener {
                        editText.setText("")
                    }
                    editText.addTextChangedListener { editable ->
                        if (editable?.isEmpty() == true) showSuggestions()
                    }
                    editText.setOnEditorActionListener { _, actionId, _ ->
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            searchProduct(editText.text.toString())
                            hideSoftInput(editText)
                            return@setOnEditorActionListener true
                        }
                        return@setOnEditorActionListener false
                    }
                }
                requestFocus()
            }
        }
    }

    companion object {
        @JvmStatic
        fun start(context: Context) =
            context.startActivity(Intent(context, SearchActivity::class.java))
    }
}

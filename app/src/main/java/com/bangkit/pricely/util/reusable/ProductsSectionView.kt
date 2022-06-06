package com.bangkit.pricely.util.reusable

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.pricely.R
import com.bangkit.pricely.databinding.LayoutProductsSectionBinding
import com.bangkit.pricely.domain.product.model.Product
import com.bangkit.pricely.presentation.detail.ProductDetailActivity
import com.bangkit.pricely.presentation.main.ProductAdapter
import com.bangkit.pricely.util.dp
import com.bangkit.pricely.util.recyclerview.PricelyLinearLayoutItemDecoration
import com.bangkit.pricely.util.showContent
import com.bangkit.pricely.util.showError
import com.bangkit.pricely.util.showLoading

class ProductsSectionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var _binding: LayoutProductsSectionBinding? = null
    private val binding
        get() = _binding!!
    private val productAdapter: ProductAdapter by lazy {
        ProductAdapter(
            onItemClicked = {
                ProductDetailActivity.start(context, it.id)
            }
        )
    }

    private var sectionTitle = ""
    private var products: MutableList<Product> = mutableListOf()
    private var onViewAllButtonClicked: (() -> Unit)? = null

    init {
        setAttributes(attrs)
        setupView()
    }

    private fun setAttributes(attrs: AttributeSet?) {
        _binding = LayoutProductsSectionBinding.inflate(LayoutInflater.from(context), this)

        val attributes = context.theme.obtainStyledAttributes(attrs, R.styleable.ProductsSectionView, 0, 0)
        sectionTitle = attributes.getString(R.styleable.ProductsSectionView_title) ?: ""
    }

    private fun setupView() {
        setupUI()
        setupAction()
    }

    private fun setupUI() {
        with(binding) {
            tvSectionTitle.text = sectionTitle

            rvSectionProducts.apply {
                adapter = productAdapter.withFooter(ViewAllAdapter {
                    onViewAllButtonClicked?.invoke()
                })
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                addItemDecoration(PricelyLinearLayoutItemDecoration(16.dp, orientation = LinearLayoutManager.HORIZONTAL, edge = 24.dp))
                itemAnimator = null
            }
        }
    }

    private fun setupAction() {
        binding.btnViewAllProduct.setOnClickListener {
            onViewAllButtonClicked?.invoke()
        }
    }

    fun setProducts(data: List<Product>?) {
        productAdapter.setIsDoneLoading(false)
        binding.msvSectionProducts.showLoading()
        products.apply {
            clear()
            data?.let { addAll(data) }
        }
        productAdapter.submitList(products.toList()) {
            if (data != null) {
                productAdapter.setIsDoneLoading(true)
                binding.rvSectionProducts.scrollToPosition(0)
                binding.msvSectionProducts.showContent()
            }
        }
    }

    fun addProducts(vararg product: Product){
        products.addAll(product)
        productAdapter.submitList(products.toList())
    }

    fun showLoading(){
        binding.msvSectionProducts.showLoading()
    }

    fun showError(message: String = "", onRetry: () -> Unit){
        binding.msvSectionProducts.showError(message= message, onRetry = onRetry)
    }

    fun setTitle(title: String){
        binding.tvSectionTitle.text = title
    }

    fun setOnViewAllButtonClicked(listener: () -> Unit) {
        this.onViewAllButtonClicked = listener
    }

    override fun onDetachedFromWindow() {
        _binding = null
        super.onDetachedFromWindow()
    }
}
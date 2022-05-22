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
import com.bangkit.pricely.presentation.main.HorizontalProductAdapter
import com.bangkit.pricely.util.showToast

class ProductsSectionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var _binding: LayoutProductsSectionBinding? = null
    private val binding
        get() = _binding!!
    private val productAdapter: HorizontalProductAdapter by lazy {
        HorizontalProductAdapter(
            onItemClicked = {
                ProductDetailActivity.start(context)
            },
            onViewAllButtonClicked = {
                onViewAllButtonClicked?.invoke()
            }
        )
    }

    private var sectionTitle = ""
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
                adapter = productAdapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
        }
    }

    private fun setupAction() {
        binding.btnViewAllProduct.setOnClickListener {
            onViewAllButtonClicked?.invoke()
        }
    }

    fun setProducts(data: List<Product>?) {
        productAdapter.submitList(data)
    }

    fun setOnViewAllButtonClicked(listener: () -> Unit) {
        this.onViewAllButtonClicked = listener
    }

    override fun onDetachedFromWindow() {
        _binding = null
        super.onDetachedFromWindow()
    }
}
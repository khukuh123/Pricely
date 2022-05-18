package com.bangkit.pricely.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.bangkit.pricely.domain.product.ProductUseCase

class ProductViewModel(private val productUseCase: ProductUseCase): ViewModel() {
}
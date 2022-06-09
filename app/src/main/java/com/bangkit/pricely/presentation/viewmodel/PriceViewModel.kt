package com.bangkit.pricely.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.pricely.domain.price.PriceUseCase
import com.bangkit.pricely.domain.price.model.Price
import com.bangkit.pricely.domain.price.model.PriceEntry
import com.bangkit.pricely.domain.product.model.Product
import com.bangkit.pricely.domain.util.Resource
import com.bangkit.pricely.util.collectResult

class PriceViewModel(private val priceUseCase: PriceUseCase) : ViewModel() {

    private val _availableYears: MutableLiveData<Resource<List<Price>>> = MutableLiveData()
    val availableYears: LiveData<Resource<List<Price>>> get() = _availableYears

    val priceByMonthAndYear: LiveData<Resource<Product>> get() = _priceByMonthAndYear
    private val _priceByMonthAndYear: MutableLiveData<Resource<Product>> = MutableLiveData()

    private val _productPrices: MutableLiveData<Resource<PriceEntry>> = MutableLiveData()
    val productPrices: LiveData<Resource<PriceEntry>> get() = _productPrices

    init {
        _availableYears.value = Resource.Loading()
        _priceByMonthAndYear.value = Resource.Loading()
        _productPrices.value = Resource.Loading()
    }

    fun getProductPriceByMonthAndYear(productId: Int, month: Int, year: Int) {
        _priceByMonthAndYear.value = Resource.Loading()
        viewModelScope.collectResult(_priceByMonthAndYear) {
            priceUseCase.getProductPriceByMonthAndYear(productId, month, year)
        }
    }

    fun getProductAvailableYears(productId: Int) {
        _availableYears.value = Resource.Loading()
        viewModelScope.collectResult(_availableYears) {
            priceUseCase.getProductAvailableYears(productId)
        }
    }

    fun getProductPrices(productId: Int, isMonthly: Boolean) {
        _productPrices.value = Resource.Loading()
        viewModelScope.collectResult(_productPrices) {
            priceUseCase.getProductPrices(productId, isMonthly)
        }
    }
}

package com.bangkit.pricely.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.pricely.domain.price.PriceUseCase
import com.bangkit.pricely.domain.price.model.Price
import com.bangkit.pricely.domain.price.model.PriceEntry
import com.bangkit.pricely.domain.util.Resource
import com.bangkit.pricely.util.collectResult

class PriceViewModel(private val priceUseCase: PriceUseCase) : ViewModel() {

    private val _availableYears: MutableLiveData<Resource<List<Price>>> = MutableLiveData()
    private val _priceByMontAndYear: MutableLiveData<Resource<Price>> = MutableLiveData()

    val priceByMontAndYear: LiveData<Resource<Price>> get() = _priceByMontAndYear
    val availableYears: LiveData<Resource<List<Price>>> get() = _availableYears

    private val _productPrices: MutableLiveData<Resource<PriceEntry>> = MutableLiveData()
    val productPrices: LiveData<Resource<PriceEntry>> get() = _productPrices

    init {
        _availableYears.value = Resource.Loading()
        _priceByMontAndYear.value = Resource.Loading()
        _productPrices.value = Resource.Loading()
    }

    fun getProductPriceByMonthAndYear(productId: Int, month: Int, year: Int) {
        _priceByMontAndYear.value = Resource.Loading()
        viewModelScope.collectResult(_priceByMontAndYear) {
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

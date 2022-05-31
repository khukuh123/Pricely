package com.bangkit.pricely.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.pricely.domain.price.PriceUseCase
import com.bangkit.pricely.domain.price.model.Price
import com.bangkit.pricely.domain.util.Resource
import com.bangkit.pricely.util.collectResult

class PriceViewModel(private val priceUseCase: PriceUseCase) : ViewModel() {

    private val _availableYears: MutableLiveData<Resource<List<Price>>> = MutableLiveData()
    val availableYears: LiveData<Resource<List<Price>>> get() = _availableYears

    private val _priceByMontAndYear: MutableLiveData<Resource<Price>> = MutableLiveData()
    val priceByMontAndYear: LiveData<Resource<Price>> get() = _priceByMontAndYear

    init {
        _availableYears.value = Resource.Loading()
        _priceByMontAndYear.value = Resource.Loading()
    }

    fun getAvailableYears(productId: Int) {
        viewModelScope.collectResult(_availableYears) {
            priceUseCase.getAvailableYears(productId)
        }
    }

    fun getPriceByMonthAndYear(productId: Int, month: Int, year: Int) {
        viewModelScope.collectResult(_priceByMontAndYear) {
            priceUseCase.getPriceByMonthAndYear(productId, month, year)
        }
    }

}
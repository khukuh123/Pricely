package com.bangkit.pricely.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.pricely.domain.product.ProductUseCase
import com.bangkit.pricely.domain.product.model.Product
import com.bangkit.pricely.domain.product.model.Suggestion
import com.bangkit.pricely.domain.util.Resource
import com.bangkit.pricely.util.collectResult

class ProductViewModel(private val productUseCase: ProductUseCase): ViewModel() {

    private val _productDetail: MutableLiveData<Resource<Product>> = MutableLiveData()
    val productDetail: LiveData<Resource<Product>> get() = _productDetail

    private val _productsByCategory: MutableLiveData<Resource<List<Product>>> = MutableLiveData()
    val productsByCategory: LiveData<Resource<List<Product>>> get() = _productsByCategory

    private val _productsRecommendation: MutableLiveData<Resource<List<Product>>> =
        MutableLiveData()
    val productsRecommendation: LiveData<Resource<List<Product>>> get() = _productsRecommendation

    private val _productsRecommendationByCategory: MutableLiveData<Resource<List<Product>>> =
        MutableLiveData()
    val productsRecommendationByCategory: LiveData<Resource<List<Product>>> get() = _productsRecommendationByCategory

    private val _products: MutableLiveData<Resource<List<Product>>> =
        MutableLiveData()
    val products: LiveData<Resource<List<Product>>> get() = _products

    private val _suggestions: MutableLiveData<Resource<List<Suggestion>>> = MutableLiveData()
    val suggestions: LiveData<Resource<List<Suggestion>>> get() = _suggestions

    init {
        _productDetail.value = Resource.Loading()
        _productsByCategory.value = Resource.Loading()
        _productsRecommendation.value = Resource.Loading()
        _productsRecommendationByCategory.value = Resource.Loading()
        _suggestions.value = Resource.Loading()
    }

    fun getProductDetail(productId: Int) {
        _productDetail.value = Resource.Loading()
        viewModelScope.collectResult(_productDetail) {
            productUseCase.getProductDetail(productId)
        }
    }

    fun getProducts() {
        _products.value = Resource.Loading()
        viewModelScope.collectResult(_products) {
            productUseCase.getProducts()
        }
    }

    fun getProductsByCategory(categoryId: Int) {
        _productsByCategory.value = Resource.Loading()
        viewModelScope.collectResult(_productsByCategory) {
            productUseCase.getProductsByCategory(categoryId)
        }
    }

    fun getProductsRecommendation(recommendation: Boolean) {
        _productsRecommendationByCategory.value = Resource.Loading()
        viewModelScope.collectResult(_productsRecommendation) {
            productUseCase.getProductsRecommendation(recommendation)
        }
    }

    fun getProductsRecommendationByCategory(categoryId: Int, recommendation: Boolean) {
        _productsRecommendationByCategory.value = Resource.Loading()
        viewModelScope.collectResult(_productsRecommendationByCategory) {
            productUseCase.getProductsRecommendationByCategory(categoryId, recommendation)
        }
    }

    fun getSuggestions(){
        _suggestions.value = Resource.Loading()
        viewModelScope.collectResult(_suggestions) {
            productUseCase.getSuggestions()
        }
    }
}
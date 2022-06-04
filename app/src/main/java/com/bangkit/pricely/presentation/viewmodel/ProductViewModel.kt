package com.bangkit.pricely.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.pricely.data.product.remote.response.AllProductResponseItem
import com.bangkit.pricely.data.product.remote.response.RecommendationResponseItem
import com.bangkit.pricely.domain.product.ProductUseCase
import com.bangkit.pricely.domain.product.model.Product
import com.bangkit.pricely.domain.util.Resource
import com.bangkit.pricely.util.collectResult

class ProductViewModel(private val productUseCase: ProductUseCase): ViewModel() {

    private val _productDetail: MutableLiveData<Resource<Product>> = MutableLiveData()
    val productDetail: LiveData<Resource<Product>> get() = _productDetail

    private val _listRecommendation: MutableLiveData<Resource<List<RecommendationResponseItem>>> =
        MutableLiveData()
    val listRecommendation: LiveData<Resource<List<RecommendationResponseItem>>> get() = _listRecommendation

    private val _listAllProduct: MutableLiveData<Resource<List<AllProductResponseItem>>> =
        MutableLiveData()
    val listAllProduct: LiveData<Resource<List<AllProductResponseItem>>> get() = _listAllProduct

    init {
        _productDetail.value = Resource.Loading()
        _listRecommendation.value = Resource.Loading()
    }

    fun getProductDetail(productId: Int){
        _productDetail.value = Resource.Loading()
        viewModelScope.collectResult(_productDetail){
            productUseCase.getProductDetail(productId)
        }
    }

    fun getListRecommendation(recommendation: Boolean){
        _listRecommendation.value = Resource.Loading()
        viewModelScope.collectResult(_listRecommendation){
            productUseCase.getListRecommendation(recommendation)
        }
    }

    fun getListRecommendationByCategory(categoryId: Int, recommendation: Boolean){
        _listRecommendation.value = Resource.Loading()
        viewModelScope.collectResult(_listRecommendation){
            productUseCase.getListRecommendationByCategory(categoryId, recommendation)
        }
    }

    fun getListAllProduct() {
        _listAllProduct.value = Resource.Loading()
        viewModelScope.collectResult(_listAllProduct) {
            productUseCase.getListAllProduct()
        }
    }
}
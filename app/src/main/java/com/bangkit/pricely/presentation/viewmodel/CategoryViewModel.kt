package com.bangkit.pricely.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.pricely.domain.category.CategoryUseCase
import com.bangkit.pricely.domain.category.model.Category
import com.bangkit.pricely.domain.util.Resource
import com.bangkit.pricely.util.collectResult

class CategoryViewModel(private val categoryUseCase: CategoryUseCase) : ViewModel() {

    private val _categoryList: MutableLiveData<Resource<List<Category>>> = MutableLiveData()
    private val _categoryDetail: MutableLiveData<Resource<Category>> = MutableLiveData()

    val categoryList: LiveData<Resource<List<Category>>>  get() = _categoryList
    val categoryDetail: LiveData<Resource<Category>> get() = _categoryDetail

    init {
        _categoryList.value = Resource.Loading()
        _categoryDetail.value = Resource.Loading()
    }

    fun getCategoryList(){
        _categoryList.value = Resource.Loading()
        viewModelScope.collectResult(_categoryList){
            categoryUseCase.getCategoryList()
        }
    }

    fun getCategoryDetail(categoryId: Int){
        _categoryDetail.value = Resource.Loading()
        viewModelScope.collectResult(_categoryDetail){
            categoryUseCase.getCategoryDetail(categoryId)
        }
    }


}
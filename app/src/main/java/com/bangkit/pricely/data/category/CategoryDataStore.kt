package com.bangkit.pricely.data.category

import com.bangkit.pricely.data.category.remote.CategoryApi
import com.bangkit.pricely.data.util.call
import com.bangkit.pricely.data.util.mapToDomain
import com.bangkit.pricely.domain.category.model.Category
import com.bangkit.pricely.domain.category.model.map
import com.bangkit.pricely.domain.util.Resource
import kotlinx.coroutines.flow.Flow

class CategoryDataStore(private val webService: CategoryApi) : CategoryRepository {

    override suspend fun getCategoryDetail(categoryId: Int): Flow<Resource<Category>> =
        webService.getCategoryDetail(categoryId).call().mapToDomain { it.map() }

    override suspend fun getCategoryList(): Flow<Resource<List<Category>>> =
        webService.getCategoryList().call().mapToDomain { it.map() }

}
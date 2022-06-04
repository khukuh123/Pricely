package com.bangkit.pricely.data.product

import com.bangkit.pricely.domain.product.model.Product
import com.bangkit.pricely.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getProductDetail(productId: Int): Flow<Resource<Product>>

    suspend fun getAllProductCategory(categoryId: Int): Flow<Resource<List<Product>>>

    suspend fun getListRecommendation(recommendation: Boolean): Flow<Resource<List<Product>>>

    suspend fun getListRecommendationByCategory(categoryId: Int, recommendation: Boolean):
            Flow<Resource<List<Product>>>

    suspend fun getListAllProduct(): Flow<Resource<List<Product>>>
}
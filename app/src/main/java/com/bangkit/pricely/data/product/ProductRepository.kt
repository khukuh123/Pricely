package com.bangkit.pricely.data.product

import com.bangkit.pricely.domain.product.model.Product
import com.bangkit.pricely.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getProductDetail(productId: Int): Flow<Resource<Product>>

    suspend fun getProducts(): Flow<Resource<List<Product>>>

    suspend fun getProductsByCategory(categoryId: Int): Flow<Resource<List<Product>>>

    suspend fun getProductsRecommendation(recommendation: Boolean): Flow<Resource<List<Product>>>

    suspend fun getProductsRecommendationByCategory(categoryId: Int, recommendation: Boolean):
            Flow<Resource<List<Product>>>
}
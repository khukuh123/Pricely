package com.bangkit.pricely.domain.product

import com.bangkit.pricely.data.product.remote.response.AllProductResponseItem
import com.bangkit.pricely.data.product.remote.response.RecommendationResponseItem
import com.bangkit.pricely.domain.product.model.Product
import com.bangkit.pricely.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface ProductUseCase {
    suspend fun getProductDetail(productId: Int): Flow<Resource<Product>>

    suspend fun getListRecommendation(recommendation: Boolean): Flow<Resource<List<RecommendationResponseItem>>>

    suspend fun getListRecommendationByCategory(categoryId: Int, recommendation: Boolean):
            Flow<Resource<List<RecommendationResponseItem>>>

    suspend fun getListAllProduct(): Flow<Resource<List<AllProductResponseItem>>>
}
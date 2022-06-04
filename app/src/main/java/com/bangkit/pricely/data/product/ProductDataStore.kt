package com.bangkit.pricely.data.product

import com.bangkit.pricely.data.product.remote.ProductApi
import com.bangkit.pricely.data.product.remote.response.AllProductResponseItem
import com.bangkit.pricely.data.product.remote.response.RecommendationResponseItem
import com.bangkit.pricely.data.util.call
import com.bangkit.pricely.data.util.mapToDomain
import com.bangkit.pricely.domain.product.model.Product
import com.bangkit.pricely.domain.product.model.map
import com.bangkit.pricely.domain.util.Resource
import kotlinx.coroutines.flow.Flow

class ProductDataStore(private val webService: ProductApi) : ProductRepository {
    override suspend fun getProductDetail(productId: Int): Flow<Resource<Product>> =
        webService.getProductDetail(productId).call().mapToDomain{ it.map() }

    override suspend fun getListRecommendation(recommendation: Boolean):
            Flow<Resource<List<RecommendationResponseItem>>> =
        webService.getListRecommendation(recommendation).call().mapToDomain{ it.map() }

    override suspend fun getListRecommendationByCategory(categoryId: Int, recommendation: Boolean):
            Flow<Resource<List<RecommendationResponseItem>>> =
        webService.getListRecommendationByCategory(categoryId, recommendation)
            .call().mapToDomain{ it.map() }

    override suspend fun getListAllProduct(): Flow<Resource<List<AllProductResponseItem>>> =
        webService.getListAllProduct().call().mapToDomain{ it.map() }

}
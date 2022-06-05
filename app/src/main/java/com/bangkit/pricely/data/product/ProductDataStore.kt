package com.bangkit.pricely.data.product

import com.bangkit.pricely.data.product.remote.ProductApi
import com.bangkit.pricely.data.util.call
import com.bangkit.pricely.data.util.mapToDomain
import com.bangkit.pricely.domain.product.model.Product
import com.bangkit.pricely.domain.product.model.Suggestion
import com.bangkit.pricely.domain.product.model.map
import com.bangkit.pricely.domain.util.Resource
import kotlinx.coroutines.flow.Flow

class ProductDataStore(private val webService: ProductApi) : ProductRepository {
    override suspend fun getProductDetail(productId: Int): Flow<Resource<Product>> =
        webService.getProductDetail(productId).call().mapToDomain { it.map() }

    override suspend fun getProducts(): Flow<Resource<List<Product>>> =
        webService.getProducts().call().mapToDomain { it.map() }

    override suspend fun getProductsByCategory(categoryId: Int): Flow<Resource<List<Product>>> =
        webService.getProductsByCategory(categoryId).call().mapToDomain { it.map() }

    override suspend fun getProductsRecommendation(recommendation: Boolean):
            Flow<Resource<List<Product>>> =
        webService.getProductsRecommendation(recommendation).call().mapToDomain { it.map() }

    override suspend fun getProductsRecommendationByCategory(categoryId: Int, recommendation: Boolean):
            Flow<Resource<List<Product>>> =
        webService.getProductsRecommendationByCategory(categoryId, recommendation)
            .call().mapToDomain { it.map() }

    override suspend fun getSuggestions(): Flow<Resource<List<Suggestion>>> =
        webService.getSuggestions().call().mapToDomain { it.map() }
}
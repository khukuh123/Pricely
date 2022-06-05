package com.bangkit.pricely.domain.product

import com.bangkit.pricely.data.product.ProductRepository
import com.bangkit.pricely.domain.product.model.Product
import com.bangkit.pricely.domain.product.model.Suggestion
import com.bangkit.pricely.domain.util.Resource
import kotlinx.coroutines.flow.Flow

class ProductInteractor(private val productRepository: ProductRepository) : ProductUseCase {
    override suspend fun getProductDetail(productId: Int): Flow<Resource<Product>> =
        productRepository.getProductDetail(productId)

    override suspend fun getProducts(): Flow<Resource<List<Product>>> =
        productRepository.getProducts()


    override suspend fun getProductsByCategory(categoryId: Int): Flow<Resource<List<Product>>> =
        productRepository.getProductsByCategory(categoryId)

    override suspend fun getProductsRecommendation(recommendation: Boolean):
            Flow<Resource<List<Product>>> =
        productRepository.getProductsRecommendation(recommendation)

    override suspend fun getProductsRecommendationByCategory(categoryId: Int, recommendation: Boolean):
            Flow<Resource<List<Product>>> =
        productRepository.getProductsRecommendationByCategory(categoryId, recommendation)

    override suspend fun getSuggestions(): Flow<Resource<List<Suggestion>>> =
        productRepository.getSuggestions()
}
package com.bangkit.pricely.domain.product

import com.bangkit.pricely.data.product.ProductRepository
import com.bangkit.pricely.data.product.remote.response.AllProductResponseItem
import com.bangkit.pricely.data.product.remote.response.RecommendationResponseItem
import com.bangkit.pricely.domain.product.model.Product
import com.bangkit.pricely.domain.util.Resource
import kotlinx.coroutines.flow.Flow

class ProductInteractor(private val productRepository: ProductRepository) : ProductUseCase{
    override suspend fun getProductDetail(productId: Int): Flow<Resource<Product>> =
        productRepository.getProductDetail(productId)

    override suspend fun getListRecommendation(recommendation: Boolean):
            Flow<Resource<List<RecommendationResponseItem>>> =
        productRepository.getListRecommendation(recommendation)

    override suspend fun getListRecommendationByCategory(categoryId: Int, recommendation: Boolean):
            Flow<Resource<List<RecommendationResponseItem>>> =
        productRepository.getListRecommendationByCategory(categoryId, recommendation)

    override suspend fun getListAllProduct(): Flow<Resource<List<AllProductResponseItem>>> =
        productRepository.getListAllProduct()

}
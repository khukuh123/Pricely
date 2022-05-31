package com.bangkit.pricely.domain.product

import com.bangkit.pricely.data.product.ProductRepository
import com.bangkit.pricely.domain.product.model.Product
import com.bangkit.pricely.domain.util.Resource
import kotlinx.coroutines.flow.Flow

class ProductInteractor(private val productRepository: ProductRepository) : ProductUseCase{
    override suspend fun getProductDetail(productId: Int): Flow<Resource<Product>> =
        productRepository.getProductDetail(productId)
}
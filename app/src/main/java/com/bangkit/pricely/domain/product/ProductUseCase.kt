package com.bangkit.pricely.domain.product

import com.bangkit.pricely.domain.product.model.Product
import com.bangkit.pricely.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface ProductUseCase {
    suspend fun getProductDetail(productId: Int): Flow<Resource<Product>>
}
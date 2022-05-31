package com.bangkit.pricely.data.product

import com.bangkit.pricely.data.product.remote.response.ProductDetail
import com.bangkit.pricely.data.util.BaseResponse
import com.bangkit.pricely.domain.product.model.Product
import com.bangkit.pricely.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface ProductRepository {
    suspend fun getProductDetail(productId: Int): Flow<Resource<Product>>
}
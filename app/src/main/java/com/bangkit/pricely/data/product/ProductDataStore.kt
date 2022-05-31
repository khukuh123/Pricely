package com.bangkit.pricely.data.product

import com.bangkit.pricely.data.product.remote.ProductApi
import com.bangkit.pricely.data.product.remote.response.ProductDetail
import com.bangkit.pricely.data.util.ApiResult
import com.bangkit.pricely.data.util.call
import com.bangkit.pricely.data.util.mapToDomain
import com.bangkit.pricely.domain.product.model.Product
import com.bangkit.pricely.domain.product.model.map
import com.bangkit.pricely.domain.util.Resource
import kotlinx.coroutines.flow.Flow

class ProductDataStore(private val webService: ProductApi) : ProductRepository {
    override suspend fun getProductDetail(productId: Int): Flow<Resource<Product>> =
        webService.getProductDetail(productId).call().mapToDomain{ it.map() }
}
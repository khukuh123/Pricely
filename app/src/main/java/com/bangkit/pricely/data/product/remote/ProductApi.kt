package com.bangkit.pricely.data.product.remote

import com.bangkit.pricely.data.product.remote.response.ProductDetailItem
import com.bangkit.pricely.data.util.BaseResponse
import retrofit2.Response

class ProductApi(private val api: ProductApiClient) : ProductApiClient {
    override suspend fun getProductDetail(id: Int): Response<BaseResponse<ProductDetailItem>> =
        api.getProductDetail(id)
}
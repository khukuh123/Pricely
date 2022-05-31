package com.bangkit.pricely.data.product.remote

import com.bangkit.pricely.data.product.remote.response.ProductDetailItem
import com.bangkit.pricely.data.util.BaseResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApiClient {
    @GET("api/product/{productId}")
    suspend fun getProductDetail(@Path("productId") id: Int): Response<BaseResponse<ProductDetailItem>>
}
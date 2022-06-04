package com.bangkit.pricely.data.product.remote

import com.bangkit.pricely.data.product.remote.response.AllProductResponse
import com.bangkit.pricely.data.product.remote.response.ProductDetailItem
import com.bangkit.pricely.data.product.remote.response.RecommendationResponse
import com.bangkit.pricely.data.util.BaseResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApiClient {
    @GET("api/product/{productId}")
    suspend fun getProductDetail(@Path("productId") id: Int): Response<BaseResponse<ProductDetailItem>>

    @GET("api/products?")
    suspend fun getListRecommendation(@Query("recommendation") recommendation: Boolean):
            Response<BaseResponse<RecommendationResponse>>

    @GET("api/products?")
    suspend fun getListRecommendationByCategory(
        @Query("category") categoryId: Int,
        @Query("recommendation") recommendation: Boolean):
            Response<BaseResponse<RecommendationResponse>>

    @GET("api/products?")
    suspend fun getListAllProduct(): Response<BaseResponse<AllProductResponse>>
}
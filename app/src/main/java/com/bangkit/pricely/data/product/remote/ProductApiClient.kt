package com.bangkit.pricely.data.product.remote

import com.bangkit.pricely.data.product.remote.response.ProductDetailItem
import com.bangkit.pricely.data.product.remote.response.ProductListResponse
import com.bangkit.pricely.data.util.BaseResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApiClient {
    @GET("api/product/{productId}")
    suspend fun getProductDetail(@Path("productId") id: Int): Response<BaseResponse<ProductDetailItem>>

    @GET("api/products")
    suspend fun getProductsByCategory(@Query("category") id: Int): Response<BaseResponse<ProductListResponse>>


    @GET("api/products")
    suspend fun getProductsRecommendation(@Query("recommendation") recommendation: Boolean):
            Response<BaseResponse<ProductListResponse>>

    @GET("api/products")
    suspend fun getProductsRecommendationByCategory(
        @Query("category") categoryId: Int,
        @Query("recommendation") recommendation: Boolean,
    ):
            Response<BaseResponse<ProductListResponse>>

    @GET("api/products")
    suspend fun getProducts(): Response<BaseResponse<ProductListResponse>>
}
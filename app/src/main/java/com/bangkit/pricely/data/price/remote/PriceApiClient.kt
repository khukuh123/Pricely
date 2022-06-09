package com.bangkit.pricely.data.price.remote

import com.bangkit.pricely.data.price.remote.response.PriceListResponse
import com.bangkit.pricely.data.product.remote.response.ProductDetailItem
import com.bangkit.pricely.data.util.BaseResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface PriceApiClient {
    @GET("/api/price/{productId}?year=true")
    suspend fun getProductAvailableYears(@Path("productId") productId: Int): Response<BaseResponse<PriceListResponse>>

    @GET("/api/product/{productId}")
    suspend fun getProductPriceByMonthAndYear(
        @Path("productId") productId: Int,
        @Query("month") month: Int,
        @Query("year") year: Int,
    ): Response<BaseResponse<ProductDetailItem>>

    @GET("/api/price/{productId}")
    suspend fun getProductPrices(
        @Path("productId") productId: Int,
        @QueryMap query: Map<String, String>,
    ): Response<BaseResponse<PriceListResponse>>
}
package com.bangkit.pricely.data.category.remote

import com.bangkit.pricely.data.category.remote.response.CategoryDetailItem
import com.bangkit.pricely.data.category.remote.response.CategoryListResponse
import com.bangkit.pricely.data.util.BaseResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CategoryApiClient {

    @GET("api/category/{id_category}")
    suspend fun getCategoryDetail(@Path("id_category") id: Int): Response<BaseResponse<CategoryDetailItem>>

    @GET("api/categories")
    suspend fun getCategoryList(): Response<BaseResponse<CategoryListResponse>>

 }
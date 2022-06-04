package com.bangkit.pricely.data.category.remote

import com.bangkit.pricely.data.category.remote.response.CategoryDetailItem
import com.bangkit.pricely.data.category.remote.response.CategoryListResponse
import com.bangkit.pricely.data.util.BaseResponse
import retrofit2.Response

class CategoryApi(private val api: CategoryApiClient): CategoryApiClient {
    override suspend fun getCategoryDetail(id: Int): Response<BaseResponse<CategoryDetailItem>> =
        api.getCategoryDetail(id)

    override suspend fun getCategoryList(): Response<BaseResponse<CategoryListResponse>> =
        api.getCategoryList()
}
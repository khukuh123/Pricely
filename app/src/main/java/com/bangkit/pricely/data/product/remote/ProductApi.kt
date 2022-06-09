package com.bangkit.pricely.data.product.remote

import com.bangkit.pricely.data.product.remote.response.ProductDetailItem
import com.bangkit.pricely.data.product.remote.response.ProductListResponse
import com.bangkit.pricely.data.product.remote.response.SuggestionResponseItem
import com.bangkit.pricely.data.util.BaseResponse
import retrofit2.Response

class ProductApi(private val api: ProductApiClient) : ProductApiClient {
    override suspend fun getProductDetail(id: Int): Response<BaseResponse<ProductDetailItem>> =
        api.getProductDetail(id)

    override suspend fun getProducts(): Response<BaseResponse<ProductListResponse>> =
        api.getProducts()

    override suspend fun getProductsByCategory(id: Int): Response<BaseResponse<ProductListResponse>> =
        api.getProductsByCategory(id)

    override suspend fun getListProductsByName(name: String): Response<BaseResponse<ProductListResponse>> =
        api.getListProductsByName(name)

    override suspend fun getProductsRecommendation(recommendation: Boolean):
            Response<BaseResponse<ProductListResponse>> =
        api.getProductsRecommendation(recommendation)

    override suspend fun getProductsRecommendationByCategory(categoryId: Int, recommendation: Boolean):
            Response<BaseResponse<ProductListResponse>> =
        api.getProductsRecommendationByCategory(categoryId, recommendation)

    override suspend fun getSuggestions(): Response<BaseResponse<SuggestionResponseItem>> =
        api.getSuggestions()
}
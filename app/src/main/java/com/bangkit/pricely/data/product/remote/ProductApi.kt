package com.bangkit.pricely.data.product.remote

import com.bangkit.pricely.data.product.remote.response.AllProductResponse
import com.bangkit.pricely.data.product.remote.response.AllProductCategoryResponse
import com.bangkit.pricely.data.product.remote.response.ProductDetailItem
import com.bangkit.pricely.data.product.remote.response.RecommendationResponse
import com.bangkit.pricely.data.util.BaseResponse
import retrofit2.Response

class ProductApi(private val api: ProductApiClient) : ProductApiClient {
    override suspend fun getProductDetail(id: Int): Response<BaseResponse<ProductDetailItem>> =
        api.getProductDetail(id)

    override suspend fun getAllProductCategory(id: Int): Response<BaseResponse<AllProductCategoryResponse>> =
        api.getAllProductCategory(id)

    override suspend fun getListRecommendation(recommendation: Boolean):
            Response<BaseResponse<RecommendationResponse>> =
        api.getListRecommendation(recommendation)

    override suspend fun getListRecommendationByCategory(categoryId: Int, recommendation: Boolean):
            Response<BaseResponse<RecommendationResponse>> =
        api.getListRecommendationByCategory(categoryId, recommendation)

    override suspend fun getListAllProduct(): Response<BaseResponse<AllProductResponse>> =
        api.getListAllProduct()


}
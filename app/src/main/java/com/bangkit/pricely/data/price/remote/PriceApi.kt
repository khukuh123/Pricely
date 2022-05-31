package com.bangkit.pricely.data.price.remote

import com.bangkit.pricely.data.price.remote.response.PriceItem
import com.bangkit.pricely.data.price.remote.response.PriceListResponse
import com.bangkit.pricely.data.util.BaseResponse
import retrofit2.Response

class PriceApi(private val api: PriceApiClient): PriceApiClient {
    override suspend fun getProductAvailableYears(productId: Int): Response<BaseResponse<PriceListResponse>> =
        api.getProductAvailableYears(productId)

    override suspend fun getProductPriceByMonthAndYear(productId: Int, month: Int, year: Int): Response<BaseResponse<PriceItem>> =
        api.getProductPriceByMonthAndYear(productId, month, year)

    override suspend fun getProductPrices(productId: Int, query: Map<String, String>): Response<BaseResponse<PriceListResponse>> =
        api.getProductPrices(productId, query)
}
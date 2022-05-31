package com.bangkit.pricely.data.price.remote

import com.bangkit.pricely.data.price.remote.response.PriceItem
import com.bangkit.pricely.data.price.remote.response.PriceListResponse
import com.bangkit.pricely.data.util.BaseResponse
import retrofit2.Response

class PriceApi(private val api: PriceApiClient): PriceApiClient {
    override suspend fun getAvailableYears(productId: Int): Response<BaseResponse<PriceListResponse>> =
        api.getAvailableYears(productId)

    override suspend fun getPriceByMonthAndYear(productId: Int, month: Int, year: Int): Response<BaseResponse<PriceItem>> =
        api.getPriceByMonthAndYear(productId, month, year)
}
package com.bangkit.pricely.data.price

import com.bangkit.pricely.data.price.remote.PriceApiClient
import com.bangkit.pricely.data.util.call
import com.bangkit.pricely.data.util.mapToDomain
import com.bangkit.pricely.domain.price.model.Price
import com.bangkit.pricely.domain.price.model.map
import com.bangkit.pricely.domain.util.Resource
import kotlinx.coroutines.flow.Flow

class PriceDataStore(private val webService: PriceApiClient) : PriceRepository {
    override suspend fun getAvailableYears(productId: Int): Flow<Resource<List<Price>>> =
        webService.getAvailableYears(productId).call().mapToDomain { it.map() }

    override suspend fun getPriceByMonthAndYear(productId: Int, month: Int, year: Int): Flow<Resource<Price>> =
        webService.getPriceByMonthAndYear(productId, month, year).call().mapToDomain { it.map() }
}
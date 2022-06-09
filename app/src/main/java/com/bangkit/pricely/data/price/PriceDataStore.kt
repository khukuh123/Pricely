package com.bangkit.pricely.data.price

import com.bangkit.pricely.data.price.remote.PriceApiClient
import com.bangkit.pricely.data.util.call
import com.bangkit.pricely.data.util.mapToDomain
import com.bangkit.pricely.domain.price.model.Price
import com.bangkit.pricely.domain.price.model.map
import com.bangkit.pricely.domain.product.model.Product
import com.bangkit.pricely.domain.product.model.map
import com.bangkit.pricely.domain.util.Resource
import kotlinx.coroutines.flow.Flow

class PriceDataStore(private val webService: PriceApiClient) : PriceRepository {
    override suspend fun getProductAvailableYears(productId: Int): Flow<Resource<List<Price>>> =
        webService.getProductAvailableYears(productId).call().mapToDomain { it.map() }

    override suspend fun getProductPriceByMonthAndYear(productId: Int, month: Int, year: Int): Flow<Resource<Product>> =
        webService.getProductPriceByMonthAndYear(productId, month, year).call().mapToDomain { it.map() }

    override suspend fun getProductPrices(productId: Int, isMonthly: Boolean): Flow<Resource<List<Price>>> {
        val query = if (isMonthly) mapOf("month" to "true") else mapOf("year" to "true")
        return webService.getProductPrices(productId, query).call().mapToDomain { it.map() }
    }
}
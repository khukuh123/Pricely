package com.bangkit.pricely.data.price

import com.bangkit.pricely.domain.price.model.Price
import com.bangkit.pricely.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface PriceRepository {
    suspend fun getAvailableYears(productId: Int): Flow<Resource<List<Price>>>

    suspend fun getPriceByMonthAndYear(productId: Int, month: Int, year: Int): Flow<Resource<Price>>
}
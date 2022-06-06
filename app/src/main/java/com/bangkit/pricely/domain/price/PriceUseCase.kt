package com.bangkit.pricely.domain.price

import com.bangkit.pricely.domain.price.model.Price
import com.bangkit.pricely.domain.price.model.PriceEntry
import com.bangkit.pricely.domain.util.Resource
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.flow.Flow

interface PriceUseCase {
    suspend fun getProductAvailableYears(productId: Int): Flow<Resource<List<Price>>>

    suspend fun getProductPriceByMonthAndYear(productId: Int, month: Int, year: Int): Flow<Resource<Price>>

    suspend fun getProductPrices(productId: Int, isMonthly: Boolean): Flow<Resource<PriceEntry>>
}
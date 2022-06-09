package com.bangkit.pricely.data.price

import com.bangkit.pricely.domain.price.model.Price
import com.bangkit.pricely.domain.product.model.Product
import com.bangkit.pricely.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface PriceRepository {
    suspend fun getProductAvailableYears(productId: Int): Flow<Resource<List<Price>>>

    suspend fun getProductPriceByMonthAndYear(productId: Int, month: Int, year: Int): Flow<Resource<Product>>

    suspend fun getProductPrices(productId: Int, isMonthly: Boolean): Flow<Resource<List<Price>>>
}
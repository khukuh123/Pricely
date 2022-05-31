package com.bangkit.pricely.domain.price

import com.bangkit.pricely.data.price.PriceRepository
import com.bangkit.pricely.domain.price.model.Price
import com.bangkit.pricely.domain.util.Resource
import kotlinx.coroutines.flow.Flow

class PriceInteractor(private val repository: PriceRepository) : PriceUseCase {
    override suspend fun getProductAvailableYears(productId: Int): Flow<Resource<List<Price>>> =
        repository.getProductAvailableYears(productId)

    override suspend fun getProductPriceByMonthAndYear(productId: Int, month: Int, year: Int): Flow<Resource<Price>> =
        repository.getProductPriceByMonthAndYear(productId, month, year)

    override suspend fun getProductPrices(productId: Int, isMonthly: Boolean): Flow<Resource<List<Price>>> =
        repository.getProductPrices(productId, isMonthly)
}

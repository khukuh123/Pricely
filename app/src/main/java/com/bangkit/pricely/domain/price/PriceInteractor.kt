package com.bangkit.pricely.domain.price

import com.bangkit.pricely.data.price.PriceRepository
import com.bangkit.pricely.domain.price.model.Price
import com.bangkit.pricely.domain.util.Resource
import kotlinx.coroutines.flow.Flow

class PriceInteractor(private val repository: PriceRepository) : PriceUseCase {
    override suspend fun getAvailableYears(productId: Int): Flow<Resource<List<Price>>> =
        repository.getAvailableYears(productId)

    override suspend fun getPriceByMonthAndYear(productId: Int, month: Int, year: Int): Flow<Resource<Price>> =
        repository.getPriceByMonthAndYear(productId, month, year)
}

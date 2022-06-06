package com.bangkit.pricely.domain.price

import com.bangkit.pricely.data.price.PriceRepository
import com.bangkit.pricely.domain.price.model.Price
import com.bangkit.pricely.domain.price.model.PriceEntry
import com.bangkit.pricely.domain.util.Resource
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.zip
import java.util.*

class PriceInteractor(private val repository: PriceRepository) : PriceUseCase {
    override suspend fun getProductAvailableYears(productId: Int): Flow<Resource<List<Price>>> =
        repository.getProductAvailableYears(productId)

    override suspend fun getProductPriceByMonthAndYear(productId: Int, month: Int, year: Int): Flow<Resource<Price>> =
        repository.getProductPriceByMonthAndYear(productId, month, year)

    override suspend fun getProductPrices(productId: Int, isMonthly: Boolean): Flow<Resource<PriceEntry>> {
        return repository.getProductPrices(productId, isMonthly)
            .zip(repository.getProductAvailableYears(productId)) { prices, years ->
                if (prices.data != null && years.data != null) {
                    val xAxisLabels = if (isMonthly) {
                        getMonthlyLabel(prices.data!!)
                    } else {
                        getAnnualLabel(prices.data!!)
                    }
                    Resource.Success(
                        PriceEntry(
                            labels = xAxisLabels,
                            prices = prices.data!!.mapIndexed { i, price ->
                                Entry(i.toFloat(),price.price.toFloat())
                            }
                        )
                    )
                } else {
                    Resource.Error(
                        999,
                        "Error when loaded product prices and/or available years.\n${prices.errorMessage?.ifEmpty { '-' }} and/or ${years.errorMessage?.ifEmpty { '-' }}"
                    )
                }
            }
    }

    private fun getMonthlyLabel(items: List<Price>): List<String> =
        items.map {
            val month = it.month.take(3).replaceFirstChar { char -> char.uppercase() }
            val stringBuilder = StringBuilder(month)
            val year = Calendar.getInstance().get(Calendar.YEAR) - 1 // TODO: Change it later.await().second.takeLast(2)
            stringBuilder.apply {
                append(" '")
                append(year.toString().takeLast(2))
            }
            stringBuilder.toString()
        }

    private fun getAnnualLabel(items: List<Price>): List<String> =
        items.map { it.year.toString() }
}

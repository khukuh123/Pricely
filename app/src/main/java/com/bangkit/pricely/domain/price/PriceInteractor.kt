package com.bangkit.pricely.domain.price

import com.bangkit.pricely.data.price.PriceRepository
import com.bangkit.pricely.domain.price.model.Price
import com.bangkit.pricely.domain.price.model.PriceEntry
import com.bangkit.pricely.domain.product.model.Product
import com.bangkit.pricely.domain.util.Resource
import com.bangkit.pricely.util.currenDate
import com.bangkit.pricely.util.year
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PriceInteractor(private val repository: PriceRepository) : PriceUseCase {
    override suspend fun getProductAvailableYears(productId: Int): Flow<Resource<List<Price>>> =
        repository.getProductAvailableYears(productId)

    override suspend fun getProductPriceByMonthAndYear(productId: Int, month: Int, year: Int): Flow<Resource<Product>> =
        repository.getProductPriceByMonthAndYear(productId, month, year)

    override suspend fun getProductPrices(productId: Int, isMonthly: Boolean): Flow<Resource<PriceEntry>> {
        return repository.getProductPrices(productId, isMonthly).map {
            val prices = it.data.orEmpty()
//            val entries = prices.mapIndexed { i, price -> Entry(i.toFloat(), price.price.toFloat()) }
            val predictionStart = prices.find { it.year == currenDate.year } ?: Price()
            val predictionStartPos = prices.indexOf(predictionStart).takeIf { it != -1 } ?: prices.lastIndex
            val priceList = prices.subList(0, predictionStartPos).mapIndexed { i, price -> Entry(i.toFloat(), price.price.toFloat()) }
            val predictionList = prices.subList(predictionStartPos - 1, prices.size).mapIndexed { i, price -> Entry((i + priceList.size - 1).toFloat(), price.price.toFloat()) }
            val priceEntry = PriceEntry(
                labels = if (isMonthly)
                    getMonthlyLabel(prices)
                else
                    getAnnualLabel(prices),
                prices = priceList,
                predictions = predictionList
            )
            Resource.Success(priceEntry)
        }
    }

    private fun getMonthlyLabel(items: List<Price>): List<String> =
        items.map {
            val month = it.month.take(3).replaceFirstChar { char -> char.uppercase() }
            val stringBuilder = StringBuilder(month)
            val year = it.year
            stringBuilder.apply {
                append(" '")
                append(year.toString().takeLast(2))
            }
            stringBuilder.toString()
        }

    private fun getAnnualLabel(items: List<Price>): List<String> =
        items.map { it.year.toString() }
}

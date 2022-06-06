package com.bangkit.pricely.domain.price.model

import com.github.mikephil.charting.data.Entry
import com.google.gson.annotations.SerializedName

data class Price(
    val id: Int = 0,
    val productId: Int = 0,
    val month: String = "",
    val price: Int = 0,
    val year: Int = 0
)

data class PriceEntry(
    val labels: List<String> = listOf(),
    val prices: List<Entry> = listOf()
)

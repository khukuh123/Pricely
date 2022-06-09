package com.bangkit.pricely.domain.price.model

import com.bangkit.pricely.data.price.remote.response.PriceItem
import com.bangkit.pricely.data.price.remote.response.PriceListResponse
import com.bangkit.pricely.data.product.remote.response.ProductDetailItem
import com.bangkit.pricely.util.orZero

fun PriceListResponse.map(): List<Price> =
    this.map { it.map() }

fun PriceItem.map(): Price =
    Price(
        id = id.orZero(),
        productId = idProduct.orZero(),
        month = month.orEmpty(),
        price = price.orZero(),
        year = year.orZero()
    )
package com.bangkit.pricely.domain.product.model

import com.bangkit.pricely.data.product.remote.response.ProductDetailItem
import com.bangkit.pricely.data.product.remote.response.ProductItem
import com.bangkit.pricely.data.product.remote.response.ProductListResponse
import com.bangkit.pricely.util.orZero

fun ProductDetailItem.map() =
    Product(
        id = id.orZero(),
        imageUrl = imageUrl.orEmpty(),
        name = name.orEmpty(),
        weight = weight.orZero(),
        unit = unit.orEmpty(),
        price = price.orZero(),
        isRise = false,
        description = description.orEmpty()
    )

fun ProductListResponse.map(): List<Product> = this.map { it.map() }

fun ProductItem.map(): Product =
    Product(
        id = id.orZero(),
        imageUrl = imageUrl.orEmpty(),
        name = name.orEmpty(),
        price = price.orZero(),
        unit = unit.orEmpty(),
        weight = weight.orZero()
    )

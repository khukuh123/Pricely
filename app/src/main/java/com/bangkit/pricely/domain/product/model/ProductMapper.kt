package com.bangkit.pricely.domain.product.model

import com.bangkit.pricely.data.product.remote.response.ProductDetail
import com.bangkit.pricely.util.orZero

fun ProductDetail.map() =
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
package com.bangkit.pricely.domain.product.model

import com.bangkit.pricely.data.product.remote.response.*
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

fun RecommendationResponseItem.map(): RecommendationResponseItem =
    RecommendationResponseItem(
        unit = unit.orEmpty(),
        price = price.orZero(),
        imageUrl = imageUrl.orEmpty(),
        name = name.orEmpty(),
        weight = weight.orZero(),
        id = id.orZero()
    )

fun RecommendationResponse.map(): List<RecommendationResponseItem> = this.map { it.map() }

fun AllProductResponseItem.map(): AllProductResponseItem =
    AllProductResponseItem(
        id = id.orZero(),
        imageUrl = imageUrl.orEmpty(),
        name = name.orEmpty(),
        price = price.orZero(),
        unit = unit.orEmpty(),
        weight = weight.orZero()
    )

fun AllProductResponse.map(): List<AllProductResponseItem> = this.map { it.map() }
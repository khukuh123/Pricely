package com.bangkit.pricely.domain.product.model

import com.bangkit.pricely.data.product.remote.response.AllProductCategoryResponse
import com.bangkit.pricely.data.product.remote.response.AllProductCategoryResponseItem
import com.bangkit.pricely.data.product.remote.response.ProductDetailItem
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

fun AllProductCategoryResponse.map() : List<Product> =
    this.map{
        it.map()
    }

fun AllProductCategoryResponseItem.map() : Product =
    Product(
        id = id.orZero(),
        imageUrl = imageUrl.orEmpty(),
        name = name.orEmpty(),
        weight = weight.orZero(),
        unit = unit.orEmpty(),
        price = price.orZero(),
        isRise = false,
        description = ""
    )

fun RecommendationResponseItem.map(): Product =
    Product(
        unit = unit.orEmpty(),
        price = price.orZero(),
        imageUrl = imageUrl.orEmpty(),
        name = name.orEmpty(),
        weight = weight.orZero(),
        id = id.orZero()
    )

fun RecommendationResponse.map(): List<Product> = this.map { it.map() }

fun AllProductResponseItem.map(): Product =
    Product(
        id = id.orZero(),
        imageUrl = imageUrl.orEmpty(),
        name = name.orEmpty(),
        price = price.orZero(),
        unit = unit.orEmpty(),
        weight = weight.orZero()
    )

fun AllProductResponse.map(): List<Product> = this.map { it.map() }
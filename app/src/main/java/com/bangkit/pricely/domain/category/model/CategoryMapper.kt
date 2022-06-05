package com.bangkit.pricely.domain.category.model

import com.bangkit.pricely.data.category.remote.response.CategoryDetailItem
import com.bangkit.pricely.data.category.remote.response.CategoryListResponse
import com.bangkit.pricely.data.category.remote.response.CategoryListResponseItem
import com.bangkit.pricely.util.orZero

fun CategoryDetailItem.map() : Category =
    Category(
        id = id.orZero(),
        name = name.orEmpty(),
        description = description.orEmpty(),
        type = 0,
        imgUrl = imageUrl.orEmpty()
    )

fun CategoryListResponse.map(): List<Category> =
    this.map {
        it.map()
    }

fun CategoryListResponseItem.map(): Category =
    Category(
        id = id.orZero(),
        name = name.orEmpty(),
        description = description.orEmpty(),
        type = 1,
        imgUrl = imageUrl.orEmpty()
    )


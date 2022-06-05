package com.bangkit.pricely.domain.product.model

import com.bangkit.pricely.data.product.remote.response.SuggestionItem
import com.bangkit.pricely.data.product.remote.response.SuggestionResponseItem
import com.bangkit.pricely.util.orZero

data class Suggestion(
    val id: Int = 0,
    val name: String = "",
)

fun SuggestionResponseItem.map(): List<Suggestion> =
    this.map { it.map() }

fun SuggestionItem.map(): Suggestion =
    Suggestion(
        id = id.orZero(),
        name = name.orEmpty()
    )
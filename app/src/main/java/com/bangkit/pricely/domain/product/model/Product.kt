package com.bangkit.pricely.domain.product.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: Int = 0,
    val imageUrl: String = "",
    val name: String = "",
    val weight: Int = 0,
    val unit: String = "",
    val price: Int = 0,
    val isRising: Boolean = false,
    val description: String = "",
    val month: String = "",
    val year: Int = 0
): Parcelable
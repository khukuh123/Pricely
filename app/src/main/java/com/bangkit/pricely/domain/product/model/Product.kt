package com.bangkit.pricely.domain.product.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product (
    val id: String,
    val name: String,
    val price: Int,
    val unit: String
): Parcelable


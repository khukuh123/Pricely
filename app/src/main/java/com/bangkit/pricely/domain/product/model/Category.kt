package com.bangkit.pricely.domain.product.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    val name: String,
    val description: String,
    val type: Int,
    val imgUrl: String
) : Parcelable

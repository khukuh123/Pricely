package com.bangkit.pricely.domain.category.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    val id: Int,
    val name: String,
    val description: String,
    val type: Int,
    val imgUrl: String
) : Parcelable

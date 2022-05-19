package com.bangkit.pricely.domain.product

data class Product(
    val id: String,
    val name: String,
    val weight: Double,
    val unit: String,
    val price: Double,
    val isRise: Boolean,
    val description: String
)
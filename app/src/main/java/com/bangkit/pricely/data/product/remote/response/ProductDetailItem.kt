package com.bangkit.pricely.data.product.remote.response

import com.google.gson.annotations.SerializedName

data class ProductDetailItem(
    @SerializedName("description")
    val description: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("id_category")
    val idCategory: Int?,
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("month")
    val month: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("price")
    val price: Int?,
    @SerializedName("unit")
    val unit: String?,
    @SerializedName("weight")
    val weight: Int?,
    @SerializedName("year")
    val year: Int?
)
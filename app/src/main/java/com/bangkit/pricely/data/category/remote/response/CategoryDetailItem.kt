package com.bangkit.pricely.data.category.remote.response


import com.google.gson.annotations.SerializedName

data class CategoryDetailItem(
    @SerializedName("description")
    val description: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("name")
    val name: String?
)
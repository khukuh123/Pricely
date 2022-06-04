package com.bangkit.pricely.data.product.remote.response

import com.google.gson.annotations.SerializedName

class AllProductCategoryResponse : ArrayList<AllProductCategoryResponseItem>()

data class AllProductCategoryResponseItem(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("price")
    val price: Int?,
    @SerializedName("unit")
    val unit: String?,
    @SerializedName("weight")
    val weight: Int?
)
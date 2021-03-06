package com.bangkit.pricely.data.product.remote.response

import com.google.gson.annotations.SerializedName

class ProductListResponse : ArrayList<ProductItem>()

data class ProductItem(
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
package com.bangkit.pricely.data.product.remote.response

import com.google.gson.annotations.SerializedName

class RecommendationResponse : ArrayList<RecommendationResponseItem>()

data class RecommendationResponseItem(
    @SerializedName("unit")
    val unit: String?,

    @SerializedName("price")
    val price: Int?,

    @SerializedName("image_url")
    val imageUrl: String?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("weight")
    val weight: Int?,

    @SerializedName("id")
    val id: Int?
)
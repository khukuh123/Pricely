package com.bangkit.pricely.data.price.remote.response


import com.google.gson.annotations.SerializedName

class PriceListResponse : ArrayList<PriceItem>()

data class PriceItem(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("id_product")
    val idProduct: Int?,
    @SerializedName("month")
    val month: String?,
    @SerializedName("price")
    val price: Int?,
    @SerializedName("year")
    val year: Int?
)
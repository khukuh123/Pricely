package com.bangkit.pricely.data.product.remote.response


import com.google.gson.annotations.SerializedName

class SuggestionResponseItem : ArrayList<SuggestionItem>()

data class SuggestionItem(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?
)
package com.example.cameraxintegration.model


import com.google.gson.annotations.SerializedName

data class CheckSpoofingBody(
    @SerializedName("imageselect")
    val imageselect: String?
)
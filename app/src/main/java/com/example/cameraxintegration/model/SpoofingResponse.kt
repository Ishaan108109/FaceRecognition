package com.example.cameraxintegration.model


import com.google.gson.annotations.SerializedName

data class SpoofingResponse(
    @SerializedName("message")
    val message: String?,
    @SerializedName("score")
    val score: Double?,
    @SerializedName("status")
    val status: String?
)
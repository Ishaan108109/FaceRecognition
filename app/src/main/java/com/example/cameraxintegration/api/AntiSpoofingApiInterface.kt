package com.example.cameraxintegration.api

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface AntiSpoofingApiInterface {

    @POST("attendance/checkSpoofing/")
    fun checkSpoofing(@Body requestBody: RequestBody): Call<ApiResponse>
}
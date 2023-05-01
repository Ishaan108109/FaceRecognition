package com.example.cameraxintegration.network

import com.example.cameraxintegration.model.CheckSpoofingBody
import com.example.cameraxintegration.model.SpoofingResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("attendance/checkSpoofing")
    suspend fun checkSpoofing(@Body requestBody: CheckSpoofingBody): Response<SpoofingResponse>
}
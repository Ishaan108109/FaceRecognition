package com.example.cameraxintegration.network

import com.example.cameraxintegration.model.CheckSpoofingBody
import com.example.cameraxintegration.model.CheckSpoofingResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("attendance/checkSpoofing/")
    suspend fun isImageSpoofed(@Body checkSpoofingBody: CheckSpoofingBody): Response<CheckSpoofingResponse>

}
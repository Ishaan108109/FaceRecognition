package com.example.cameraxintegration.network

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Client {
    private val gson = GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create()
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://dev.honohr.com/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    val api: ApiService = retrofit.create(ApiService::class.java)
}
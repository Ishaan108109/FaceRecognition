package com.example.cameraxintegration.network

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Client {
    private val gson = GsonBuilder()
        .create()


    private val retrofit = Retrofit.Builder()
        .baseUrl("https://bot.honohr.com/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    val api = retrofit.create(ApiService::class.java)
}
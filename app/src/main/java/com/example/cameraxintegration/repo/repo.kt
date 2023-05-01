package com.example.cameraxintegration.repo

import com.example.cameraxintegration.model.CheckSpoofingBody
import com.example.cameraxintegration.model.SpoofingResponse
import com.example.cameraxintegration.model.UserImageEntity
import com.example.cameraxintegration.network.Client
import com.example.cameraxintegration.network.Resource
import com.example.cameraxintegration.repo.local.AppDb
import com.example.cameraxintegration.repo.remote.BaseRepo

object repo : BaseRepo() {
    val apiService = Client.api
    suspend fun checkSpoofingImage(checkSpoofingBody: CheckSpoofingBody): Resource<SpoofingResponse> {
        return safeApiCall { apiService.checkSpoofing(checkSpoofingBody) }
    }
    suspend fun insertUser(userImageEntity: UserImageEntity) {
        AppDb.getDatabase().userImageDao().insert(userImageEntity)
    }
}
package com.example.cameraxintegration.repo

import com.example.cameraxintegration.model.CheckSpoofingBody
import com.example.cameraxintegration.model.CheckSpoofingResponse
import com.example.cameraxintegration.network.ApiService
import com.example.cameraxintegration.model.UserImageEntity
import com.example.cameraxintegration.network.Resource
import com.example.cameraxintegration.repo.local.AppDb
import com.example.cameraxintegration.repo.remote.BaseRepo

class Repo(private val apiService: ApiService):BaseRepo() {

    suspend fun insertUser(userImageEntity: UserImageEntity) {
        AppDb.getDatabase().userImageDao().insert(userImageEntity)
    }

    suspend fun isImageSpoofed(checkSpoofingBody: CheckSpoofingBody): Resource<CheckSpoofingResponse> {
        return safeApiCall { apiService.isImageSpoofed(checkSpoofingBody) }
    }
}
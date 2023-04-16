package com.example.cameraxintegration.repo.local

import com.example.cameraxintegration.repo.model.UserImageEntity

object repo {

    suspend fun insertUser(userImageEntity: UserImageEntity) {
        AppDb.getDatabase().userImageDao().insert(userImageEntity)
    }
}
package com.example.cameraxintegration.repo.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cameraxintegration.repo.model.UserImageEntity

@Dao
interface UserImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserImageEntity)
}
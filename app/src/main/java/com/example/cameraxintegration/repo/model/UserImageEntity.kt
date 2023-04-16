package com.example.cameraxintegration.repo.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.cameraxintegration.Constants.USER_IMAGE_TABLE

@Entity(tableName = USER_IMAGE_TABLE)
data class UserImageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val imagePath: String?
)
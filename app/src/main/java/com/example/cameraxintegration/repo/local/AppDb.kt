package com.example.cameraxintegration.repo.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cameraxintegration.Constants
import com.example.cameraxintegration.MyApplication
import com.example.cameraxintegration.model.UserImageEntity

@Database(entities = [UserImageEntity::class], version = 1)
abstract class AppDb : RoomDatabase() {
    abstract fun userImageDao(): UserImageDao

    companion object {
        @Volatile
        private var INSTANCE: AppDb? = null
        fun getDatabase(): AppDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    MyApplication.context,
                    AppDb::class.java,
                    Constants.USER_IMAGE_DB
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
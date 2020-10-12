package com.hafidhadhi.submissiontwo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hafidhadhi.submissiontwo.data.local.entity.FavoriteUserEnt

@Database(entities = [FavoriteUserEnt::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteUserDao(): FavoriteUserDao
}
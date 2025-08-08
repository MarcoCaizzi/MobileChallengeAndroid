package com.example.mobilechallengeandroid.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mobilechallengeandroid.data.database.dao.CityDao
import com.example.mobilechallengeandroid.data.database.entities.CityEntity

@Database(entities = [CityEntity::class], version = 1, exportSchema = false)
abstract class CityDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao
}
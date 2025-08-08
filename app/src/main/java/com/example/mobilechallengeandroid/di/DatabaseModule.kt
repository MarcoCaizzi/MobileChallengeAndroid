package com.example.mobilechallengeandroid.di

import android.content.Context
import androidx.room.Room
import com.example.mobilechallengeandroid.data.database.CityDatabase
import com.example.mobilechallengeandroid.data.database.dao.CityDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val DB_NAME = "cities.db"
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext appContext: Context
    ): CityDatabase =
        Room.databaseBuilder(appContext, CityDatabase::class.java, DB_NAME).build()

    @Provides
    fun provideCityDao(db: CityDatabase): CityDao = db.cityDao()
}
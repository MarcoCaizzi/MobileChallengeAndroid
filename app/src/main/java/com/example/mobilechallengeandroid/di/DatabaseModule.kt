package com.example.mobilechallengeandroid.di

import android.content.Context
import androidx.room.Room
import com.example.mobilechallengeandroid.data.local.AppDatabase
import com.example.mobilechallengeandroid.data.local.CityDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext appContext: Context
    ): AppDatabase =
        Room.databaseBuilder(appContext, AppDatabase::class.java, "cities.db").build()

    @Provides
    fun provideCityDao(db: AppDatabase): CityDao = db.cityDao()
}
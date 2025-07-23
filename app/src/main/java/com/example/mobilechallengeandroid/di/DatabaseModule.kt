package com.example.mobilechallengeandroid.di

import android.content.Context
import androidx.room.Room
import com.example.mobilechallengeandroid.data.local.CityDatabase
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
    ): CityDatabase =
        Room.databaseBuilder(appContext, CityDatabase::class.java, "cities.db").build()

    @Provides
    fun provideCityDao(db: CityDatabase): CityDao = db.cityDao()
}
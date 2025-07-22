package com.example.mobilechallengeandroid.di

import android.content.Context
import com.example.mobilechallengeandroid.domain.CityRepository
import com.example.mobilechallengeandroid.domain.CityRepositoryImpl
import com.example.mobilechallengeandroid.data.local.CityDao
import com.example.mobilechallengeandroid.data.remote.weather.WeatherApi
import com.example.mobilechallengeandroid.data.remote.file.FileDownloadApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCityRepository(
        @ApplicationContext context: Context,
        cityDao: CityDao,
        weatherApi: WeatherApi,
        fileDownloadApi: FileDownloadApi
    ): CityRepository {
        return CityRepositoryImpl(context, cityDao, weatherApi, fileDownloadApi)
    }
}
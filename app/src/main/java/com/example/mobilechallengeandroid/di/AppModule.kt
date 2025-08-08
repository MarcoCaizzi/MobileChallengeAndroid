package com.example.mobilechallengeandroid.di

import android.content.Context
import com.example.mobilechallengeandroid.domain.repository.CityListRepository
import com.example.mobilechallengeandroid.data.repository.CityListRepositoryImpl
import com.example.mobilechallengeandroid.data.database.dao.CityDao
import com.example.mobilechallengeandroid.data.network.file.CityListService
import com.example.mobilechallengeandroid.data.network.weather.WeatherService
import com.example.mobilechallengeandroid.domain.repository.FavoriteRepository
import com.example.mobilechallengeandroid.data.repository.FavoriteRepositoryImpl
import com.example.mobilechallengeandroid.domain.repository.WeatherRepository
import com.example.mobilechallengeandroid.data.repository.WeatherRepositoryImpl
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
        cityDao: CityDao,
        cityListService: CityListService
    ): CityListRepository {
        return CityListRepositoryImpl(cityDao,cityListService )
    }

    @Provides
    @Singleton
    fun provideFavoriteRepository(
        @ApplicationContext context: Context
    ): FavoriteRepository {
        return FavoriteRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideWeatherRepository(
        weatherService: WeatherService,
        @ApplicationContext context: Context
    ): WeatherRepository {
        return WeatherRepositoryImpl(weatherService, context)
    }
}
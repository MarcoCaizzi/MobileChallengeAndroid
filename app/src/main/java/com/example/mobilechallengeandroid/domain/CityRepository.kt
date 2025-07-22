package com.example.mobilechallengeandroid.domain

import androidx.paging.PagingData
import com.example.mobilechallengeandroid.data.model.WeatherData
import com.example.mobilechallengeandroid.data.model.City
import kotlinx.coroutines.flow.Flow

interface CityRepository {
    suspend fun toggleFavorite(cityId: Long)
    suspend fun downloadAndFetchCities(jsonUrl: String): List<City>
    suspend fun getFavoriteIds(): Set<Long>
    suspend fun getWeatherForCity(city: City): WeatherData?
    suspend fun searchCitiesByPrefix(prefix: String): List<City>
    fun getPagedCities(prefix: String): Flow<PagingData<City>>
    suspend fun getCitiesByIds(ids: List<Long>): List<City>
    suspend fun getCityById(id: Long): City?
}
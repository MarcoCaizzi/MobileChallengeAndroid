package com.example.mobilechallengeandroid.data.repository

import android.content.Context
import com.example.mobilechallengeandroid.R
import com.example.mobilechallengeandroid.data.network.weather.WeatherService
import com.example.mobilechallengeandroid.domain.model.CityItem
import com.example.mobilechallengeandroid.domain.model.WeatherItem
import com.example.mobilechallengeandroid.domain.repository.WeatherRepository
import com.example.mobilechallengeandroid.domain.model.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherService: WeatherService,
    private val context: Context,
) : WeatherRepository {
    override suspend fun getWeatherForCity(city: CityItem): WeatherItem? = withContext(Dispatchers.IO) {
        val apiKey = context.getString(R.string.weather_api_key)
        return@withContext try {
            val response = weatherService.getWeatherByCity(
                apiKey = apiKey,
                lat = city.coordinates.lat,
                lon = city.coordinates.lon
            )
            response?.toDomain()
        } catch (_: Exception) {
            null
        }
    }
}
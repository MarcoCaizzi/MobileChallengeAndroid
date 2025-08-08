package com.example.mobilechallengeandroid.domain.repository

import com.example.mobilechallengeandroid.domain.model.CityItem
import com.example.mobilechallengeandroid.domain.model.WeatherItem

interface WeatherRepository {
    suspend fun getWeatherForCity(city: CityItem): WeatherItem?
}
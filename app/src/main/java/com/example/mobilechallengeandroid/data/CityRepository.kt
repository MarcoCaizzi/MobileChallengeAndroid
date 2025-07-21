package com.example.mobilechallengeandroid.data

interface CityRepository {
    suspend fun toggleFavorite(cityId: Long)
    suspend fun downloadAndFetchCities(jsonUrl: String): List<City>
    suspend fun getFavoriteIds(): Set<Long>
    suspend fun getWeatherForCity(city: City): WeatherData?
}
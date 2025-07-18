package com.example.mobilechallegeandroid.data

interface CityRepository {
    suspend fun getFavorites(): List<City>
    suspend fun toggleFavorite(cityId: Long)
    suspend fun downloadAndFetchCities(jsonUrl: String): List<City>
}
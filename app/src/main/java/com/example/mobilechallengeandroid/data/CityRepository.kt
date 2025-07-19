package com.example.mobilechallengeandroid.data

interface CityRepository {
    suspend fun getFavorites(): List<City>
    suspend fun toggleFavorite(cityId: Long)
    suspend fun downloadAndFetchCities(jsonUrl: String): List<City>
}
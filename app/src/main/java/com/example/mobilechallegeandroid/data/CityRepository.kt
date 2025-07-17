package com.example.mobilechallegeandroid.data

interface CityRepository {
    suspend fun fetchCities(): List<City>
    suspend fun getFavorites(): List<City>
    suspend fun toggleFavorite(cityId: Long)
}
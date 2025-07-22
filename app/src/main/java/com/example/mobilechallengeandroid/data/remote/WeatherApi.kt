package com.example.mobilechallengeandroid.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("v1/currentConditions:lookup")
    suspend fun getWeather(
        @Query("key") apiKey: String,
        @Query("location.latitude") lat: Double,
        @Query("location.longitude") lon: Double,
        @Query("unitsSystem") units: String = "IMPERIAL"
    ): WeatherApiResponse
}
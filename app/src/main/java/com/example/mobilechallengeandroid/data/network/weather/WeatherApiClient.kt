package com.example.mobilechallengeandroid.data.network.weather

import com.example.mobilechallengeandroid.data.model.WeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiClient {
    @GET("v1/currentConditions:lookup")
    suspend fun getWeatherByCity(
        @Query("key") apiKey: String,
        @Query("location.latitude") lat: Double,
        @Query("location.longitude") lon: Double,
        @Query("unitsSystem") units: String = "IMPERIAL"
    ): Response<WeatherModel>
}
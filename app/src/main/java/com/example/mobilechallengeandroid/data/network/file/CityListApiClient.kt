package com.example.mobilechallengeandroid.data.network.file

import com.example.mobilechallengeandroid.data.model.CityModel
import retrofit2.http.GET
import retrofit2.Response

interface CityListApiClient {
    @GET("cities.json")
    suspend fun getAllCities(): Response<List<CityModel>>
}
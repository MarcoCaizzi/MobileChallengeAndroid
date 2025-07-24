package com.example.mobilechallengeandroid.di

import org.junit.Assert.*
import org.junit.Test

class NetworkModuleTest {

    /**
     * This test checks if the OkHttpClient is correctly provided by the NetworkModule.
     * It ensures that the client can be instantiated and is not null.
     */
    @Test
    fun provideOkHttpClient_returnsClient() {
        val client = NetworkModule.provideOkHttpClient()
        assertNotNull(client)
    }

    /**
     * This test checks if the WeatherApi is correctly provided by the NetworkModule.
     * It ensures that the API can be instantiated and is not null.
     */
    @Test
    fun provideWeatherApi_returnsApi() {
        val client = NetworkModule.provideOkHttpClient()
        val api = NetworkModule.provideWeatherApi(client)
        assertNotNull(api)
    }

    /**
     * This test checks if the FileDownloadApi is correctly provided by the NetworkModule.
     * It ensures that the API can be instantiated and is not null.
     */
    @Test
    fun provideFileDownloadApi_returnsApi() {
        val client = NetworkModule.provideOkHttpClient()
        val api = NetworkModule.provideFileDownloadApi(client)
        assertNotNull(api)
    }
}
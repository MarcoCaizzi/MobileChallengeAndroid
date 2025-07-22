package com.example.mobilechallengeandroid.data.model

data class WeatherData(
    val description: String?,
    val temperature: Double?,
    val temperatureUnit: String?,
    val feelsLike: Double?,
    val humidity: Int?,
    val rainProbability: Int?
)
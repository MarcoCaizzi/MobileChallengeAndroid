package com.example.mobilechallengeandroid.ui.model

import com.example.mobilechallengeandroid.domain.model.WeatherItem

data class CityDetailState(
    val weather: WeatherItem? = null,
    val temperatureCelsius: Double? = null,
    val feelsLikeCelsius: Double? = null,
    val temperatureUnit: String = "°C",
    val mapUrl: String? = null,
    val isLoading: Boolean = false
)
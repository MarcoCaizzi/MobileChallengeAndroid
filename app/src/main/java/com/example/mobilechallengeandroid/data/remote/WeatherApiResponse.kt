package com.example.mobilechallengeandroid.data.remote

data class WeatherApiResponse(
    val weatherCondition: WeatherCondition?,
    val temperature: Temperature?,
    val feelsLikeTemperature: Temperature?,
    val relativeHumidity: Int?,
    val precipitation: Precipitation?
) {
    data class WeatherCondition(val description: Description?)
    data class Description(val text: String?)
    data class Temperature(val degrees: Double?, val unit: String?)
    data class Precipitation(val probability: Probability?)
    data class Probability(val percent: Int?)
}
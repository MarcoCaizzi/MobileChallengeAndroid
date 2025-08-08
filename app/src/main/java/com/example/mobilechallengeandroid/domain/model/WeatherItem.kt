package com.example.mobilechallengeandroid.domain.model
import com.example.mobilechallengeandroid.data.model.WeatherModel
data class WeatherItem(
    val description: String,
    val temperature: Double,
    val temperatureUnit: String,
    val feelsLike: Double,
    val humidity: Int,
    val rainProbability: Int
)

fun WeatherModel.toDomain() = WeatherItem(
    description = weatherCondition?.description?.text ?: "",
    temperature = temperature?.degrees ?: 0.0,
    temperatureUnit = temperature?.unit ?: "",
    feelsLike = feelsLikeTemperature?.degrees ?: 0.0,
    humidity = relativeHumidity ?: 0,
    rainProbability = precipitation?.probability?.percent ?: 0
)
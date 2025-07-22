package com.example.mobilechallengeandroid.data.remote.weather

data class WeatherDto(
    val weatherCondition: WeatherConditionDto?,
    val temperature: TemperatureDto?,
    val feelsLikeTemperature: TemperatureDto?,
    val relativeHumidity: Int?,
    val precipitation: PrecipitationDto?
)

data class WeatherConditionDto(val description: DescriptionDto?)
data class DescriptionDto(val text: String?)
data class TemperatureDto(val degrees: Double?, val unit: String?)
data class PrecipitationDto(val probability: ProbabilityDto?)
data class ProbabilityDto(val percent: Int?)
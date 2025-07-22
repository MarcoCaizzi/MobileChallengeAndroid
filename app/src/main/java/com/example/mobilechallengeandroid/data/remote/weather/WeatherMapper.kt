package com.example.mobilechallengeandroid.data.remote.weather

import com.example.mobilechallengeandroid.data.model.WeatherData

fun WeatherDto.toWeatherData(): WeatherData = WeatherData(
    description = weatherCondition?.description?.text,
    temperature = temperature?.degrees,
    temperatureUnit = temperature?.unit,
    feelsLike = feelsLikeTemperature?.degrees,
    humidity = relativeHumidity,
    rainProbability = precipitation?.probability?.percent
)
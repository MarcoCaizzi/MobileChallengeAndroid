package com.example.mobilechallengeandroid.data.remote

import com.example.mobilechallengeandroid.data.model.WeatherData

fun WeatherApiResponse.toWeatherData(): WeatherData = WeatherData(
    description = weatherCondition?.description?.text,
    temperature = temperature?.degrees,
    temperatureUnit = temperature?.unit,
    feelsLike = feelsLikeTemperature?.degrees,
    humidity = relativeHumidity,
    rainProbability = precipitation?.probability?.percent
)
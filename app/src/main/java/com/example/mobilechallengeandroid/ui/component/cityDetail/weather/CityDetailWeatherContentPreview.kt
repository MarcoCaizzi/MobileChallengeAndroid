package com.example.mobilechallengeandroid.ui.component.cityDetail.weather

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.mobilechallengeandroid.domain.model.WeatherItem

@Preview(showBackground = true)
@Composable
fun PreviewCityDetailWeatherContent() {
    CityDetailWeatherContent(
        weather = WeatherItem(
            description = "Clear sky",
            temperature = 25.0,
            temperatureUnit = "°C",
            feelsLike = 24.0,
            humidity = 60,
            rainProbability = 10
        ),
        temperatureCelsius = 25.0,
        feelsLikeCelsius = 24.0,
        temperatureUnit = "°C"
    )
}
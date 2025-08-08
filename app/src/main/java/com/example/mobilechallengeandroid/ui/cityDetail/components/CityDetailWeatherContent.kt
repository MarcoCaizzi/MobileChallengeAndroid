package com.example.mobilechallengeandroid.ui.cityDetail.components

import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mobilechallengeandroid.domain.model.WeatherItem
import java.util.Locale

@Composable
fun CityDetailWeatherContent(
    weather: WeatherItem?,
    temperatureCelsius: Double?,
    feelsLikeCelsius: Double?,
    temperatureUnit: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Weather", style = MaterialTheme.typography.titleMedium)
            Text("Description: ${weather?.description ?: "N/A"}")
            Text(
                "Temperature: " +
                        (temperatureCelsius?.let {
                            String.format(Locale.getDefault(), "%.1f", it)
                        } ?: "N/A") + temperatureUnit
            )
            Text(
                "Feels like: " +
                        (feelsLikeCelsius?.let {
                            String.format(Locale.getDefault(), "%.1f", it)
                        } ?: "N/A") + temperatureUnit
            )
            Text("Humidity: ${weather?.humidity ?: "N/A"}%")
            Text("Rain probability: ${weather?.rainProbability ?: "N/A"}%")
        }
    }
}


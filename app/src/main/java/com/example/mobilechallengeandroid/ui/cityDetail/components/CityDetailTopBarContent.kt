package com.example.mobilechallengeandroid.ui.cityDetail.components

import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.mobilechallengeandroid.domain.model.CityItem
import com.example.mobilechallengeandroid.domain.model.CoordinatesItem
import com.example.mobilechallengeandroid.domain.model.WeatherItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityDetailTopBarContent(title: String, onBack: () -> Unit) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCityDetailTopBarContent() {
    CityDetailTopBarContent(title = "Buenos Aires, Argentina", onBack = {})
}

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

@Preview(showBackground = true)
@Composable
fun PreviewCityDetailCoordinatesContent() {
    CityDetailCoordinatesContent(
        cityItem = CityItem(
            id = 1L,
            name = "Buenos Aires",
            country = "Argentina",
            coordinates = CoordinatesItem(-34.6037, -58.3816)
        )
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCityDetailMapContent() {
    CityDetailMapContent(mapUrl = "https://maps.googleapis.com/maps/api/staticmap?center=-34.6037,-58.3816&zoom=10&size=600x300")
}

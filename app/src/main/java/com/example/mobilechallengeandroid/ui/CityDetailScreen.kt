package com.example.mobilechallengeandroid.ui

import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mobilechallengeandroid.data.City
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityDetailScreen(
    city: City,
    onBack: () -> Unit,
    viewModel: CityDetailViewModel = viewModel()
) {
    LaunchedEffect(city) {
        viewModel.loadWeather(city)
    }
    val weather by viewModel.weather.collectAsState()
    val tempC by viewModel.temperatureCelsius.collectAsState()
    val feelsLikeC by viewModel.feelsLikeCelsius.collectAsState()
    val tempUnit by viewModel.temperatureUnit.collectAsState()


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${city.name}, ${city.country}") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("General information", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(Modifier.height(8.dp))
                    Text("ID: ${city.id}")
                    Text("Country: ${city.country}")
                    Text("Coordinates: Lat ${city.coord.lat}, Lon ${city.coord.lon}")
                }
            }
            Card {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Current weather", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(Modifier.height(8.dp))
                    if (weather != null) {
                        Text("Weather description: ${weather!!.description}")
                        Text("Temperature: ${tempC?.let { "%.1f".format(it) } ?: "N/A"} $tempUnit")
                        Text("Wind chill: ${feelsLikeC?.let { "%.1f".format(it) } ?: "N/A"} $tempUnit")
                        Text("Humidity: ${weather!!.humidity}%")
                        Text("Chance of rain: ${weather!!.rainProbability ?: "N/A"}%")
                    } else {
                        Text("Charging...")
                    }
                }
            }
        }
    }
}
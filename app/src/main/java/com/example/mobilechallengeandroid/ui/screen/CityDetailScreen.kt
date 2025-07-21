package com.example.mobilechallengeandroid.ui.screen

import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mobilechallengeandroid.data.City
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import coil.compose.AsyncImage
import androidx.compose.ui.platform.LocalContext
import com.example.mobilechallengeandroid.ui.viewmodel.CityDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityDetailScreen(
    city: City,
    onBack: () -> Unit,
    viewModel: CityDetailViewModel = viewModel()
) {
    val context = LocalContext.current
    LaunchedEffect(city) {
        viewModel.loadMapUrl(context, city)
        viewModel.loadWeather(city)
    }

    val mapUrl by viewModel.mapUrl.collectAsState()
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
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            if (weather == null) {
                CircularProgressIndicator()
            } else {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("City", style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(8.dp))
                            Text("Name: ${city.name}")
                            Text("Country: ${city.country}")
                            Text("Latitude: ${city.coord.lat}")
                            Text("Longitude: ${city.coord.lon}")
                        }
                    }
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Weather", style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(8.dp))
                            Text("Description: ${weather?.description ?: "N/A"}")
                            Text("Temperature: ${tempC?.let { "%.1f".format(it) } ?: "N/A"} $tempUnit")
                            Text("Feels like: ${feelsLikeC?.let { "%.1f".format(it) } ?: "N/A"} $tempUnit")
                            Text("Humidity: ${weather?.humidity ?: "N/A"}%")
                            Text("Rain probability: ${weather?.rainProbability ?: "N/A"}%")
                        }
                    }
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            AsyncImage(
                                model = mapUrl,
                                contentDescription = "Map image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
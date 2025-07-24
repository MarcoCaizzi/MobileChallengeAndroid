package com.example.mobilechallengeandroid.ui.screen

import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mobilechallengeandroid.data.model.City
import androidx.compose.ui.Alignment
import coil.compose.AsyncImage
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mobilechallengeandroid.ui.viewmodel.CityDetailViewModel
import com.example.mobilechallengeandroid.ui.viewmodel.CityListViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityDetailScreen(
    cityId: Long,
    onBack: () -> Unit,
    cityListViewModel: CityListViewModel = hiltViewModel(),
    viewModel: CityDetailViewModel = hiltViewModel()
) {

    val city by produceState<City?>(initialValue = null, cityId) {
        value = cityListViewModel.getCityById(cityId)
    }

    LaunchedEffect(city) {
        city?.let { viewModel.loadCityDetails(it) }
    }

    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(city?.let { "${it.name}, ${it.country}" } ?: "Detailers") },
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
            if (state.isLoading) {
                CircularProgressIndicator()
            } else if (city != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    state.mapUrl?.let { url ->
                        AsyncImage(
                            model = url,
                            contentDescription = "Maps",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                    }
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Weather", style = MaterialTheme.typography.titleMedium)
                            Text("Description: ${state.weather?.description ?: "N/A"}")
                            Text(
                                "Temperature: ${
                                    state.temperatureCelsius?.let {
                                        String.format(
                                            Locale.getDefault(),
                                            "%.1f",
                                            it
                                        )
                                    } ?: "N/A"
                                }${state.temperatureUnit}"
                            )
                            Text(
                                "Feels like: ${
                                    state.feelsLikeCelsius?.let {
                                        String.format(
                                            Locale.getDefault(),
                                            "%.1f",
                                            it
                                        )
                                    } ?: "N/A"
                                }${state.temperatureUnit}"
                            )
                            Text("Humidity: ${state.weather?.humidity ?: "N/A"}%")
                            Text("Rain probability:: ${state.weather?.rainProbability ?: "N/A"}%")
                        }
                    }
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Coordinates", style = MaterialTheme.typography.titleMedium)
                            Text("Latitude: ${city!!.coord.lat}")
                            Text("Longitude: ${city!!.coord.lon}")
                        }
                    }
                }
            }
        }
    }
}
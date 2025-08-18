package com.example.mobilechallengeandroid.ui.feature.cityDetail

import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mobilechallengeandroid.domain.model.CityItem
import androidx.compose.ui.Alignment
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mobilechallengeandroid.ui.model.CityDetailState
import com.example.mobilechallengeandroid.ui.feature.cityList.CityListViewModel
import com.example.mobilechallengeandroid.ui.component.cityDetail.weather.CityDetailWeatherContent
import com.example.mobilechallengeandroid.ui.component.cityDetail.coordinantes.CityDetailCoordinatesContent
import com.example.mobilechallengeandroid.ui.component.cityDetail.map.CityDetailMapContent
import com.example.mobilechallengeandroid.ui.utils.getStaticMapUrl
import androidx.compose.ui.platform.LocalContext
import com.example.mobilechallengeandroid.ui.component.header.TopBarContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityDetailScreenContainer(
    cityId: Long,
    onBack: () -> Unit,
    cityListViewModel: CityListViewModel = hiltViewModel(),
    viewModel: CityDetailViewModel = hiltViewModel()
) {
    val cityItemState = produceState<CityItem?>(initialValue = null, cityId) {
        value = cityListViewModel.getCityById(cityId)
    }
    val cityItem = cityItemState.value
    val state = viewModel.state.collectAsState().value
    val context = LocalContext.current
    val mapUrl = cityItem?.let { getStaticMapUrl(context, it) }

    LaunchedEffect(cityItem) {
        cityItem?.let { viewModel.loadCityDetails(it, mapUrl) }
    }

    CityDetailScreen(
        cityItem = cityItem,
        state = state,
        onBack = onBack,
        onLoadCityDetails = { city, url -> viewModel.loadCityDetails(city, url) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityDetailScreen(
    cityItem: CityItem?,
    state: CityDetailState,
    onBack: () -> Unit,
    onLoadCityDetails: (CityItem, String?) -> Unit
) {
    val context = LocalContext.current
    val mapUrl = cityItem?.let { getStaticMapUrl(context, it) }

    LaunchedEffect(cityItem) {
        cityItem?.let { onLoadCityDetails(it, mapUrl) }
    }

    Scaffold(
        topBar = {
            TopBarContent(
                title = cityItem?.let { "${it.name}, ${it.country}" } ?: "Detailers",
                onBack = onBack
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
            } else if (cityItem != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CityDetailMapContent(state.mapUrl)
                    CityDetailWeatherContent(
                        weather = state.weather,
                        temperatureCelsius = state.temperatureCelsius,
                        feelsLikeCelsius = state.feelsLikeCelsius,
                        temperatureUnit = state.temperatureUnit
                    )
                    CityDetailCoordinatesContent(cityItem)
                }
            }
        }
    }
}
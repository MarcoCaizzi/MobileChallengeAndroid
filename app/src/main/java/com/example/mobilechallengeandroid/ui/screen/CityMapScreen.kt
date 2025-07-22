package com.example.mobilechallengeandroid.ui.screen

import android.content.res.Configuration
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import com.example.mobilechallengeandroid.data.model.City
import com.example.mobilechallengeandroid.ui.viewmodel.CityListViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityMapScreen(
    cityId: Long,
    onBack: () -> Unit,
    viewModel: CityListViewModel = hiltViewModel()
) {
    val city by produceState<City?>(initialValue = null, key1 = cityId) {
        value = viewModel.getCityById(cityId)
    }
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    city?.let { city ->
        val cityPosition = LatLng(city.coord.lat, city.coord.lon)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(cityPosition, 10f)
        }

        LaunchedEffect(cityPosition) {
            cameraPositionState.animate(CameraUpdateFactory.newCameraPosition(
                CameraPosition.fromLatLngZoom(cityPosition, 10f)
            ))
        }

        Scaffold(
            topBar = {
                if (isPortrait) {
                    TopAppBar(
                        title = { Text("Back") },
                        navigationIcon = {
                            IconButton(onClick = onBack) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        }
                    )
                }
            }
        ) { padding ->
            GoogleMap(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                Marker(
                    state = remember { MarkerState(position = cityPosition) },
                    title = city.name,
                    snippet = city.country
                )
            }
        }
    }
}
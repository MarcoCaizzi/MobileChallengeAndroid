package com.example.mobilechallengeandroid.ui.map

import android.content.res.Configuration
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import com.example.mobilechallengeandroid.domain.model.CityItem
import com.example.mobilechallengeandroid.ui.cityList.CityListViewModel
import com.example.mobilechallengeandroid.ui.map.components.TopBarContent
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment

@Composable
fun CityMapMarkerContent(city: CityItem) {
    val cityPosition = LatLng(city.coordinates.lat, city.coordinates.lon)
    Marker(
        state = remember { MarkerState(position = cityPosition) },
        title = city.name,
        snippet = city.country
    )
}

@Composable
fun CityMapGoogleMapContent(
    city: CityItem,
    cameraPositionState: com.google.maps.android.compose.CameraPositionState
) {
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        CityMapMarkerContent(city)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityMapScreen(
    city: CityItem?,
    onBack: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    if (city == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No city selected")
        }
        return
    }

    val cityPosition = LatLng(city.coordinates.lat, city.coordinates.lon)
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
                TopBarContent(
                    onBack = onBack,
                    title = city.name,
                    contentDescription = "Back"
                )
            }
        }
    ) { padding ->
        Box(modifier = Modifier
            .padding(padding)
            .fillMaxSize()) {
            CityMapGoogleMapContent(city = city, cameraPositionState = cameraPositionState)
        }
    }
}

@Composable
fun CityMapScreenContainer(
    cityId: Long?,
    cityListViewModel: CityListViewModel,
    onBack: () -> Unit = {}
) {
    var city by remember(cityId) { mutableStateOf<CityItem?>(null) }
    LaunchedEffect(cityId) {
        city = cityId?.let { cityListViewModel.getCityById(it) }
    }
    CityMapScreen(
        city = city,
        onBack = onBack
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCityMapScreen() {
    val city = CityItem(
        id = 1L,
        name = "Buenos Aires",
        country = "Argentina",
        coordinates = com.example.mobilechallengeandroid.domain.model.CoordinatesItem( -34.6037, -58.3816 )
    )
    CityMapScreen(city = city, onBack = {})
}

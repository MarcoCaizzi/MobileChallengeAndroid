package com.example.mobilechallengeandroid.ui.feature.cityMap

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import com.example.mobilechallengeandroid.domain.model.CityItem
import com.example.mobilechallengeandroid.ui.component.header.TopBarContent
import com.example.mobilechallengeandroid.ui.component.cityMap.CityMapGoogleMapContent
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun CityMapScreen(
    city: CityItem?,
    onBack: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    if (!isPortrait) {
        onBack()
        return
    }
    if (city == null) {
        Scaffold(
            topBar = {
                TopBarContent(
                    onBack = onBack,
                    title = "City not found",
                    contentDescription = "Back"
                )
            }
        ) { padding ->
            Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                // Mensaje de error o pantalla vacía
            }
        }
        return
    }
    val cityPosition = LatLng(city.coordinates.lat, city.coordinates.lon)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(cityPosition, 10f)
    }
    Scaffold(
        topBar = {
            TopBarContent(
                onBack = onBack,
                title = city.name,
                contentDescription = "Back"
            )
        }
    ) { padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {
            CityMapGoogleMapContent(city = city, cameraPositionState = cameraPositionState)
        }
    }
}

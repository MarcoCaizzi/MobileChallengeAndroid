package com.example.mobilechallengeandroid.ui.component.cityMap

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.mobilechallengeandroid.domain.model.CityItem
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun CityMapInteractiveContent(
    city: CityItem,
    modifier: Modifier = Modifier,
    zoom: Float = 10f
) {
    val cityPosition = LatLng(city.coordinates.lat, city.coordinates.lon)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(cityPosition, zoom)
    }
    CityMapGoogleMapContent(
        city = city,
        cameraPositionState = cameraPositionState
    )
}


package com.example.mobilechallengeandroid.ui.component.cityMap

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.mobilechallengeandroid.domain.model.CityItem
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap

@Composable
fun CityMapGoogleMapContent(
    city: CityItem,
    cameraPositionState: CameraPositionState
) {
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        CityMapMarkerContent(city)
    }
}
package com.example.mobilechallengeandroid.ui.component.cityMap

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.mobilechallengeandroid.domain.model.CityItem
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

@Composable
fun CityMapMarkerContent(city: CityItem) {
    val cityPosition = LatLng(city.coordinates.lat, city.coordinates.lon)
    Marker(
        state = remember { MarkerState(position = cityPosition) },
        title = city.name,
        snippet = city.country
    )
}

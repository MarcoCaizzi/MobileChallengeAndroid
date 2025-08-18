package com.example.mobilechallengeandroid.ui.component.cityDetail.coordinantes

import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mobilechallengeandroid.domain.model.CityItem

@Composable
fun CityDetailCoordinatesContent(cityItem: CityItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Coordinates", style = MaterialTheme.typography.titleMedium)
            Text("Latitude: ${cityItem.coordinates.lat}")
            Text("Longitude: ${cityItem.coordinates.lon}")
        }
    }
}


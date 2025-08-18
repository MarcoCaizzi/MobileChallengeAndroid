package com.example.mobilechallengeandroid.ui.component.cityDetail.map

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun CityDetailMapContent(mapUrl: String?) {
    mapUrl?.let { url ->
        AsyncImage(
            model = url,
            contentDescription = "Maps",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
    }
}

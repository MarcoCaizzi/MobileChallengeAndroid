package com.example.mobilechallengeandroid.ui.cityList.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobilechallengeandroid.domain.model.CityItem

@Composable
fun RowCityContent(
    city: CityItem,
    isFavorite: Boolean,
    isLandscape: Boolean,
    onFavoriteClick: (Long) -> Unit,
    onDetailsClick: (CityItem) -> Unit,
    onCityClick: (Long) -> Unit
) {
    val backgroundColor = when {
        city.id == 2L -> Color(0xFFB3E5FC)
        else -> Color.Transparent
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .clickable { onCityClick(city.id) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = "${city.name}, ${city.country}", fontSize = 20.sp)
            Text(
                text = "Lat: ${city.coordinates.lat}, Lon: ${city.coordinates.lon}",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
        IconButton(onClick = { onFavoriteClick(city.id) }) {
            val icon = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder
            Icon(imageVector = icon, contentDescription = "Favorite")
        }
        if (!isLandscape) {
            androidx.compose.material3.Button(
                onClick = { onDetailsClick(city) },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Info")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRowCityContent() {
    RowCityContent(
        city = CityItem(1, "Buenos Aires", "Argentina", com.example.mobilechallengeandroid.domain.model.CoordinatesItem(1.0, 2.0)),
        isFavorite = true,
        isLandscape = false,
        onFavoriteClick = {},
        onDetailsClick = {},
        onCityClick = {}
    )
}



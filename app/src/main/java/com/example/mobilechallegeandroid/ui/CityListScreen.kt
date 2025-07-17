package com.example.mobilechallegeandroid.ui

import androidx.compose.foundation.clickable
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder


@Composable
fun CityListScreen(viewModel: CityListViewModel, onCityClick: (Long) -> Unit) {
    val filter by viewModel.filter.collectAsState()
    val cities by viewModel.cities.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = filter,
            onValueChange = { viewModel.onFilterChange(it) },
            label = { Text("filter") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(cities) { city ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { onCityClick(city.id) },
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("${city.name}, ${city.country}")
                    IconButton(onClick = { viewModel.onFavoriteClick(city.id) }) {
                        if (city.isFavorite) {
                            Icon(Icons.Filled.Favorite, contentDescription = "Favorite")
                        } else {
                            Icon(Icons.Outlined.FavoriteBorder, contentDescription = "Not favorite")
                        }
                    }
                }
                HorizontalDivider()
            }
        }
    }
}
package com.example.mobilechallegeandroid.ui

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.ui.tooling.preview.Preview
import com.example.mobilechallegeandroid.data.City
import com.example.mobilechallegeandroid.data.Coord
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Preview(
    showBackground = true,
    device = "spec:width=1344px,height=2992px,dpi=480,cutout=corner",
    name = "test"
)
@Composable
fun CityListScreenPreview() {
    val cities = listOf(
        City(1, "Madrid", "ES", Coord(-3.7038, 40.4168), false),
        City(2, "Paris", "FR", Coord(2.3522, 48.8566), true)
    )
    CityListContent(
        filter = "",
        cities = cities,
        onFilterChange = {},
        onCityClick = {},
        onFavoriteClick = {}
    )
}

@Composable
fun CityListScreen(
    viewModel: CityListViewModel,
    onCityClick: (Long) -> Unit
) {
    val filter by viewModel.filter.collectAsState()
    val cities by viewModel.cities.collectAsState()

    CityListContent(
        filter = filter,
        cities = cities,
        onFilterChange = viewModel::onFilterChange,
        onCityClick = onCityClick,
        onFavoriteClick = viewModel::onFavoriteClick
    )
}

@Composable
fun CityListContent(
    filter: String,
    cities: List<City>,
    onFilterChange: (String) -> Unit,
    onCityClick: (Long) -> Unit,
    onFavoriteClick: (Long) -> Unit
) {
    Column(modifier = Modifier.padding(8.dp)) {
        OutlinedTextField(
            value = filter,
            onValueChange = onFilterChange,
            label = { Text("filter") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(25.dp),
            leadingIcon = {
                Icon(Icons.Filled.Search, contentDescription = "filter_icon")
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(cities) { index, city ->
                val bgColor = if (index % 2 == 0) Color.White else Color(0xFFCBCACA)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(bgColor)
                        .padding(vertical = 8.dp)
                        .clickable { onCityClick(city.id) },
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 24.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
                    ) {
                        Text("${city.name}, ${city.country}", fontSize = 20.sp)
                    }
                    IconButton(onClick = { onFavoriteClick(city.id) }) {
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
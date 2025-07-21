package com.example.mobilechallengeandroid.ui

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
import com.example.mobilechallengeandroid.data.City
import com.example.mobilechallengeandroid.data.Coord
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.ui.Alignment

@Preview(
    showBackground = true,
    device = "spec:width=1344px,height=2992px,dpi=480,cutout=corner",
    name = "test"
)
@Composable
fun CityListScreenPreview() {
    val cities = listOf(
        City(1, "Madrid", "ES", Coord(-3.7038, 40.4168)),
        City(2, "Paris", "FR", Coord(2.3522, 48.8566))
    )
    val favoriteIds = setOf<Long>(1)
    CityListContent(
        filter = "",
        cities = cities,
        favoriteIds = favoriteIds,
        showOnlyFavorites = false,
        onShowOnlyFavoritesChange = {},
        onFilterChange = {},
        onCityClick = {},
        onFavoriteClick = {},
        onDetailsClick = { city ->
            println("Navigate to details of ${city.name}")
        }
    )
}

@Composable
fun CityListScreen(
    viewModel: CityListViewModel,
    onCityClick: (Long) -> Unit,
    navController: NavHostController
) {
    val filter by viewModel.filter.collectAsState()
    val cities by viewModel.cities.collectAsState()
    val favoriteIds by viewModel.favoriteIds.collectAsState()
    val showOnlyFavorites by viewModel.showOnlyFavorites.collectAsState()
    val configuration = LocalConfiguration.current
    val isLandscape =
        configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    var selectedCity by remember(cities) { mutableStateOf(cities.firstOrNull()) }

    if (isLandscape) {
        Row(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.weight(1f)) {
                CityListContent(
                    filter = filter,
                    cities = cities,
                    favoriteIds = favoriteIds,
                    showOnlyFavorites = showOnlyFavorites,
                    onShowOnlyFavoritesChange = viewModel::onShowOnlyFavoritesChange,
                    onFilterChange = viewModel::onFilterChange,
                    onCityClick = { cityId ->
                        selectedCity = cities.find { it.id == cityId }
                    },
                    onFavoriteClick = viewModel::onFavoriteClick,
                    onDetailsClick = { city ->
                        navController.navigate("cityData/${city.id}")
                    }
                )
            }
            Box(modifier = Modifier.weight(1f)) {
                selectedCity?.let { city ->
                    CityMapScreen(city, onBack = { })
                }
            }
        }
    } else {
        CityListContent(
            filter = filter,
            cities = cities,
            favoriteIds = favoriteIds,
            showOnlyFavorites = showOnlyFavorites,
            onShowOnlyFavoritesChange = viewModel::onShowOnlyFavoritesChange,
            onFilterChange = viewModel::onFilterChange,
            onCityClick = onCityClick,
            onFavoriteClick = viewModel::onFavoriteClick,
            onDetailsClick = { city ->
                navController.navigate("cityData/${city.id}")
            }
        )
    }
}

@Composable
fun CityListContent(
    filter: String,
    cities: List<City>,
    favoriteIds: Set<Long>,
    showOnlyFavorites: Boolean,
    onShowOnlyFavoritesChange: (Boolean) -> Unit,
    onFilterChange: (String) -> Unit,
    onCityClick: (Long) -> Unit,
    onFavoriteClick: (Long) -> Unit,
    onDetailsClick: (City) -> Unit,
) {
    Column(modifier = Modifier.padding(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = filter,
                onValueChange = onFilterChange,
                label = { Text("filter") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(25.dp),
                leadingIcon = {
                    Icon(Icons.Filled.Search, contentDescription = "filter_icon")
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Checkbox(
                checked = showOnlyFavorites,
                onCheckedChange = onShowOnlyFavoritesChange
            )
            Text("Solo favoritos")
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(cities) { index, city ->
                val isFavorite = favoriteIds.contains(city.id)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(if (index % 2 == 0) Color.White else Color(0xFFCBCACA))
                        .padding(vertical = 8.dp)
                        .clickable { onCityClick(city.id) },
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 24.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
                    ) {
                        Text("${city.name}, ${city.country}", fontSize = 20.sp)
                        Text("Lat: ${city.coord.lat}, Lon: ${city.coord.lon}", fontSize = 14.sp, color = Color.Gray)
                    }
                    IconButton(onClick = { onFavoriteClick(city.id) }) {
                        if (isFavorite) {
                            Icon(Icons.Filled.Favorite, contentDescription = "Favorite")
                        } else {
                            Icon(Icons.Outlined.FavoriteBorder, contentDescription = "Not favorite")
                        }
                    }
                    Button(
                        onClick = { onDetailsClick(city) },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Info")
                    }
                }
                HorizontalDivider()
            }
        }
    }
}
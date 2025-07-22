package com.example.mobilechallengeandroid.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mobilechallengeandroid.data.model.City
import com.example.mobilechallengeandroid.ui.viewmodel.CityListViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.compose.animation.animateColorAsState

@Composable
fun CityListScreen(
    onCityClick: (Long) -> Unit,
    navController: NavHostController,
    viewModel: CityListViewModel = hiltViewModel(),
    isLandscape: Boolean = false,
) {
    val selectedCityId by viewModel.selectedCityId.collectAsState()
    CityListContent(
        viewModel = viewModel,
        onCityClick = onCityClick,
        onFavoriteClick = { viewModel.onFavoriteClick(it) },
        onDetailsClick = { city ->
            if (!isLandscape) {
                navController.navigate("cityData/${city.id}")
            }
        },
        isLandscape = isLandscape,
        selectedCityId = selectedCityId
    )
}

@Composable
fun CityListContent(
    viewModel: CityListViewModel = hiltViewModel(),
    onCityClick: (Long) -> Unit,
    onFavoriteClick: (Long) -> Unit,
    onDetailsClick: (City) -> Unit,
    isLandscape: Boolean = false,
    selectedCityId: Long? = null
) {
    val filter by viewModel.filter.collectAsState()
    val favoriteIds by viewModel.favoriteIds.collectAsState()
    val showOnlyFavorites by viewModel.showOnlyFavorites.collectAsState()
    val pagedCities = viewModel.pagedCities.collectAsLazyPagingItems()

    Column(modifier = Modifier.padding(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = filter,
                onValueChange = viewModel::onFilterChange,
                label = { Text("filter") },
                modifier = Modifier.weight(1f),
                shape = MaterialTheme.shapes.large,
                leadingIcon = {
                    Icon(Icons.Filled.Search, contentDescription = "filter_icon")
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Checkbox(
                checked = showOnlyFavorites,
                onCheckedChange = viewModel::onShowOnlyFavoritesChange
            )
            Text("Favorites only")
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (pagedCities.itemCount == 0 && pagedCities.loadState.refresh is androidx.paging.LoadState.Loading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(
                    count = pagedCities.itemCount,
                    key = { index -> pagedCities[index]?.id ?: index }
                ) { index ->
                    val city = pagedCities[index]
                    if (city != null) {
                        val isFavorite = favoriteIds.contains(city.id)
                        val isSelected = city.id == selectedCityId
                        val targetColor = when {
                            isSelected -> Color(0xFFB3E5FC)
                            index % 2 == 0 -> Color.White
                            else -> Color(0xFFCBCACA)
                        }
                        val backgroundColor by animateColorAsState(targetColor)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(backgroundColor)
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
                                Text(
                                    "Lat: ${city.coord.lat}, Lon: ${city.coord.lon}",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            }
                            IconButton(onClick = { onFavoriteClick(city.id) }) {
                                if (isFavorite) {
                                    Icon(Icons.Filled.Favorite, contentDescription = "Favorite")
                                } else {
                                    Icon(
                                        Icons.Outlined.FavoriteBorder,
                                        contentDescription = "Not favorite"
                                    )
                                }
                            }
                            if (!isLandscape) {
                                Button(
                                    onClick = { onDetailsClick(city) },
                                    modifier = Modifier.padding(end = 8.dp)
                                ) {
                                    Text("Info")
                                }
                            }
                        }
                        HorizontalDivider(
                            Modifier,
                            DividerDefaults.Thickness,
                            DividerDefaults.color
                        )
                    }
                }
                if (pagedCities.loadState.append is androidx.paging.LoadState.Loading) {
                    item { CircularProgressIndicator() }
                } else if (pagedCities.loadState.append is androidx.paging.LoadState.Error) {
                    item { Text("Error loading cities") }
                }
            }
        }
    }
}
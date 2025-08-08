package com.example.mobilechallengeandroid.ui.cityList

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.LoadState
import com.example.mobilechallengeandroid.domain.model.CityItem
import com.example.mobilechallengeandroid.domain.model.CoordinatesItem
import com.example.mobilechallengeandroid.ui.cityList.components.CityListContent
import com.example.mobilechallengeandroid.ui.cityList.components.FilterCityContent

@Composable
fun CityListScreen(
    filter: String,
    favoriteIds: Set<Long>,
    showOnlyFavorites: Boolean,
    pagedCities: androidx.paging.compose.LazyPagingItems<CityItem>,
    isLandscape: Boolean,
    onCityClick: (Long) -> Unit,
    onFavoriteClick: (Long) -> Unit,
    onDetailsClick: (CityItem) -> Unit,
    onFilterChange: (String) -> Unit,
    onShowOnlyFavoritesChange: (Boolean) -> Unit
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(WindowInsets.systemBars.asPaddingValues())
        .padding(8.dp)) {
        FilterCityContent(
            filter = filter,
            showOnlyFavorites = showOnlyFavorites,
            onFilterChange = onFilterChange,
            onShowOnlyFavoritesChange = onShowOnlyFavoritesChange
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (pagedCities.itemCount == 0 && pagedCities.loadState.refresh is LoadState.Loading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            CityListContent(
                cities = List(pagedCities.itemCount) { pagedCities[it] }.filterNotNull(),
                favoriteIds = favoriteIds,
                onCityClick = onCityClick,
                onFavoriteClick = onFavoriteClick,
                onDetailsClick = onDetailsClick,
                isLandscape = isLandscape
            )
            if (pagedCities.loadState.append is LoadState.Loading) {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (pagedCities.loadState.append is LoadState.Error) {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text("Error loading cities")
                }
            }
        }
    }
}

@Composable
fun CityListScreenContainer(
    navController: NavHostController,
    cityListViewModel: CityListViewModel,
    isLandscape: Boolean = false
) {
    val filter by cityListViewModel.filter.collectAsState()
    val favoriteIds by cityListViewModel.favoriteIds.collectAsState()
    val showOnlyFavorites by cityListViewModel.showOnlyFavorites.collectAsState()
    val pagedCities = cityListViewModel.pagedCities.collectAsLazyPagingItems()

    CityListScreen(
        filter = filter,
        favoriteIds = favoriteIds,
        showOnlyFavorites = showOnlyFavorites,
        pagedCities = pagedCities,
        isLandscape = isLandscape,
        onCityClick = { cityId ->
            cityListViewModel.selectCity(cityId)
            if (!isLandscape) {
                navController.navigate("cityDetail/$cityId")
            }
        },
        onFavoriteClick = cityListViewModel::onFavoriteClick,
        onDetailsClick = { city ->
            if (!isLandscape) {
                navController.navigate("cityData/${city.id}")
            }
        },
        onFilterChange = cityListViewModel::onFilterChange,
        onShowOnlyFavoritesChange = cityListViewModel::onShowOnlyFavoritesChange
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCityListScreen() {
    val cities = listOf(
        CityItem(1, "Buenos Aires", "Argentina", CoordinatesItem(1.0, 2.0)),
        CityItem(2, "Madrid", "España", CoordinatesItem(3.0, 4.0))
    )
    val favoriteIds = setOf(1L)
    val filter = ""
    val showOnlyFavorites = false

    Column(modifier = Modifier.padding(8.dp)) {
        FilterCityContent(
            filter = filter,
            showOnlyFavorites = showOnlyFavorites,
            onFilterChange = {},
            onShowOnlyFavoritesChange = {}
        )
        Spacer(modifier = Modifier.height(16.dp))
        CityListContent(
            cities = cities,
            favoriteIds = favoriteIds,
            onCityClick = {},
            onFavoriteClick = {},
            onDetailsClick = {},
            isLandscape = false
        )
    }
}

package com.example.mobilechallengeandroid.ui.feature.cityList

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalConfiguration
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.mobilechallengeandroid.ui.feature.MainCoordinator

@Composable
fun CityListRoute(
    coordinator: MainCoordinator
) {
    val cityListViewModel: CityListViewModel = hiltViewModel()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
    val filter = cityListViewModel.filter.collectAsState().value
    val favoriteIds = cityListViewModel.favoriteIds.collectAsState().value
    val showOnlyFavorites = cityListViewModel.showOnlyFavorites.collectAsState().value
    val pagedCities = cityListViewModel.pagedCities.collectAsLazyPagingItems()
    val selectedCityId = cityListViewModel.selectedCityId.collectAsState().value
    val actions = rememberCityListAction(coordinator)

    CityListScreen(
        cities = List(pagedCities.itemCount) { pagedCities[it] }.filterNotNull(),
        favoriteIds = favoriteIds,
        filter = filter,
        showOnlyFavorites = showOnlyFavorites,
        isLandscape = isLandscape,
        selectedCityId = selectedCityId,
        actions = actions,
        onCityClick = { cityId ->
            cityListViewModel.selectCity(cityId)
            actions.onCityRowClicked(cityId)
        },
        onFavoriteClick = cityListViewModel::onFavoriteClick,
        onDetailsClick = { city -> actions.onCityDetailClicked(city.id) },
        onFilterChange = cityListViewModel::onFilterChange,
        onShowOnlyFavoritesChange = cityListViewModel::onShowOnlyFavoritesChange
    )
}
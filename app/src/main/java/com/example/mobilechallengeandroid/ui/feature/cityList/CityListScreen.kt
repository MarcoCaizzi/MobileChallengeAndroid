package com.example.mobilechallengeandroid.ui.feature.cityList

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mobilechallengeandroid.domain.model.CityItem
import com.example.mobilechallengeandroid.ui.component.cityList.filter.FilterCityContent
import com.example.mobilechallengeandroid.ui.component.cityList.list.CityListContent
import com.example.mobilechallengeandroid.ui.component.cityMap.CityMapInteractiveContent

@Composable
fun CityListScreen(
    cities: List<CityItem>,
    favoriteIds: Set<Long>,
    filter: String,
    showOnlyFavorites: Boolean,
    isLandscape: Boolean,
    selectedCityId: Long?,
    actions: CityListAction,
    onCityClick: (Long) -> Unit,
    onFavoriteClick: (Long) -> Unit,
    onDetailsClick: (CityItem) -> Unit,
    onFilterChange: (String) -> Unit,
    onShowOnlyFavoritesChange: (Boolean) -> Unit
) {
    if (isLandscape) {
        Row(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.weight(1f)) {
                CityListContent(
                    cities = cities,
                    favoriteIds = favoriteIds,
                    onCityClick = onCityClick,
                    onFavoriteClick = onFavoriteClick,
                    onDetailsClick = { cityItem -> actions.onCityDetailClicked(cityItem.id) },
                    isLandscape = true
                )
            }
            val cityItemState = produceState<CityItem?>(initialValue = null, selectedCityId) {
                value = cities.find { it.id == selectedCityId }
            }
            val cityItem = cityItemState.value
            Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                if (cityItem != null) {
                    CityMapInteractiveContent(
                        city = cityItem,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Select a city to view the map")
                    }
                }
            }
        }
    } else {
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
            if (cities.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                CityListContent(
                    cities = cities,
                    favoriteIds = favoriteIds,
                    onCityClick = onCityClick,
                    onFavoriteClick = onFavoriteClick,
                    onDetailsClick = { cityItem -> actions.onCityDetailClicked(cityItem.id) },
                    isLandscape = false
                )
            }
        }
    }
}

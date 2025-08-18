package com.example.mobilechallengeandroid.ui.component.cityList.list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.mobilechallengeandroid.domain.model.CityItem
import com.example.mobilechallengeandroid.ui.component.cityList.row.RowCityContent


@Composable
fun CityListContent(
    cities: List<CityItem>,
    favoriteIds: Set<Long>,
    onCityClick: (Long) -> Unit,
    onFavoriteClick: (Long) -> Unit,
    onDetailsClick: (CityItem) -> Unit,
    isLandscape: Boolean
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(cities, key = { it.id }) { city ->
            RowCityContent(
                city = city,
                isFavorite = favoriteIds.contains(city.id),
                isLandscape = isLandscape,
                onCityClick = onCityClick,
                onFavoriteClick = onFavoriteClick,
                onDetailsClick = onDetailsClick
            )
            HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
        }
    }
}
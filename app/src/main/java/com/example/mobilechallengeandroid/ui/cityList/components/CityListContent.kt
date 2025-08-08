package com.example.mobilechallengeandroid.ui.cityList.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mobilechallengeandroid.domain.model.CityItem


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

@Preview
@Composable
fun PreviewCityList() {
    CityListContent(
        cities = listOf(
            CityItem(1, "Buenos Aires", "Argentina", com.example.mobilechallengeandroid.domain.model.CoordinatesItem(1.0, 2.0)),
            CityItem(2, "Madrid", "España", com.example.mobilechallengeandroid.domain.model.CoordinatesItem(3.0, 4.0))
        ),
        favoriteIds = setOf(1),
        onCityClick = {},
        onFavoriteClick = {},
        onDetailsClick = {},
        isLandscape = false
    )
}
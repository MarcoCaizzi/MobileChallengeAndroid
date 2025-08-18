package com.example.mobilechallengeandroid.ui.component.cityList.list

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.mobilechallengeandroid.domain.model.CityItem
import com.example.mobilechallengeandroid.domain.model.CoordinatesItem

@Preview
@Composable
fun PreviewCityList() {
    CityListContent(
        cities = listOf(
            CityItem(1, "Buenos Aires", "Argentina", CoordinatesItem(1.0, 2.0)),
            CityItem(2, "Madrid", "España", CoordinatesItem(3.0, 4.0))
        ),
        favoriteIds = setOf(1),
        onCityClick = {},
        onFavoriteClick = {},
        onDetailsClick = {},
        isLandscape = false
    )
}
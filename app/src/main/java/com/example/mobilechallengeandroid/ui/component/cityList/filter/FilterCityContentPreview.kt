package com.example.mobilechallengeandroid.ui.component.cityList.filter

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Composable
fun PreviewFilterCityContent() {
    FilterCityContent(
        filter = "",
        showOnlyFavorites = true,
        onFilterChange = {},
        onShowOnlyFavoritesChange = {}
    )
}
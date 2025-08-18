package com.example.mobilechallengeandroid.ui.component.cityList.row

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.mobilechallengeandroid.domain.model.CityItem
import com.example.mobilechallengeandroid.domain.model.CoordinatesItem

@Preview(showBackground = true)
@Composable
fun PreviewRowCityContent() {
    RowCityContent(
        city = CityItem(1, "Buenos Aires", "Argentina", CoordinatesItem(1.0, 2.0)),
        isFavorite = true,
        isLandscape = false,
        onFavoriteClick = {},
        onDetailsClick = {},
        onCityClick = {}
    )
}
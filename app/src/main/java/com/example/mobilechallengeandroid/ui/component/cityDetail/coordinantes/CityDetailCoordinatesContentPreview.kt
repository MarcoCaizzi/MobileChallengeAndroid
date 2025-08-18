package com.example.mobilechallengeandroid.ui.component.cityDetail.coordinantes

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.mobilechallengeandroid.domain.model.CityItem
import com.example.mobilechallengeandroid.domain.model.CoordinatesItem

@Preview(showBackground = true)
@Composable
fun PreviewCityDetailCoordinatesContent() {
    CityDetailCoordinatesContent(
        cityItem = CityItem(
            id = 1L,
            name = "Buenos Aires",
            country = "Argentina",
            coordinates = CoordinatesItem(-34.6037, -58.3816)
        )
    )
}
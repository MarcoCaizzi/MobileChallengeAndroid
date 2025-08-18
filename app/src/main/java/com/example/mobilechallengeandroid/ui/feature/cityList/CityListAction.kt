package com.example.mobilechallengeandroid.ui.feature.cityList

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.mobilechallengeandroid.ui.feature.MainCoordinator

data class CityListAction(
    val onCityRowClicked: (Long) -> Unit,
    val onCityDetailClicked: (Long) -> Unit,
)

@Composable
fun rememberCityListAction(
    coordinator: MainCoordinator
): CityListAction =
    remember {
        CityListAction(
            onCityRowClicked = { cityId ->
                coordinator.navigateToCityMap(cityId)
            },
            onCityDetailClicked = { cityId ->
                coordinator.navigateToCityDetail(cityId)
            },
        )
    }

package com.example.mobilechallengeandroid.ui.feature.cityMap

import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.mobilechallengeandroid.ui.feature.cityList.CityListViewModel
import com.example.mobilechallengeandroid.domain.model.CityItem

@Composable
fun CityMapRoute(
    navController: NavHostController,
    cityId: Long
) {
    val cityListViewModel: CityListViewModel = hiltViewModel()
    val cityState = produceState<CityItem?>(initialValue = null, cityId) {
        value = cityListViewModel.getCityById(cityId)
    }
    val city = cityState.value
    CityMapScreen(
        city = city,
        onBack = { navController.popBackStack() }
    )
}

package com.example.mobilechallengeandroid.ui.feature.cityDetail

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.mobilechallengeandroid.ui.feature.cityList.CityListViewModel

@Composable
fun CityDetailRoute(
    navController: NavHostController,
    cityId: Long,
    onBack: (() -> Unit)? = null
) {
    val viewModel: CityDetailViewModel = hiltViewModel()
    val cityListViewModel: CityListViewModel = hiltViewModel()
    CityDetailScreenContainer(
        cityId = cityId,
        onBack = { navController.popBackStack() },
        cityListViewModel = cityListViewModel,
        viewModel = viewModel
    )
}

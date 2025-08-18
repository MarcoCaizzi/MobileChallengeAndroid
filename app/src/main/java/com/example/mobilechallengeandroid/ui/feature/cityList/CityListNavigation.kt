package com.example.mobilechallengeandroid.ui.feature.cityList

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.example.mobilechallengeandroid.ui.feature.MainScreens
import com.example.mobilechallengeandroid.ui.feature.composable
import com.example.mobilechallengeandroid.ui.feature.rememberMainCoordinator

fun NavGraphBuilder.cityListNavigation(
    navController: NavHostController,
    factory: ViewModelProvider.Factory
) {
    composable(MainScreens.CityList) {
        val cityListViewModel: CityListViewModel = hiltViewModel()
        val coordinator = rememberMainCoordinator(
            cityListViewModel = cityListViewModel,
            navController = navController
        )
        CityListRoute(coordinator = coordinator)
    }
}
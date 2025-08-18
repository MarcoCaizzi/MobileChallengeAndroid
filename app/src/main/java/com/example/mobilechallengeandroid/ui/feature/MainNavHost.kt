package com.example.mobilechallengeandroid.ui.feature

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.mobilechallengeandroid.ui.feature.cityList.CityListRoute
import com.example.mobilechallengeandroid.ui.feature.cityDetail.CityDetailRoute
import com.example.mobilechallengeandroid.ui.feature.cityMap.CityMapRoute

@Composable
fun MainNavHost(
    navController: NavHostController,
    coordinator: MainCoordinator
) {
    NavHost(
        navController = coordinator.navController,
        startDestination = MainScreens.CityList.route
    ) {
        composable(MainScreens.CityList) {
            CityListRoute(coordinator = coordinator)
        }
        composable(MainScreens.CityDetail) { backStackEntry ->
            val cityId = backStackEntry.findArg<Long>(MainScreens.CityDetail.navArgs[0])
            CityDetailRoute(navController = navController, cityId = cityId)
        }
        composable(MainScreens.CityMap) { backStackEntry ->
            val cityId = backStackEntry.findArg<Long>(MainScreens.CityMap.navArgs[0])
            CityMapRoute(navController = navController, cityId = cityId)
        }
    }
}

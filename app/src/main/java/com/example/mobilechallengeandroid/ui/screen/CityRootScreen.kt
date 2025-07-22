package com.example.mobilechallengeandroid.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.*
import com.example.mobilechallengeandroid.ui.viewmodel.CityListViewModel
import androidx.paging.compose.collectAsLazyPagingItems

@Composable
fun CityRootScreen() {
    val navController = rememberNavController()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val cityListViewModel: CityListViewModel = hiltViewModel()

    val selectedId by cityListViewModel.selectedCityId.collectAsState()
    val pagedCities = cityListViewModel.pagedCities.collectAsLazyPagingItems()
    val firstPagedCityId = pagedCities.itemSnapshotList.items.firstOrNull()?.id

    LaunchedEffect(firstPagedCityId, selectedId, isLandscape) {
        if (isLandscape && selectedId == null && firstPagedCityId != null) {
            cityListViewModel.selectCity(firstPagedCityId)
        }
    }

    if (isLandscape) {
        Row(Modifier.fillMaxSize()) {
            Box(Modifier.weight(1f)) {
                CityListScreen(
                    onCityClick = { cityId -> cityListViewModel.selectCity(cityId) },
                    navController = navController,
                    viewModel = cityListViewModel,
                    isLandscape = true
                )
            }
            Box(Modifier.weight(1f)) {
                selectedId?.let { id ->
                    CityMapScreen(
                        cityId = id,
                        onBack = {},
                        viewModel = cityListViewModel
                    )
                }
            }
        }
    } else {
        NavHost(navController, startDestination = "cityList") {
            composable("cityList") {
                CityListScreen(
                    onCityClick = { cityId: Long ->
                        navController.navigate("cityDetail/$cityId")
                    },
                    navController = navController,
                    viewModel = cityListViewModel
                )
            }
            composable("cityDetail/{cityId}") { backStackEntry ->
                val cityId = backStackEntry.arguments?.getString("cityId")?.toLongOrNull()
                if (cityId != null) {
                    CityMapScreen(
                        cityId = cityId,
                        onBack = { navController.navigateUp() },
                        viewModel = cityListViewModel
                    )
                }
            }
            composable("cityData/{cityId}") { backStackEntry ->
                val cityId = backStackEntry.arguments?.getString("cityId")?.toLongOrNull()
                if (cityId != null) {
                    CityDetailScreen(
                        cityId = cityId,
                        onBack = { navController.navigateUp() }
                    )
                }
            }
        }
    }
}
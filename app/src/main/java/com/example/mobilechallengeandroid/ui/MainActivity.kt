package com.example.mobilechallengeandroid.ui

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.mobilechallengeandroid.ui.cityDetail.CityDetailScreenContainer
import com.example.mobilechallengeandroid.ui.cityList.CityListScreenContainer
import com.example.mobilechallengeandroid.ui.cityList.CityListViewModel
import com.example.mobilechallengeandroid.ui.map.CityMapScreenContainer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
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
                        CityListScreenContainer(
                            navController = navController,
                            cityListViewModel = cityListViewModel,
                            isLandscape = true
                        )
                    }
                    Box(Modifier.weight(1f)) {
                        val selectedCityId by cityListViewModel.selectedCityId.collectAsState()
                        CityMapScreenContainer(
                            cityId = selectedCityId,
                            cityListViewModel = cityListViewModel
                        )
                    }
                }
            } else {
                NavHost(navController, startDestination = "cityList") {
                    composable("cityList") {
                        CityListScreenContainer(
                            navController = navController,
                            cityListViewModel = cityListViewModel,
                            isLandscape = false
                        )
                    }
                    composable("cityDetail/{cityId}") { backStackEntry ->
                        val cityId = backStackEntry.arguments?.getString("cityId")?.toLongOrNull()
                        if (cityId != null) {
                            CityMapScreenContainer(
                                cityId = cityId,
                                cityListViewModel = cityListViewModel,
                                onBack = { navController.navigateUp() }
                            )
                        }
                    }
                    composable("cityData/{cityId}") { backStackEntry ->
                        val cityId = backStackEntry.arguments?.getString("cityId")?.toLongOrNull()
                        if (cityId != null) {
                            CityDetailScreenContainer(
                                cityId = cityId,
                                onBack = { navController.navigateUp() }
                            )
                        }
                    }
                }
            }
        }
    }
}
package com.example.mobilechallengeandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.compose.*
import com.example.mobilechallengeandroid.ui.CityListScreen
import com.example.mobilechallengeandroid.ui.CityMapScreen
import com.example.mobilechallengeandroid.ui.CityListViewModel
import com.example.mobilechallengeandroid.data.CityRepositoryImpl
import com.example.mobilechallengeandroid.ui.CityDetailScreen
import com.example.mobilechallengeandroid.ui.CityDetailViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = CityRepositoryImpl(this)
        val cityListViewModel = CityListViewModel(repository)
        val cityDetailViewModel = CityDetailViewModel(repository)


        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = "cityList") {
                composable("cityList") {
                    CityListScreen(
                        viewModel = cityListViewModel, onCityClick = { cityId ->
                            navController.navigate("cityDetail/$cityId")
                        },
                        navController = navController
                    )
                }
                composable("cityDetail/{cityId}") { backStackEntry ->
                    val cityId = backStackEntry.arguments?.getString("cityId")?.toLongOrNull()
                    val cities by cityListViewModel.cities.collectAsState()
                    val city = cities.find { it.id == cityId }
                    if (city != null) {
                        CityMapScreen(city, onBack = {
                            navController.navigateUp()
                        })
                    }
                }
                composable("cityData/{cityId}") { backStackEntry ->
                    val cityId = backStackEntry.arguments?.getString("cityId")?.toLongOrNull()
                    val cities by cityListViewModel.cities.collectAsState()
                    val city = cities.find { it.id == cityId }
                    if (city != null) {
                        CityDetailScreen(city, onBack = { navController.navigateUp() })
                    }
                }
                composable("cityData/{cityId}") { backStackEntry ->
                    val cityId = backStackEntry.arguments?.getString("cityId")?.toLongOrNull()
                    val cities by cityListViewModel.cities.collectAsState()
                    val city = cities.find { it.id == cityId }
                    if (city != null) {
                        CityDetailScreen(
                            city = city,
                            onBack = { navController.navigateUp() },
                            viewModel = cityDetailViewModel
                        )
                    }
                }
            }
        }
    }
}
package com.example.mobilechallegeandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.compose.*
import com.example.mobilechallegeandroid.ui.CityListScreen
import com.example.mobilechallegeandroid.ui.CityMapScreen
import com.example.mobilechallegeandroid.ui.CityListViewModel
import com.example.mobilechallegeandroid.data.CityRepositoryImpl
import com.example.mobilechallegeandroid.ui.CityDetailScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = CityRepositoryImpl(this)
        val viewModel = CityListViewModel(repository)
        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = "cityList") {
                composable("cityList") {
                    CityListScreen(
                        viewModel = viewModel, onCityClick = { cityId ->
                            navController.navigate("cityDetail/$cityId")
                        },
                        navController = navController
                    )
                }
                composable("cityDetail/{cityId}") { backStackEntry ->
                    val cityId = backStackEntry.arguments?.getString("cityId")?.toLongOrNull()
                    val cities by viewModel.cities.collectAsState()
                    val city = cities.find { it.id == cityId }
                    if (city != null) {
                        CityMapScreen(city, onBack = {
                            navController.navigateUp()
                        })
                    }
                }
                composable("cityData/{cityId}") { backStackEntry ->
                    val cityId = backStackEntry.arguments?.getString("cityId")?.toLongOrNull()
                    val cities by viewModel.cities.collectAsState()
                    val city = cities.find { it.id == cityId }
                    if (city != null) {
                        CityDetailScreen(city, onBack = { navController.navigateUp() })
                    }
                }
            }
        }
    }
}
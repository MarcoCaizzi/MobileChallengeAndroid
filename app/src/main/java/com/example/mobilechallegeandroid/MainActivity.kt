package com.example.mobilechallegeandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.*
import com.example.mobilechallegeandroid.ui.CityListScreen
import com.example.mobilechallegeandroid.ui.CityListViewModel
import com.example.mobilechallegeandroid.data.CityRepositoryImpl

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = CityRepositoryImpl(this)
        val viewModel = CityListViewModel(repository)
        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = "cityList") {
                composable("cityList") {
                    CityListScreen(viewModel = viewModel, onCityClick = { cityId ->
                        navController.navigate("cityDetail/$cityId")
                    })
                }
            }
        }
    }
}
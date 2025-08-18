package com.example.mobilechallengeandroid.ui.feature

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.mobilechallengeandroid.ui.feature.cityList.CityListViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.hilt.navigation.compose.hiltViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val cityListViewModel: CityListViewModel = hiltViewModel()
            MainNavHost(
                navController = navController,
                coordinator = rememberMainCoordinator(
                    cityListViewModel = cityListViewModel,
                    navController = navController,
                )
            )
        }
    }
}
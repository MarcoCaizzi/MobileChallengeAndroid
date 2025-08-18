package com.example.mobilechallengeandroid.ui.feature

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.mobilechallengeandroid.domain.model.CityItem
import com.example.mobilechallengeandroid.ui.feature.base.MainBaseCoordinator
import com.example.mobilechallengeandroid.ui.feature.cityList.CityListViewModel
import kotlinx.coroutines.CoroutineScope

class MainCoordinator(
    private val cityListViewModel: CityListViewModel,
    navController: NavHostController,
    context: Context,
    scope: CoroutineScope,
): MainBaseCoordinator(
    context,
    navController,
    scope
) {

    fun onCreate() {
        goToScreen(screenRoute = MainScreens.CityList.baseRoute)
    }

    fun onRefresh() {
        // Lógica para refrescar la lista o datos
    }

    fun onInfoClicked(city: CityItem) {
        navController.navigate("${MainScreens.CityDetail.baseRoute}/${city.id}")
    }

    fun onCityRowClicked(cityId: Long, isLandscape: Boolean) {
        if (isLandscape) {
            // Solo seleccionar la ciudad
            // La lógica de selección se maneja en el ViewModel
        } else {
            navController.navigate("${MainScreens.CityMap.baseRoute}/$cityId")
        }
    }

    fun navigateToCityDetail(cityId: Long) {
        navController.navigate("${MainScreens.CityDetail.baseRoute}/$cityId")
    }

    fun navigateToCityMap(cityId: Long) {
        navController.navigate("${MainScreens.CityMap.baseRoute}/$cityId")
    }
}

@Composable
fun rememberMainCoordinator(
    cityListViewModel: CityListViewModel,
    navController: NavHostController,
    score: CoroutineScope = rememberCoroutineScope(),
): MainCoordinator {
    val context = LocalContext.current
    return remember{
        MainCoordinator(
            cityListViewModel = cityListViewModel,
            navController=navController,
            scope=score,
            context=context)
        }
    }

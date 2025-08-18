package com.example.mobilechallengeandroid.ui.feature

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

data class NavArg(val key: String, val navType: NavType<*>)

sealed class MainScreens(
    val baseRoute: String,
    val navArgs: List<NavArg> = emptyList(),
) {
    val route = run {
        val argKeys = navArgs.map { "{${it.key}}" }
        if (argKeys.isEmpty()) {
            baseRoute
        } else {
            "$baseRoute/${argKeys.joinToString("/")}"
        }
    }
    open val args = navArgs.map { navArgument(it.key) { type = it.navType } }

    object CityList : MainScreens("cityList")
    object CityDetail : MainScreens("cityDetail", listOf(NavArg("cityId", NavType.LongType)))
    object CityMap : MainScreens("cityMap", listOf(NavArg("cityId", NavType.LongType)))
}

fun NavGraphBuilder.composable(
    navItem: MainScreens,
    content: @androidx.compose.runtime.Composable (NavBackStackEntry) -> Unit,
) {
    composable(route = navItem.route, arguments = navItem.args) {
        content(it)
    }
}

inline fun <reified T> NavBackStackEntry.findArg(arg: NavArg): T {
    val value = when (arg.navType) {
        NavType.FloatType -> arguments?.getFloat(arg.key)
        NavType.IntType -> arguments?.getInt(arg.key)
        NavType.LongType -> arguments?.getLong(arg.key)
        else -> arguments?.getString(arg.key)
    }
    requireNotNull(value) { "Argument ${arg.key} is required but was null" }
    return value as T
}

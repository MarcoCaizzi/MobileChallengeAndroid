package com.example.mobilechallengeandroid.ui.feature.base

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope

abstract class MainBaseCoordinator(
    val context: Context,
    val navController: NavHostController,
    val scope: CoroutineScope
) {

    val activity get() = context as ComponentActivity

    fun onBack() = navController.popBackStack()

    fun finish() = activity.finish()

    fun goToScreen(screenRoute: String) = navController.navigate(screenRoute)

}
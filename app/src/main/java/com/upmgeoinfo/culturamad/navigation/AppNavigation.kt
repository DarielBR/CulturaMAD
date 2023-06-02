package com.upmgeoinfo.culturamad.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.upmgeoinfo.culturamad.MainScreen
import com.upmgeoinfo.culturamad.ui.composables.SplashScreen

/**
 * Handles the navigation within the application. Will show at first the Splash Screen followed by
 * the main activity. Also cleanses the views while navigates towards the main activity.
 */
@Composable
fun AppNavigation(fuseLocationClient: FusedLocationProviderClient,){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AppScreens.SplashScreen.route,
        builder = {
            composable(AppScreens.SplashScreen.route){
                SplashScreen(navController)
            }
            composable(AppScreens.MainScreen.route){
                MainScreen(fuseLocationClient,modifier = Modifier)
            }
        }
    )
}
package com.upmgeoinfo.culturamad.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.upmgeoinfo.culturamad.MainScreen
import com.upmgeoinfo.culturamad.datamodel.MainViewModel
import com.upmgeoinfo.culturamad.navigation.navbar.MenuItems
import com.upmgeoinfo.culturamad.ui.composables.OverviewScreen
import com.upmgeoinfo.culturamad.ui.composables.SplashScreen
import com.upmgeoinfo.culturamad.ui.composables.UserScreen

/**
 * Handles the navigation within the application. Will show at first the Splash Screen followed by
 * the main activity. Also cleanses the views while navigates towards the main activity.
 */
//TODO: Rewrite this functionality in order to have all routes homogeneously coded.
@OptIn(ExperimentalMaterial3Api::class)
@MapsComposeExperimentalApi
@Composable
fun AppNavigation(fuseLocationClient: FusedLocationProviderClient, viewModel: MainViewModel){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AppScreens.SplashScreen.route,
        builder = {
            composable(AppScreens.SplashScreen.route){
                SplashScreen(navController)
            }
            composable(AppScreens.MainScreen.route){
                MainScreen(fuseLocationClient, viewModel)
            }
            composable(MenuItems.OverviewScreen.route){
                OverviewScreen()
            }
            composable(MenuItems.UserScreen.route){
                UserScreen()
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlternateNavigation(
    navController: NavHostController,
    fusedLocationClient: FusedLocationProviderClient,
    viewModel: MainViewModel
){
    NavHost(
        navController = navController,
        startDestination = AppScreens.SplashScreen.route
    ){//this is the builder parameter
        composable(AppScreens.SplashScreen.route){
            SplashScreen(navController = navController)
        }
        composable(AppScreens.MainScreen.route){
            MainScreen(fusedLocationClient, viewModel)
        }
        composable(MenuItems.OverviewScreen.route){
            OverviewScreen()
        }
        composable(MenuItems.UserScreen.route){
            UserScreen()
        }
    }
}
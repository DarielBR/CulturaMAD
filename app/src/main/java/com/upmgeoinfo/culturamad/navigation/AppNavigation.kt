package com.upmgeoinfo.culturamad.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.upmgeoinfo.culturamad.MainScreen
import com.upmgeoinfo.culturamad.viewmodels.main.MainViewModel
import com.upmgeoinfo.culturamad.navigation.navbar.MenuItems
import com.upmgeoinfo.culturamad.ui.composables.ClusterMapScreen
import com.upmgeoinfo.culturamad.ui.composables.DetailViewScreen
import com.upmgeoinfo.culturamad.ui.composables.OverviewScreen
import com.upmgeoinfo.culturamad.ui.composables.SplashScreen
import com.upmgeoinfo.culturamad.ui.composables.UserScreen

/**
 * <<NOT IN USE CURRENTLY>>
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
                SplashScreen(
                    navController = navController,
                    viewModel = viewModel
                )
            }
            composable(AppScreens.MainScreen.route){
                MainScreen(fuseLocationClient, viewModel)
            }
            composable(MenuItems.OverviewScreen.route){
                OverviewScreen()
            }
            composable(MenuItems.UserScreen.route){
                UserScreen(onNavToSignupScreen = {}, onNavToLoginScreen = {})
            }
        }
    )
}
/**
 * (Alternatively using a navigation bottom bar)Handles the navigation within the application. Will show at first the Splash Screen followed by
 * the main activity. Also cleanses the views while navigates towards the main activity.
 */
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, MapsComposeExperimentalApi::class)
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
            SplashScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        composable(MenuItems.FullMapScreen.route){
            ClusterMapScreen(
                fusedLocationClient = fusedLocationClient,
                viewModel = viewModel
            )
        }
        composable(MenuItems.OverviewScreen.route){
            OverviewScreen(
                viewModel = viewModel
            )
        }
        composable(MenuItems.UserScreen.route){
            UserScreen(onNavToSignupScreen = {}, onNavToLoginScreen = {})
        }
        composable(AppScreens.DetailViewScreen.route){
            DetailViewScreen(culturalEvent = viewModel.getCurrentEvent())
        }
    }
}
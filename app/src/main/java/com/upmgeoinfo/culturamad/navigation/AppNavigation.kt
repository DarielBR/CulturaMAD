package com.upmgeoinfo.culturamad.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.upmgeoinfo.culturamad.viewmodels.MainViewModel
import com.upmgeoinfo.culturamad.navigation.navbar.MenuItems
import com.upmgeoinfo.culturamad.ui.composables.ClusterMapScreen
import com.upmgeoinfo.culturamad.ui.composables.DetailViewScreen
import com.upmgeoinfo.culturamad.ui.composables.ErrorScreen
import com.upmgeoinfo.culturamad.ui.composables.LoginScreen
import com.upmgeoinfo.culturamad.ui.composables.OverviewScreen
import com.upmgeoinfo.culturamad.ui.composables.SignupScreen
import com.upmgeoinfo.culturamad.ui.composables.SplashScreen
import com.upmgeoinfo.culturamad.ui.composables.UserScreen

/**
 * (Alternatively using a navigation bottom bar)Handles the navigation within the application. Will show at first the Splash Screen followed by
 * the main activity. Also cleanses the views while navigates towards the main activity.
 */
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun AlternateNavigation(
    navController: NavHostController,
    fusedLocationClient: FusedLocationProviderClient,
    viewModel: MainViewModel,
    //authenticationViewModel: AuthenticationViewModel
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
        composable(AppScreens.ErrorScreen.route){
            ErrorScreen(
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
            UserScreen(
                viewModel = viewModel,
                //authenticationViewModel = authenticationViewModel,
                onNavToSignupScreen = {
                    navController.navigate(AppScreens.SignupScreen.route){}
                },
                onNavToLoginScreen = {
                    navController.navigate(AppScreens.LoginScreen.route){}
                }
            )
        }
        composable(AppScreens.DetailViewScreen.route){
            DetailViewScreen(
                mainViewModel = viewModel,
                onNavBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(AppScreens.LoginScreen.route){
            LoginScreen(
                viewModel = viewModel,
                //authenticationViewModel = authenticationViewModel ,
                onNavToSignupScreen = {
                    navController.navigate(AppScreens.SignupScreen.route){
                        popUpTo(AppScreens.LoginScreen.route){inclusive = true}
                    }
                },
                onNavBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(AppScreens.SignupScreen.route){
            SignupScreen(
                viewModel = viewModel,
                //authenticationViewModel = authenticationViewModel,
                onNavToLoginScreen = {
                    navController.navigate(AppScreens.LoginScreen.route){
                        popUpTo(AppScreens.SignupScreen.route){inclusive = true}
                    }
                },
                onNavBack = {
                    navController.navigate(MenuItems.UserScreen.route){
                        popUpTo(AppScreens.SignupScreen.route){inclusive = true}
                    }
                    //navController.popBackStack()
                }
            )
        }
    }
}
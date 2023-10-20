package com.upmgeoinfo.culturamad.navigation

sealed class AppScreens (val route: String){
    object SplashScreen: AppScreens("splash_screen")
    object MainScreen: AppScreens("main_screen")
    object DetailViewScreen: AppScreens("detail_view_screen")
    object AdvancedFilterScreen: AppScreens("advanced_filter_screen")
    object LoginScreen: AppScreens("login_screen")
    object SignupScreen: AppScreens("signup_screen")
    object ErrorScreen: AppScreens("error_screen")
}
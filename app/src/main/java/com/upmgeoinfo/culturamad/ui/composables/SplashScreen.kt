package com.upmgeoinfo.culturamad.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.upmgeoinfo.culturamad.R
import com.upmgeoinfo.culturamad.datamodel.MainViewModel
import com.upmgeoinfo.culturamad.navigation.AppScreens
import com.upmgeoinfo.culturamad.navigation.navbar.MenuItems
import kotlinx.coroutines.delay

/**
 * Calls the splash screen within a coroutine scope.
 */
@Composable
fun SplashScreen(
    navController: NavHostController,
    viewModel: MainViewModel
){

    LaunchedEffect(key1 = true,){
        delay(2000)
        navController.popBackStack()
        //navController.navigate(AppScreens.MainScreen.route)
        viewModel.changeSplashScreenState(false)
        navController.navigate(MenuItems.FullMapScreen.route)
    }
    Splash()
}

/**
 * A composable declaration of the splash screen
 */
@Composable
fun Splash(){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(color = colorResource(id = R.color.cmad_background))
            .fillMaxSize()
    ){
        var darkTheme by remember { mutableStateOf(false) }
        darkTheme = isSystemInDarkTheme()
        val systemUiController = rememberSystemUiController()
        SideEffect {
            systemUiController.setSystemBarsColor(
                color = Color.Transparent,
                darkIcons = !darkTheme
                // isNavigationBarContrastEnforced = true
            )
            systemUiController.setNavigationBarColor(
                color = Color.Transparent,
                darkIcons = !darkTheme
            )
        }
        Image(
            painter = painterResource(id = R.drawable.cmad_logo),
            contentDescription = null,
            modifier = Modifier.scale(4f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPrevie(){
    Splash()
}
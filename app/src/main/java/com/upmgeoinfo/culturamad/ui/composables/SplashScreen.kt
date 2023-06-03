package com.upmgeoinfo.culturamad.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.upmgeoinfo.culturamad.R
import com.upmgeoinfo.culturamad.navigation.AppScreens
import kotlinx.coroutines.delay

/**
 * Calls the splash screen within a coroutine scope.
 */
@Composable
fun SplashScreen(navController: NavHostController){

    LaunchedEffect(key1 = true,){
        delay(2000)
        navController.popBackStack()
        navController.navigate(AppScreens.MainScreen.route)
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
        val systemUiController = rememberSystemUiController()
        SideEffect {
            systemUiController.setSystemBarsColor(
                color = Color.Transparent,
                darkIcons = true
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
package com.upmgeoinfo.culturamad.ui.composables

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.upmgeoinfo.culturamad.datamodel.MainViewModel
import com.upmgeoinfo.culturamad.navigation.AlternateNavigation
import com.upmgeoinfo.culturamad.navigation.navbar.MenuItems
import com.upmgeoinfo.culturamad.navigation.navbar.MenuItems.*
import com.upmgeoinfo.culturamad.ui.theme.CulturaMADTheme

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ScaffoldedScreen(
    fusedLocationClient: FusedLocationProviderClient,
    viewModel: MainViewModel
){
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    //val scope = rememberCoroutineScope()

    val navItems = listOf(
        OverviewScreen,
        FullMapScreen,
        UserScreen
    )

    Scaffold(
        scaffoldState = scaffoldState,
        bottomBar = {
            if(!viewModel.state.isSplashScreenOnRender){
                AppBottomNavigation(
                    navController = navController,
                    menuItems = navItems
                )
            }
        },
        modifier = Modifier
            .fillMaxSize()
    ){
        AlternateNavigation(
            navController = navController,
            fusedLocationClient = fusedLocationClient,
            viewModel = viewModel
        )
    }
}

@Composable
fun AppBottomNavigation(
    navController: NavHostController,
    menuItems: List<MenuItems>
) {
    val bottomBackgroundColor = MaterialTheme.colorScheme.surface
    BottomAppBar(
        backgroundColor = bottomBackgroundColor,
    ) {
        BottomNavigation(
            backgroundColor = bottomBackgroundColor,
            contentColor = MaterialTheme.colorScheme.onSurface
        ){
            val currentRoute = currentNavigationEntry(navController = navController)
            menuItems.forEach{item ->
                BottomNavigationItem(
                    selected = currentRoute == item.route,
                    onClick = { navController.navigate(item.route) },
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title
                        )
                    },
                    label = { Text(text = item.title) },
                    //alwaysShowLabel = false
                )
            }
        }
    }
}

@Composable
fun currentNavigationEntry(navController: NavHostController): String{
    val entry by navController.currentBackStackEntryAsState()
    return entry?.destination?.route.toString()
}


/*
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true)
@Composable
fun PreviewScaffoldedScreen(){
    CulturaMADTheme {
        //ScaffoldedScreen()
    }
}*/

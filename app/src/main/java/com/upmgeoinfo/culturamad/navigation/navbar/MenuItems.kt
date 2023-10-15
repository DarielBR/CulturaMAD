package com.upmgeoinfo.culturamad.navigation.navbar

import android.graphics.drawable.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import java.util.function.IntConsumer

sealed class MenuItems(
    val icon: ImageVector,
    val title: String,
    val route: String
){
    object OverviewScreen : MenuItems(
        Icons.Filled.Search,
        "Overview",
        "Overview_Screen"
    )

    object FullMapScreen: MenuItems(
        Icons.Filled.Place,
        "Explore",
        "Map_Screen"
    )

    object UserScreen: MenuItems(
        Icons.Filled.Person,
        "Account",
        "User_Screen"
    )
}

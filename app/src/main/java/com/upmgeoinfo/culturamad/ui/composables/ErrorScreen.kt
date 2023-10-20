package com.upmgeoinfo.culturamad.ui.composables

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.upmgeoinfo.culturamad.R
import com.upmgeoinfo.culturamad.navigation.AppScreens
import com.upmgeoinfo.culturamad.ui.theme.CulturaMADTheme
import com.upmgeoinfo.culturamad.viewmodels.MainViewModel

@Composable
fun ErrorScreen(
    navController: NavHostController,
    viewModel: MainViewModel
){
    viewModel.changeSplashScreenState(true)
    SplashError {
        navController.navigate(AppScreens.SplashScreen.route){
            popUpTo(AppScreens.ErrorScreen.route){inclusive = true}
        }
    }
}

@Composable
fun SplashError(
    onNavClick: () -> Unit
){
    Surface(
        color = MaterialTheme.colorScheme.errorContainer,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ){
            var darkTheme by remember { mutableStateOf(false) }
            darkTheme = isSystemInDarkTheme()
            val systemUiController = rememberSystemUiController()
            SideEffect {
                systemUiController.setSystemBarsColor(
                    color = Color.Transparent,
                    darkIcons = !darkTheme
                )
                systemUiController.setNavigationBarColor(
                    color = Color.Transparent,
                    darkIcons = !darkTheme
                )
            }
            Image(
                painter = painterResource(id = R.drawable.cmad_logo),
                contentDescription = null,
                modifier = Modifier.scale(2f)
            )
            Text(
                text = stringResource(R.string.ui_connection_error),
                color = MaterialTheme.colorScheme.onErrorContainer,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 45.dp, start = 18.dp, end = 18.dp, bottom = 18.dp)
            )
            Button(
                onClick = onNavClick,
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text(
                    text = stringResource(R.string.ui_retry),
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SplashErrorPreview(){
    CulturaMADTheme {
        SplashError(){}
    }
}
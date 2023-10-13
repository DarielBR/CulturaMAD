package com.upmgeoinfo.culturamad.ui.composables

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upmgeoinfo.culturamad.ui.composables.prefab.GeneralSearchBar
import com.upmgeoinfo.culturamad.ui.composables.prefab.GeneralSearchBarPreview
import com.upmgeoinfo.culturamad.ui.composables.prefab.PasswordTextField
import com.upmgeoinfo.culturamad.ui.composables.prefab.UserNameTextField
import com.upmgeoinfo.culturamad.ui.theme.CulturaMADTheme
import com.upmgeoinfo.culturamad.viewmodels.auth.AuthenticationViewModel
import kotlin.math.log

@Composable
fun LoginScreen(
    authenticationViewModel: AuthenticationViewModel? = null,
    onNavToSignupScreen: ()-> Unit,
    onNavBack: () -> Unit
){
    val loginUiState = authenticationViewModel?.loginUiState
    val isError = loginUiState?.loginError != null
    val context = LocalContext.current
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surface
                )
                .fillMaxSize()
                .padding(top = 24.dp, start = 12.dp, end = 12.dp, bottom = 12.dp)
        ) {
            Row(//First Row for title
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "Login",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = "Use your mail and password to access your user data.",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Light
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    if (isError){
                        Text(text = loginUiState?.loginError ?: "Unknown error.")
                    }
                    UserNameTextField(authenticationViewModel)
                    Spacer(modifier = Modifier.height(8.dp))
                    PasswordTextField(authenticationViewModel)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun LoginScreenPreview(){
    CulturaMADTheme {
        LoginScreen(
            onNavToSignupScreen = {},
            onNavBack = {}
        )
    }
}
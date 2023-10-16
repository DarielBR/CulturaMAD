package com.upmgeoinfo.culturamad.ui.composables

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upmgeoinfo.culturamad.R
import com.upmgeoinfo.culturamad.ui.composables.prefab.ConfirmPasswordSignupTextField
import com.upmgeoinfo.culturamad.ui.composables.prefab.NavBackButton
import com.upmgeoinfo.culturamad.ui.composables.prefab.PasswordSignupTextField
import com.upmgeoinfo.culturamad.ui.composables.prefab.UserNameSignupTextField
import com.upmgeoinfo.culturamad.ui.theme.CulturaMADTheme
import com.upmgeoinfo.culturamad.viewmodels.auth.AuthenticationViewModel

@Composable
fun SignupScreen(
    authenticationViewModel: AuthenticationViewModel? = null,
    onNavToLoginScreen: () -> Unit,
    onNavBack: () -> Unit
){
    val loginUiState = authenticationViewModel?.loginUiState
    val isError = loginUiState?.signupError != null
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
                    Spacer(modifier = Modifier.height(24.dp))
                    NavBackButton(onClick = onNavBack)
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Signup",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = stringResource(id = R.string.ui_signup_information),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Light
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    if (isError){
                        Text(
                            text = loginUiState?.loginError ?: "Unknown error.",
                            color = Color.Red
                        )
                    }
                    UserNameSignupTextField(authenticationViewModel)
                    Spacer(modifier = Modifier.height(8.dp))
                    PasswordSignupTextField(authenticationViewModel)
                    Spacer(modifier = Modifier.height(8.dp))
                    ConfirmPasswordSignupTextField(authenticationViewModel)
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = {
                            authenticationViewModel?.createUser(context){success ->
                                if (success) {
                                    onNavBack.invoke()
                                }
                            }
                        },
                        shape = MaterialTheme.shapes.medium,
                        elevation = ButtonDefaults.buttonElevation(1.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Signup",
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(id = R.string.ui_do_have_account),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Button(
                            onClick = { onNavToLoginScreen.invoke() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent
                            )
                        ) {
                            Text(
                                text = "Login",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun SignupScreenPreview(){
    CulturaMADTheme {
        SignupScreen(
            onNavToLoginScreen = {},
            onNavBack = {}
        )
    }
}
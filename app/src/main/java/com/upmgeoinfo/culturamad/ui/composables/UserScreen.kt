package com.upmgeoinfo.culturamad.ui.composables

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upmgeoinfo.culturamad.R
import com.upmgeoinfo.culturamad.ui.theme.CulturaMADTheme
import com.upmgeoinfo.culturamad.viewmodels.MainViewModel

@Composable
fun UserScreen(
    viewModel: MainViewModel? = null,
    //authenticationViewModel: AuthenticationViewModel? = null,
    onNavToLoginScreen: () -> Unit,
    onNavToSignupScreen: () -> Unit
){
    val loginUiState = viewModel?.loginUiState
    //val currentUserMail = authenticationViewModel?.currentUser?.email ?: ""
    val hasUser = viewModel?.hasUser ?: false

    viewModel?.hideBottomNavBar(false)
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surface
                )
                .fillMaxSize()
                .padding(top = 12.dp, start = 12.dp, end = 12.dp, bottom = 12.dp)
        ) {
            Spacer(modifier = Modifier.height(45.dp))
            if(hasUser){
                viewModel?.refreshCurrentUserMail()
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.ui_account_information_logged_1))
                    Surface(
                        color = MaterialTheme.colorScheme.surface,
                        elevation = 2.dp,
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp)
                    ){
                        Text(
                            //text = authenticationViewModel?.currentUser?.email ?: "",
                            text = loginUiState?.currentUserMail ?: "",
                            //text = currentUserMail,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Light,
                            modifier = Modifier
                                .padding(6.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(id = R.string.ui_account_information_logged_2),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Button(
                            onClick = {
                                viewModel?.clearStateValues()
                                viewModel?.resetErrors()
                                viewModel?.logOutUser()
                                viewModel?.setupStateDataAfterUserChange()
                                onNavToLoginScreen.invoke()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent
                            ),
                            contentPadding = PaddingValues(start = 1.dp)
                        ) {
                            Text(
                                text = stringResource(id = R.string.ui_change_account_here),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                }
            }else{
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.ui_account_information_not_logged),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            viewModel?.clearStateValues()
                            viewModel?.resetErrors()
                            onNavToLoginScreen.invoke()
                        },
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(id = R.string.ui_here),
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.SemiBold
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
                            text = stringResource(id = R.string.ui_have_account_yet),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Button(
                            onClick = {
                                viewModel?.clearStateValues()
                                viewModel?.resetErrors()
                                onNavToSignupScreen.invoke()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent
                            )
                        ) {
                            Text(
                                text = "Signup",
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

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true)
@Composable
fun PreviewUserScree(){
    CulturaMADTheme {
        UserScreen(
            onNavToLoginScreen = {},
            onNavToSignupScreen = {}
        )
    }
}
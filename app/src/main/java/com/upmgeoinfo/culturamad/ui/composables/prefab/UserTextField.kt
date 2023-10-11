package com.upmgeoinfo.culturamad.ui.composables.prefab

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upmgeoinfo.culturamad.ui.theme.CulturaMADTheme
import com.upmgeoinfo.culturamad.viewmodels.auth.AuthenticationViewModel

@Composable
fun UserNameTextField(
    authenticationViewModel: AuthenticationViewModel? = null
){
    val loginUiState = authenticationViewModel?.loginUiState
    val isError = loginUiState?.loginError != null

    androidx.compose.material.OutlinedTextField(
        value = loginUiState?.userName ?: "",
        onValueChange = { authenticationViewModel?.onUserNameChange(it) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.AccountCircle,
                contentDescription = null
            )
        },
        label = {
            Text(text = "e-mail")
        },
        isError = isError,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colorScheme.surface,
            leadingIconColor = MaterialTheme.colorScheme.onSurface,
            trailingIconColor = MaterialTheme.colorScheme.onSurface,
            textColor = MaterialTheme.colorScheme.onSurface,
            focusedLabelColor = MaterialTheme.colorScheme.surface,
            cursorColor = MaterialTheme.colorScheme.onSurface,
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .shadow(
                elevation = 1.dp,
                shape = MaterialTheme.shapes.medium,
            )
            .clip(MaterialTheme.shapes.medium)
            .fillMaxWidth()
    )
}

@Composable
fun UserNameSignupTextField(
    authenticationViewModel: AuthenticationViewModel? = null
){
    val loginUiState = authenticationViewModel?.loginUiState
    val isError = loginUiState?.signupError != null

    androidx.compose.material.OutlinedTextField(
        value = authenticationViewModel?.loginUiState?.userNameSignup ?: "",
        onValueChange = { authenticationViewModel?.onUserNameSignupChange(it) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.AccountCircle,
                contentDescription = null
            )
        },
        label = {
            Text(text = "e-mail")
        },
        isError = isError,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colorScheme.surface,
            leadingIconColor = MaterialTheme.colorScheme.onSurface,
            trailingIconColor = MaterialTheme.colorScheme.onSurface,
            textColor = MaterialTheme.colorScheme.onSurface,
            focusedLabelColor = MaterialTheme.colorScheme.surface,
            cursorColor = MaterialTheme.colorScheme.onSurface,
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .shadow(
                elevation = 1.dp,
                shape = MaterialTheme.shapes.medium,
            )
            .clip(MaterialTheme.shapes.medium)
            .fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun UserNameTextFieldPreview(){
    CulturaMADTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ){
            UserNameTextField()
            UserNameSignupTextField()
        }
    }
}
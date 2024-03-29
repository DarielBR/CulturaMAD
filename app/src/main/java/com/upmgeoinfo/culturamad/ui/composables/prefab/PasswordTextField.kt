package com.upmgeoinfo.culturamad.ui.composables.prefab

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upmgeoinfo.culturamad.R
import com.upmgeoinfo.culturamad.ui.theme.CulturaMADTheme
import com.upmgeoinfo.culturamad.viewmodels.MainViewModel

@Composable
fun PasswordTextField(
    viewModel: MainViewModel? = null
    //authenticationViewModel: AuthenticationViewModel? = null
){
    val loginUiState = viewModel?.loginUiState
    val isError = loginUiState?.loginError != null
    var visibility by remember { mutableStateOf(false) }
    val trailingIcon = 
        if (visibility) painterResource(id = R.drawable.ic_visibility_on)
        else painterResource(id = R.drawable.ic_visibility_off)
    val visualTransformation =
        if (visibility) VisualTransformation.None
        else PasswordVisualTransformation()

    OutlinedTextField(
        value = loginUiState?.password ?: "",
        onValueChange = { viewModel?.onUserPasswordChange(it) },
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 1.dp,
                shape = MaterialTheme.shapes.medium
            )
            .clip(MaterialTheme.shapes.medium),
        placeholder = { Text(text = "password")},
        leadingIcon = { Icon(imageVector = Icons.Outlined.Lock, contentDescription = null)},
        trailingIcon = {
            IconButton(onClick = {visibility = !visibility}) {
                Icon(painter = trailingIcon, contentDescription = "")
            }
        },
        isError = isError,
        visualTransformation = visualTransformation,
        shape = MaterialTheme.shapes.medium,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
            unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
            unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
    )
}

@Composable
fun PasswordSignupTextField(
    viewModel: MainViewModel? = null
    //authenticationViewModel: AuthenticationViewModel? = null
){
    val loginUiState = viewModel?.loginUiState
    val isError = loginUiState?.signupError != null
    var visibility by remember { mutableStateOf(false) }
    val trailingIcon =
        if (visibility) painterResource(id = R.drawable.ic_visibility_on)
        else painterResource(id = R.drawable.ic_visibility_off)
    val visualTransformation =
        if (visibility) VisualTransformation.None
        else PasswordVisualTransformation()

    OutlinedTextField(
        value = loginUiState?.passwordSignup ?: "",
        onValueChange = { viewModel?.onPasswordSignupChange(it) },
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 1.dp,
                shape = MaterialTheme.shapes.medium
            )
            .clip(MaterialTheme.shapes.medium),
        placeholder = { Text(text = "password")},
        leadingIcon = { Icon(imageVector = Icons.Outlined.Lock, contentDescription = null)},
        trailingIcon = {
            IconButton(onClick = {visibility = !visibility}) {
                Icon(painter = trailingIcon, contentDescription = "")
            }
        },
        isError = isError,
        visualTransformation = visualTransformation,
        shape = MaterialTheme.shapes.medium,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
            unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
            unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
    )
}

@Composable
fun ConfirmPasswordSignupTextField(
    viewModel: MainViewModel? = null
    //authenticationViewModel: AuthenticationViewModel? = null
){
    val loginUiState = viewModel?.loginUiState
    val isError = loginUiState?.signupError != null
    var visibility by remember { mutableStateOf(false) }
    val trailingIcon =
        if (visibility) painterResource(id = R.drawable.ic_visibility_on)
        else painterResource(id = R.drawable.ic_visibility_off)
    val visualTransformation =
        if (visibility) VisualTransformation.None
        else PasswordVisualTransformation()

    OutlinedTextField(
        value = loginUiState?.confirmPasswordSignup ?: "",
        onValueChange = { viewModel?.onConfirmPasswordSignupChange(it) },
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 1.dp,
                shape = MaterialTheme.shapes.medium
            )
            .clip(MaterialTheme.shapes.medium),
        placeholder = { Text(text = "confirm password")},
        leadingIcon = { Icon(imageVector = Icons.Outlined.Lock, contentDescription = null)},
        trailingIcon = {
            IconButton(onClick = {visibility = !visibility}) {
                Icon(painter = trailingIcon, contentDescription = "")
            }
        },
        isError = isError,
        visualTransformation = visualTransformation,
        shape = MaterialTheme.shapes.medium,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
            unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
            unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
    )
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PasswordTextFieldPreview(){
    CulturaMADTheme {
        Column {
            UserNameTextField()
            Spacer(modifier = Modifier.height(8.dp))
            PasswordTextField()
            Spacer(modifier = Modifier.height(8.dp))
            PasswordSignupTextField()
            Spacer(modifier = Modifier.height(8.dp))
            ConfirmPasswordSignupTextField()
            Spacer(modifier = Modifier.height(8.dp))
            GeneralSearchBar()
        }
    }
}
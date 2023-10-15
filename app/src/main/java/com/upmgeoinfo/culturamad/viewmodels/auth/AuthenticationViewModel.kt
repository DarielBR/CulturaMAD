package com.upmgeoinfo.culturamad.viewmodels.auth

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.upmgeoinfo.culturamad.services.authentication.AuthenticationRepository
import com.upmgeoinfo.culturamad.viewmodels.auth.model.LoginUiState
import kotlinx.coroutines.launch

/**
 * ViewModel class to handle UI changes related to Firebase Authentication feature.
 */
class AuthenticationViewModel(
    private val repository: AuthenticationRepository
): ViewModel() {
    val currentUser: FirebaseUser?
        get() = repository.currentUser
    val hasUser: Boolean
        get() = repository.hasUser()
    var loginUiState by mutableStateOf(LoginUiState())
        private set

    fun refreshCurrentUserMail() = viewModelScope.launch {
        val mail = repository.getCurrentUserMail()
        loginUiState = loginUiState.copy(currentUserMail = mail)
    }

    fun onUserNameChange(userName: String){
        loginUiState = loginUiState.copy(userName = userName)
    }

    fun onUserPasswordChange(password: String){
        loginUiState = loginUiState.copy(password = password)
    }

    fun onUserNameSignupChange(userNameSignup: String){
        loginUiState = loginUiState.copy(userNameSignup = userNameSignup)
    }

    fun onPasswordSignupChange(passwordSignup: String){
        loginUiState = loginUiState.copy(passwordSignup = passwordSignup)
    }

    fun onConfirmPasswordSignupChange(confirmPasswordSignup: String){
        loginUiState = loginUiState.copy(confirmPasswordSignup = confirmPasswordSignup)
    }

    private fun validateLoginForm() = loginUiState.userName.isNotBlank()
            && loginUiState.password.isNotBlank()

    private fun validateSignupForm() = loginUiState.userNameSignup.isNotBlank()
            && loginUiState.passwordSignup.isNotBlank()
            && loginUiState.confirmPasswordSignup.isNotBlank()

    fun createUser(
        context: Context,
        onComplete: (Boolean) -> Unit
    ) = viewModelScope.launch {
        try {
            if (!validateSignupForm())
                throw IllegalArgumentException("e-mail and/or password can not be blank.")
            loginUiState = loginUiState.copy(isLoading = true)
            if (loginUiState.passwordSignup != loginUiState.confirmPasswordSignup)
                throw IllegalArgumentException("passwords do not match.")
            loginUiState = loginUiState.copy(signupError = null)
            repository.createUser(
                email = loginUiState.userNameSignup,
                password = loginUiState.passwordSignup
            ){isSuccessful ->
                if (isSuccessful){
                    onComplete.invoke(true)
                    loginUiState = loginUiState.copy(isSuccessOnLogin = true)
                    //loginUiState = loginUiState.copy(currentUser = currentUser)
                    Toast.makeText(context, "Signup Successful!", Toast.LENGTH_LONG).show()
                }else{
                    onComplete.invoke(false)
                    loginUiState = loginUiState.copy(isSuccessOnLogin = false)
                    Toast.makeText(context, "Signup Failure!", Toast.LENGTH_LONG).show()
                }
            }
        }catch (e: Exception){
            loginUiState = loginUiState.copy(signupError = e.localizedMessage)
            e.printStackTrace()
        }finally {
            loginUiState = loginUiState.copy(isLoading = false)
        }
    }

    fun loginUser(
        context: Context,
        onComplete: (Boolean) -> Unit
    ) = viewModelScope.launch {
        try {
            if (!validateLoginForm())
                throw IllegalArgumentException("e-mail and/or password can not be blank.")
            loginUiState = loginUiState.copy(isLoading = true)
            loginUiState = loginUiState.copy(loginError = null)
            repository.logIn(
                email = loginUiState.userName,
                password = loginUiState.password
            ){isSuccessful ->
                if (isSuccessful){
                    onComplete.invoke(true)
                    loginUiState = loginUiState.copy(isSuccessOnLogin = true)
                    Toast.makeText(context, "Login Successful!", Toast.LENGTH_LONG).show()
                }else{
                    onComplete.invoke(false)
                    loginUiState = loginUiState.copy(isSuccessOnLogin = false)
                    Toast.makeText(context, "Login Failure!", Toast.LENGTH_LONG).show()
                }
            }
        }catch (e: Exception){
            loginUiState = loginUiState.copy(loginError = e.localizedMessage)
            e.printStackTrace()
        }finally {
            loginUiState = loginUiState.copy(isLoading = false)
        }
    }

    fun clearStateValues() = viewModelScope.launch{
        loginUiState = loginUiState.copy(userName = "")
        loginUiState = loginUiState.copy(password = "")
        loginUiState = loginUiState.copy(userNameSignup = "")
        loginUiState = loginUiState.copy(passwordSignup = "")
        loginUiState = loginUiState.copy(confirmPasswordSignup = "")
    }

    fun resetErrors() = viewModelScope.launch {
        loginUiState = loginUiState.copy(loginError = null)
        loginUiState = loginUiState.copy(signupError = null)
    }

    fun logOutUser() = viewModelScope.launch {
        repository.logOut()
    }
}
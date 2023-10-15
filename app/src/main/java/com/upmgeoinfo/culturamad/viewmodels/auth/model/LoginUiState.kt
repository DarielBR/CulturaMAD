package com.upmgeoinfo.culturamad.viewmodels.auth.model

import com.google.firebase.auth.FirebaseUser

data class LoginUiState(
    val userName: String = "",
    val password: String = "",
    val userNameSignup: String = "",
    val passwordSignup: String = "",
    val confirmPasswordSignup: String = "",
    val isLoading: Boolean = false,
    val isSuccessOnLogin: Boolean = false,
    val signupError: String? = null,
    val loginError: String? = null,
    val currentUserMail: String = ""
)

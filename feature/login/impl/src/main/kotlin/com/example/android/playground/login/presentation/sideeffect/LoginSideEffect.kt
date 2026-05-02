package com.example.android.playground.login.presentation.sideeffect

sealed interface LoginSideEffect {
    data class ShowWelcomeToast(
        val username: String,
    ) : LoginSideEffect

    data class ShowErrorSnackbar(
        val message: String,
    ) : LoginSideEffect
}

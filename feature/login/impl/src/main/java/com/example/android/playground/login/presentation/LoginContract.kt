package com.example.android.playground.login.presentation

// MVI State
data class LoginState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isLoginSuccess: Boolean = false,
)

// MVI Intent
sealed class LoginIntent {
    data class UpdateUsername(
        val username: String,
    ) : LoginIntent()

    data class UpdatePassword(
        val password: String,
    ) : LoginIntent()

    object Login : LoginIntent()
}

// Side Effects (One-time events)
sealed class LoginSideEffect {
    data class ShowWelcomeToast(
        val username: String,
    ) : LoginSideEffect()

    data class ShowErrorSnackbar(
        val message: String,
    ) : LoginSideEffect()
}

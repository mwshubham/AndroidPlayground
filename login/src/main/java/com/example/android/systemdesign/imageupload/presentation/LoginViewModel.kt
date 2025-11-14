package com.example.android.systemdesign.imageupload.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<LoginSideEffect>()
    val sideEffect: SharedFlow<LoginSideEffect> = _sideEffect.asSharedFlow()

    fun handleIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.UpdateUsername -> updateUsername(intent.username)
            is LoginIntent.UpdatePassword -> updatePassword(intent.password)
            is LoginIntent.Login -> performLogin()
        }
    }

    private fun updateUsername(username: String) {
        _state.value = _state.value.copy(username = username)
    }

    private fun updatePassword(password: String) {
        _state.value = _state.value.copy(password = password)
    }

    private fun performLogin() {
        val currentState = _state.value

        viewModelScope.launch {
            if (currentState.username.isBlank()) {
                _sideEffect.emit(LoginSideEffect.ShowErrorSnackbar("Username cannot be empty"))
                return@launch
            }

            if (currentState.password.isBlank()) {
                _sideEffect.emit(LoginSideEffect.ShowErrorSnackbar("Password cannot be empty"))
                return@launch
            }

            if (currentState.password.length < 6) {
                _sideEffect.emit(LoginSideEffect.ShowErrorSnackbar("Password must be at least 6 characters"))
                return@launch
            }

            _state.value = currentState.copy(isLoading = true)

            try {
                // Simulate fake login with 2 second delay
                delay(2000)

                // For demo purposes, any non-empty username/password is valid
                _state.value = _state.value.copy(
                    isLoading = false,
                    isLoginSuccess = true
                )

                // Emit side effect for welcome toast
                _sideEffect.emit(LoginSideEffect.ShowWelcomeToast(currentState.username))
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false)
                _sideEffect.emit(LoginSideEffect.ShowErrorSnackbar(e.message ?: "Login failed"))
            }
        }
    }
}

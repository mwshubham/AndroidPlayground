package com.example.android.playground.login.presentation.component

import androidx.compose.material3.SnackbarHostState
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.login.presentation.state.LoginState
import org.junit.Rule
import org.junit.Test

class LoginContentTest {
    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    @Test
    fun emptyState() {
        paparazzi.snapshot {
            AppTheme {
                LoginContent(
                    state = LoginState(),
                    onIntent = {},
                    snackbarHostState = SnackbarHostState(),
                )
            }
        }
    }

    @Test
    fun filledFields() {
        paparazzi.snapshot {
            AppTheme {
                LoginContent(
                    state = LoginState(username = "john_doe", password = "secret123"),
                    onIntent = {},
                    snackbarHostState = SnackbarHostState(),
                )
            }
        }
    }

    @Test
    fun loadingState() {
        paparazzi.snapshot {
            AppTheme {
                LoginContent(
                    state =
                        LoginState(
                            username = "john_doe",
                            password = "secret123",
                            isLoading = true,
                        ),
                    onIntent = {},
                    snackbarHostState = SnackbarHostState(),
                )
            }
        }
    }

    @Test
    fun loginSuccessState() {
        paparazzi.snapshot {
            AppTheme {
                LoginContent(
                    state =
                        LoginState(
                            username = "john_doe",
                            isLoginSuccess = true,
                        ),
                    onIntent = {},
                    snackbarHostState = SnackbarHostState(),
                )
            }
        }
    }
}

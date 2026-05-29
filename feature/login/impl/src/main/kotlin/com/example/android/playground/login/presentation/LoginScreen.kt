package com.example.android.playground.login.presentation

import android.widget.Toast
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.android.playground.core.ui.TrackScreenViewEvent
import com.example.android.playground.core.ui.preview.DualThemePreview
import com.example.android.playground.core.ui.preview.PreviewContainer
import com.example.android.playground.login.presentation.component.LoginContent
import com.example.android.playground.login.presentation.sideeffect.LoginSideEffect
import com.example.android.playground.login.presentation.state.LoginState
import com.example.android.playground.login.util.LoginConstants

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                is LoginSideEffect.ShowWelcomeToast -> {
                    Toast
                        .makeText(
                            context,
                            "Welcome ${sideEffect.username}!",
                            Toast.LENGTH_SHORT,
                        ).show()
                }

                is LoginSideEffect.ShowErrorSnackbar -> {
                    snackbarHostState.showSnackbar(sideEffect.message)
                }
            }
        }
    }

    TrackScreenViewEvent(screenName = LoginConstants.SCREEN_NAME)

    LoginContent(
        state = state,
        onIntent = viewModel::handleIntent,
        snackbarHostState = snackbarHostState,
        modifier = modifier,
        onNavigateBack = onNavigateBack,
    )
}

@DualThemePreview
@Composable
fun LoginScreenPreview() {
    PreviewContainer {
        LoginContent(
            state = LoginState(),
            onIntent = {},
            snackbarHostState = SnackbarHostState(),
        )
    }
}

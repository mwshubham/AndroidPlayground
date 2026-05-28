package com.example.android.playground.login.presentation

import app.cash.turbine.test
import com.example.android.playground.core.testing.MainDispatcherRule
import com.example.android.playground.login.presentation.intent.LoginIntent
import com.example.android.playground.login.presentation.sideeffect.LoginSideEffect
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun createViewModel() = LoginViewModel()

    @Test
    fun `initial state has empty credentials`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.state.test {
                val state = awaitItem()
                assertEquals("", state.username)
                assertEquals("", state.password)
                assertFalse(state.isLoading)
                assertFalse(state.isLoginSuccess)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `UpdateUsername intent updates username`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.handleIntent(LoginIntent.UpdateUsername("alice"))

            viewModel.state.test {
                assertEquals("alice", awaitItem().username)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `UpdatePassword intent updates password`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.handleIntent(LoginIntent.UpdatePassword("secret123"))

            viewModel.state.test {
                assertEquals("secret123", awaitItem().password)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `Login with blank username emits ShowErrorSnackbar`() =
        runTest {
            val viewModel = createViewModel()
            viewModel.handleIntent(LoginIntent.UpdateUsername(""))
            viewModel.handleIntent(LoginIntent.UpdatePassword("password123"))

            viewModel.handleIntent(LoginIntent.Login)

            viewModel.sideEffect.test {
                val effect = awaitItem()
                assertTrue(effect is LoginSideEffect.ShowErrorSnackbar)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `Login with short password emits ShowErrorSnackbar`() =
        runTest {
            val viewModel = createViewModel()
            viewModel.handleIntent(LoginIntent.UpdateUsername("alice"))
            viewModel.handleIntent(LoginIntent.UpdatePassword("abc"))

            viewModel.handleIntent(LoginIntent.Login)

            viewModel.sideEffect.test {
                val effect = awaitItem()
                assertTrue(effect is LoginSideEffect.ShowErrorSnackbar)
                cancelAndIgnoreRemainingEvents()
            }
        }
}

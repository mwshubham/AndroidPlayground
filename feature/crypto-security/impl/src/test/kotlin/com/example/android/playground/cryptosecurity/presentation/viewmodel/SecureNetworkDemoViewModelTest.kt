package com.example.android.playground.cryptosecurity.presentation.viewmodel

import app.cash.turbine.test
import com.example.android.playground.core.testing.MainDispatcherRule
import com.example.android.playground.cryptosecurity.domain.model.SecureNetworkResponse
import com.example.android.playground.cryptosecurity.domain.usecase.DecryptNetworkResponseUseCase
import com.example.android.playground.cryptosecurity.domain.usecase.FetchEncryptedFromServerUseCase
import com.example.android.playground.cryptosecurity.presentation.intent.SecureNetworkDemoIntent
import com.example.android.playground.cryptosecurity.presentation.sideeffect.SecureNetworkDemoSideEffect
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

class SecureNetworkDemoViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val fetchEncryptedFromServer: FetchEncryptedFromServerUseCase = mockk()
    private val decryptNetworkResponse: DecryptNetworkResponseUseCase = mockk()

    private fun createViewModel() =
        SecureNetworkDemoViewModel(
            fetchEncryptedFromServer,
            decryptNetworkResponse,
        )

    @Test
    fun `initial state has empty values`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.state.test {
                val state = awaitItem()
                assertFalse(state.isLoading)
                assertNull(state.serverPayload)
                assertNull(state.decryptedMessage)
                assertNull(state.error)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `FetchData success updates serverPayload in state`() =
        runTest {
            val fakeResponse = SecureNetworkResponse(iv = "aWQ=", encryptedPayload = "cGF5")
            coEvery { fetchEncryptedFromServer() } returns fakeResponse

            val viewModel = createViewModel()
            viewModel.handleIntent(SecureNetworkDemoIntent.FetchData)

            viewModel.state.test {
                val state = awaitItem()
                assertNotNull(state.serverPayload)
                assertFalse(state.isLoading)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `FetchData failure sets error and emits ShowError side effect`() =
        runTest {
            coEvery { fetchEncryptedFromServer() } throws RuntimeException("Fetch failed")

            val viewModel = createViewModel()
            viewModel.handleIntent(SecureNetworkDemoIntent.FetchData)

            viewModel.sideEffect.test {
                val effect = awaitItem()
                assertEquals(
                    "Fetch failed",
                    (effect as SecureNetworkDemoSideEffect.ShowError).message,
                )
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `DecryptPayload success updates decryptedMessage in state`() =
        runTest {
            val fakeResponse = SecureNetworkResponse(iv = "aWQ=", encryptedPayload = "cGF5")
            coEvery { fetchEncryptedFromServer() } returns fakeResponse
            every { decryptNetworkResponse(fakeResponse) } returns "Hello World"

            val viewModel = createViewModel()
            viewModel.handleIntent(SecureNetworkDemoIntent.FetchData)
            viewModel.handleIntent(SecureNetworkDemoIntent.DecryptPayload)

            viewModel.state.test {
                val state = awaitItem()
                assertEquals("Hello World", state.decryptedMessage)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `Clear resets state fields`() =
        runTest {
            val viewModel = createViewModel()
            viewModel.handleIntent(SecureNetworkDemoIntent.Clear)

            viewModel.state.test {
                val state = awaitItem()
                assertNull(state.serverPayload)
                assertNull(state.decryptedMessage)
                assertNull(state.error)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `NavigateBack emits NavigateBack side effect`() =
        runTest {
            val viewModel = createViewModel()
            viewModel.handleIntent(SecureNetworkDemoIntent.NavigateBack)

            viewModel.sideEffect.test {
                assertEquals(SecureNetworkDemoSideEffect.NavigateBack, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
}

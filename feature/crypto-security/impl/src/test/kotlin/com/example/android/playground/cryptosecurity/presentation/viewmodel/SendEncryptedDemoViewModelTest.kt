package com.example.android.playground.cryptosecurity.presentation.viewmodel

import app.cash.turbine.test
import com.example.android.playground.core.testing.MainDispatcherRule
import com.example.android.playground.cryptosecurity.domain.model.HybridEncryptedPayload
import com.example.android.playground.cryptosecurity.domain.usecase.EncryptForServerUseCase
import com.example.android.playground.cryptosecurity.domain.usecase.SimulateServerDecryptUseCase
import com.example.android.playground.cryptosecurity.presentation.intent.SendEncryptedDemoIntent
import com.example.android.playground.cryptosecurity.presentation.sideeffect.SendEncryptedDemoSideEffect
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

class SendEncryptedDemoViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val encryptForServer: EncryptForServerUseCase = mockk()
    private val simulateServerDecrypt: SimulateServerDecryptUseCase = mockk()

    private val fakePayload =
        HybridEncryptedPayload(
            encryptedKey = byteArrayOf(1, 2),
            iv = byteArrayOf(3, 4),
            encryptedPayload = byteArrayOf(5, 6),
        )

    private fun createViewModel() =
        SendEncryptedDemoViewModel(
            encryptForServer,
            simulateServerDecrypt,
        )

    @Test
    fun `initial state has default message`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.state.test {
                val state = awaitItem()
                assertEquals("Hello Server! Keep this private.", state.inputMessage)
                assertFalse(state.isEncrypting)
                assertNull(state.encryptedPayload)
                assertNull(state.serverDecryptedMessage)
                assertNull(state.error)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `MessageChanged updates inputMessage in state`() =
        runTest {
            val viewModel = createViewModel()
            viewModel.handleIntent(SendEncryptedDemoIntent.MessageChanged("New message"))

            viewModel.state.test {
                assertEquals("New message", awaitItem().inputMessage)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `EncryptAndSend success updates encryptedPayload in state`() =
        runTest {
            every { encryptForServer(any()) } returns fakePayload

            val viewModel = createViewModel()
            viewModel.handleIntent(SendEncryptedDemoIntent.EncryptAndSend)

            viewModel.state.test {
                val state = awaitItem()
                assertNotNull(state.encryptedPayload)
                assertFalse(state.isEncrypting)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `EncryptAndSend failure emits ShowError side effect`() =
        runTest {
            every { encryptForServer(any()) } throws RuntimeException("Encryption failed")

            val viewModel = createViewModel()
            viewModel.handleIntent(SendEncryptedDemoIntent.EncryptAndSend)

            viewModel.sideEffect.test {
                val effect = awaitItem()
                assertEquals(
                    "Encryption failed",
                    (effect as SendEncryptedDemoSideEffect.ShowError).message,
                )
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `SimulateServerDecrypt success updates serverDecryptedMessage`() =
        runTest {
            every { encryptForServer(any()) } returns fakePayload
            every { simulateServerDecrypt(fakePayload) } returns "Server says hello back"

            val viewModel = createViewModel()
            viewModel.handleIntent(SendEncryptedDemoIntent.EncryptAndSend)
            viewModel.handleIntent(SendEncryptedDemoIntent.SimulateServerDecrypt)

            viewModel.state.test {
                val state = awaitItem()
                assertEquals("Server says hello back", state.serverDecryptedMessage)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `Clear resets state display fields`() =
        runTest {
            val viewModel = createViewModel()
            viewModel.handleIntent(SendEncryptedDemoIntent.Clear)

            viewModel.state.test {
                val state = awaitItem()
                assertNull(state.encryptedPayload)
                assertNull(state.serverDecryptedMessage)
                assertNull(state.error)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `NavigateBack emits NavigateBack side effect`() =
        runTest {
            val viewModel = createViewModel()
            viewModel.handleIntent(SendEncryptedDemoIntent.NavigateBack)

            viewModel.sideEffect.test {
                assertEquals(SendEncryptedDemoSideEffect.NavigateBack, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
}

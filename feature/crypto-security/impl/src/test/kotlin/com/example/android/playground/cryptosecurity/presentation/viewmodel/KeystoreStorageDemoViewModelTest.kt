package com.example.android.playground.cryptosecurity.presentation.viewmodel

import app.cash.turbine.test
import com.example.android.playground.core.testing.MainDispatcherRule
import com.example.android.playground.cryptosecurity.domain.repository.CryptoSecurityRepository
import com.example.android.playground.cryptosecurity.domain.usecase.LoadKeystoreDecryptedUseCase
import com.example.android.playground.cryptosecurity.domain.usecase.SaveKeystoreEncryptedUseCase
import com.example.android.playground.cryptosecurity.presentation.intent.StorageDemoIntent
import com.example.android.playground.cryptosecurity.presentation.sideeffect.StorageDemoSideEffect
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class KeystoreStorageDemoViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val saveKeystoreEncrypted: SaveKeystoreEncryptedUseCase = mockk()
    private val loadKeystoreDecrypted: LoadKeystoreDecryptedUseCase = mockk()
    private val repository: CryptoSecurityRepository = mockk()

    private fun createViewModel() =
        KeystoreStorageDemoViewModel(
            saveKeystoreEncrypted,
            loadKeystoreDecrypted,
            repository,
        )

    @Test
    fun `initial state has default values`() =
        runTest {
            val viewModel = createViewModel()

            viewModel.state.test {
                val state = awaitItem()
                assertEquals("my_secret_key", state.inputKey)
                assertEquals("", state.inputValue)
                assertFalse(state.isSaving)
                assertFalse(state.isLoading)
                assertNull(state.error)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `KeyChanged updates inputKey in state`() =
        runTest {
            val viewModel = createViewModel()
            viewModel.handleIntent(StorageDemoIntent.KeyChanged("new_key"))

            viewModel.state.test {
                assertEquals("new_key", awaitItem().inputKey)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `ValueChanged updates inputValue in state`() =
        runTest {
            val viewModel = createViewModel()
            viewModel.handleIntent(StorageDemoIntent.ValueChanged("top_secret"))

            viewModel.state.test {
                assertEquals("top_secret", awaitItem().inputValue)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `EncryptSave with blank key is a no-op`() =
        runTest {
            val viewModel = createViewModel()
            viewModel.handleIntent(StorageDemoIntent.KeyChanged(""))
            viewModel.handleIntent(StorageDemoIntent.EncryptSave)

            viewModel.state.test {
                assertFalse(awaitItem().isSaving)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `EncryptSave success emits SavedSuccessfully side effect`() =
        runTest {
            coEvery { saveKeystoreEncrypted(any(), any()) } just runs
            every { repository.loadKeystoreRawIv(any()) } returns flowOf(null)
            every { repository.loadKeystoreRawCiphertext(any()) } returns flowOf(null)

            val viewModel = createViewModel()
            viewModel.handleIntent(StorageDemoIntent.KeyChanged("key"))
            viewModel.handleIntent(StorageDemoIntent.ValueChanged("value"))
            viewModel.handleIntent(StorageDemoIntent.EncryptSave)

            viewModel.sideEffect.test {
                assertTrue(awaitItem() is StorageDemoSideEffect.SavedSuccessfully)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `EncryptSave failure emits ShowError side effect`() =
        runTest {
            coEvery { saveKeystoreEncrypted(any(), any()) } throws RuntimeException("Keystore error")

            val viewModel = createViewModel()
            viewModel.handleIntent(StorageDemoIntent.KeyChanged("key"))
            viewModel.handleIntent(StorageDemoIntent.ValueChanged("value"))
            viewModel.handleIntent(StorageDemoIntent.EncryptSave)

            viewModel.sideEffect.test {
                val effect = awaitItem()
                assertTrue(effect is StorageDemoSideEffect.ShowError)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `EncryptSave failure sets error in state`() =
        runTest {
            coEvery { saveKeystoreEncrypted(any(), any()) } throws RuntimeException("Save failed")

            val viewModel = createViewModel()
            viewModel.handleIntent(StorageDemoIntent.KeyChanged("key"))
            viewModel.handleIntent(StorageDemoIntent.ValueChanged("value"))
            viewModel.handleIntent(StorageDemoIntent.EncryptSave)

            viewModel.state.test {
                val state = awaitItem()
                assertEquals("Save failed", state.error)
                assertFalse(state.isSaving)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `LoadDecrypt with blank key is a no-op`() =
        runTest {
            val viewModel = createViewModel()
            viewModel.handleIntent(StorageDemoIntent.KeyChanged(""))
            viewModel.handleIntent(StorageDemoIntent.LoadDecrypt)

            viewModel.state.test {
                assertFalse(awaitItem().isLoading)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `LoadDecrypt success updates loadedValue in state`() =
        runTest {
            every { loadKeystoreDecrypted(any()) } returns flowOf("decrypted_value")

            val viewModel = createViewModel()
            viewModel.handleIntent(StorageDemoIntent.LoadDecrypt)

            viewModel.state.test {
                val state = awaitItem()
                assertEquals("decrypted_value", state.loadedValue)
                assertFalse(state.isLoading)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `Clear sets loaded fields to null in state`() =
        runTest {
            coEvery { repository.clearKeystoreEntry(any()) } just runs

            val viewModel = createViewModel()
            viewModel.handleIntent(StorageDemoIntent.Clear)

            viewModel.state.test {
                val state = awaitItem()
                assertNull(state.loadedValue)
                assertNull(state.savedIvHex)
                assertNull(state.savedCiphertextHex)
                assertNull(state.error)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `NavigateBack emits NavigateBack side effect`() =
        runTest {
            val viewModel = createViewModel()
            viewModel.handleIntent(StorageDemoIntent.NavigateBack)

            viewModel.sideEffect.test {
                assertEquals(StorageDemoSideEffect.NavigateBack, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
}

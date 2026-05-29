package com.example.android.playground.cryptosecurity.presentation.viewmodel

import app.cash.turbine.test
import com.example.android.playground.core.testing.MainDispatcherRule
import com.example.android.playground.cryptosecurity.domain.repository.CryptoSecurityRepository
import com.example.android.playground.cryptosecurity.domain.usecase.LoadTinkDecryptedUseCase
import com.example.android.playground.cryptosecurity.domain.usecase.SaveTinkEncryptedUseCase
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

class TinkStorageDemoViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val saveTinkEncrypted: SaveTinkEncryptedUseCase = mockk()
    private val loadTinkDecrypted: LoadTinkDecryptedUseCase = mockk()
    private val repository: CryptoSecurityRepository = mockk()

    private fun createViewModel() =
        TinkStorageDemoViewModel(
            saveTinkEncrypted,
            loadTinkDecrypted,
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
            viewModel.handleIntent(StorageDemoIntent.KeyChanged("tink_key"))

            viewModel.state.test {
                assertEquals("tink_key", awaitItem().inputKey)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `ValueChanged updates inputValue in state`() =
        runTest {
            val viewModel = createViewModel()
            viewModel.handleIntent(StorageDemoIntent.ValueChanged("tink_value"))

            viewModel.state.test {
                assertEquals("tink_value", awaitItem().inputValue)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `EncryptSave with blank value is a no-op`() =
        runTest {
            val viewModel = createViewModel()
            viewModel.handleIntent(StorageDemoIntent.KeyChanged("key"))
            // value stays blank
            viewModel.handleIntent(StorageDemoIntent.EncryptSave)

            viewModel.state.test {
                assertFalse(awaitItem().isSaving)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `EncryptSave success emits SavedSuccessfully side effect`() =
        runTest {
            coEvery { saveTinkEncrypted(any(), any()) } just runs
            every { repository.loadTinkRawCiphertext(any()) } returns flowOf(null)

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
            coEvery { saveTinkEncrypted(any(), any()) } throws RuntimeException("Tink error")

            val viewModel = createViewModel()
            viewModel.handleIntent(StorageDemoIntent.KeyChanged("key"))
            viewModel.handleIntent(StorageDemoIntent.ValueChanged("value"))
            viewModel.handleIntent(StorageDemoIntent.EncryptSave)

            viewModel.sideEffect.test {
                assertTrue(awaitItem() is StorageDemoSideEffect.ShowError)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `LoadDecrypt success updates loadedValue in state`() =
        runTest {
            every { loadTinkDecrypted(any()) } returns flowOf("tink_decrypted")

            val viewModel = createViewModel()
            viewModel.handleIntent(StorageDemoIntent.LoadDecrypt)

            viewModel.state.test {
                val state = awaitItem()
                assertEquals("tink_decrypted", state.loadedValue)
                assertFalse(state.isLoading)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `Clear resets loaded display fields to null`() =
        runTest {
            coEvery { repository.clearTinkEntry(any()) } just runs

            val viewModel = createViewModel()
            viewModel.handleIntent(StorageDemoIntent.Clear)

            viewModel.state.test {
                val state = awaitItem()
                assertNull(state.loadedValue)
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

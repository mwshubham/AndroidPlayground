package com.example.android.playground.cryptosecurity.domain.usecase

import com.example.android.playground.cryptosecurity.domain.repository.CryptoSecurityRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SaveTinkEncryptedUseCaseTest {
    private val repository: CryptoSecurityRepository = mockk()
    private lateinit var useCase: SaveTinkEncryptedUseCase

    @Before
    fun setUp() {
        useCase = SaveTinkEncryptedUseCase(repository)
    }

    @Test
    fun `invoke delegates to repository with correct key and value`() =
        runTest {
            coEvery { repository.saveTinkEncrypted("myKey", "myValue") } just runs

            useCase("myKey", "myValue")

            coVerify(exactly = 1) { repository.saveTinkEncrypted("myKey", "myValue") }
        }

    @Test
    fun `invoke propagates exception from repository`() =
        runTest {
            coEvery { repository.saveTinkEncrypted(any(), any()) } throws RuntimeException("Tink error")

            var thrown: Throwable? = null
            runCatching { useCase("k", "v") }.onFailure { thrown = it }

            assert(thrown is RuntimeException)
        }
}

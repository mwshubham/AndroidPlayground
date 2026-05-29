package com.example.android.playground.cryptosecurity.domain.usecase

import app.cash.turbine.test
import com.example.android.playground.cryptosecurity.domain.repository.CryptoSecurityRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class LoadKeystoreDecryptedUseCaseTest {
    private val repository: CryptoSecurityRepository = mockk()
    private lateinit var useCase: LoadKeystoreDecryptedUseCase

    @Before
    fun setUp() {
        useCase = LoadKeystoreDecryptedUseCase(repository)
    }

    @Test
    fun `invoke returns flow from repository`() =
        runTest {
            every { repository.loadKeystoreDecrypted("myKey") } returns flowOf("secret")

            useCase("myKey").test {
                assertEquals("secret", awaitItem())
                awaitComplete()
            }
            verify(exactly = 1) { repository.loadKeystoreDecrypted("myKey") }
        }

    @Test
    fun `invoke returns null when nothing stored for key`() =
        runTest {
            every { repository.loadKeystoreDecrypted("empty") } returns flowOf(null)

            useCase("empty").test {
                assertNull(awaitItem())
                awaitComplete()
            }
        }
}

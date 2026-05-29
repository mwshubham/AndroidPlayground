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

class LoadTinkDecryptedUseCaseTest {
    private val repository: CryptoSecurityRepository = mockk()
    private lateinit var useCase: LoadTinkDecryptedUseCase

    @Before
    fun setUp() {
        useCase = LoadTinkDecryptedUseCase(repository)
    }

    @Test
    fun `invoke returns flow from repository`() =
        runTest {
            every { repository.loadTinkDecrypted("myKey") } returns flowOf("tinkSecret")

            useCase("myKey").test {
                assertEquals("tinkSecret", awaitItem())
                awaitComplete()
            }
            verify(exactly = 1) { repository.loadTinkDecrypted("myKey") }
        }

    @Test
    fun `invoke returns null when nothing stored for key`() =
        runTest {
            every { repository.loadTinkDecrypted("missing") } returns flowOf(null)

            useCase("missing").test {
                assertNull(awaitItem())
                awaitComplete()
            }
        }
}

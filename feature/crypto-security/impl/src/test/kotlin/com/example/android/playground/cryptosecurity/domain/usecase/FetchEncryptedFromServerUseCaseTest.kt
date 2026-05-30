package com.example.android.playground.cryptosecurity.domain.usecase

import com.example.android.playground.cryptosecurity.domain.model.SecureNetworkResponse
import com.example.android.playground.cryptosecurity.domain.repository.CryptoSecurityRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class FetchEncryptedFromServerUseCaseTest {
    private val repository: CryptoSecurityRepository = mockk()
    private lateinit var useCase: FetchEncryptedFromServerUseCase

    private val fakeResponse = SecureNetworkResponse(iv = "aWQ=", encryptedPayload = "cGF5bG9hZA==")

    @Before
    fun setUp() {
        useCase = FetchEncryptedFromServerUseCase(repository)
    }

    @Test
    fun `invoke delegates to repository and returns response`() =
        runTest {
            coEvery { repository.fetchEncryptedFromServer() } returns fakeResponse

            val result = useCase()

            assertEquals(fakeResponse, result)
            coVerify(exactly = 1) { repository.fetchEncryptedFromServer() }
        }

    @Test
    fun `invoke propagates exception from repository`() =
        runTest {
            coEvery { repository.fetchEncryptedFromServer() } throws RuntimeException("Network error")

            var thrown: Throwable? = null
            runCatching { useCase() }.onFailure { thrown = it }

            assert(thrown is RuntimeException)
        }
}

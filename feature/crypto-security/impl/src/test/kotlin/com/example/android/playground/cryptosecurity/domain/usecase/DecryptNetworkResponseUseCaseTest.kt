package com.example.android.playground.cryptosecurity.domain.usecase

import com.example.android.playground.cryptosecurity.domain.model.SecureNetworkResponse
import com.example.android.playground.cryptosecurity.domain.repository.CryptoSecurityRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DecryptNetworkResponseUseCaseTest {
    private val repository: CryptoSecurityRepository = mockk()
    private lateinit var useCase: DecryptNetworkResponseUseCase

    private val fakeResponse = SecureNetworkResponse(iv = "aWQ=", encryptedPayload = "cGF5bG9hZA==")

    @Before
    fun setUp() {
        useCase = DecryptNetworkResponseUseCase(repository)
    }

    @Test
    fun `invoke delegates to repository and returns plaintext`() {
        every { repository.decryptNetworkResponse(fakeResponse) } returns "plaintext"

        val result = useCase(fakeResponse)

        assertEquals("plaintext", result)
        verify(exactly = 1) { repository.decryptNetworkResponse(fakeResponse) }
    }

    @Test
    fun `invoke propagates exception from repository`() {
        every { repository.decryptNetworkResponse(any()) } throws IllegalArgumentException("Bad IV")

        var thrown: Throwable? = null
        runCatching { useCase(fakeResponse) }.onFailure { thrown = it }

        assert(thrown is IllegalArgumentException)
    }
}

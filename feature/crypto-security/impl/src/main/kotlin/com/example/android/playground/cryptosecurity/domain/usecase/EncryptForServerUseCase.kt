package com.example.android.playground.cryptosecurity.domain.usecase

import com.example.android.playground.cryptosecurity.domain.model.HybridEncryptedPayload
import com.example.android.playground.cryptosecurity.domain.repository.CryptoSecurityRepository
import javax.inject.Inject

class EncryptForServerUseCase
    @Inject
    constructor(
        private val repository: CryptoSecurityRepository,
    ) {
        operator fun invoke(plaintext: String): HybridEncryptedPayload = repository.encryptForServer(plaintext)
    }

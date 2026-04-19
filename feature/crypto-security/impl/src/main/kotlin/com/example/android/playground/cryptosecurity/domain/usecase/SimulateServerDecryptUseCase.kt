package com.example.android.playground.cryptosecurity.domain.usecase

import com.example.android.playground.cryptosecurity.domain.model.HybridEncryptedPayload
import com.example.android.playground.cryptosecurity.domain.repository.CryptoSecurityRepository
import javax.inject.Inject

class SimulateServerDecryptUseCase @Inject constructor(
    private val repository: CryptoSecurityRepository,
) {
    operator fun invoke(payload: HybridEncryptedPayload): String =
        repository.simulateServerDecrypt(payload)
}

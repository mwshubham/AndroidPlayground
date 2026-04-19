package com.example.android.playground.cryptosecurity.domain.usecase

import com.example.android.playground.cryptosecurity.domain.repository.CryptoSecurityRepository
import javax.inject.Inject

class SaveTinkEncryptedUseCase @Inject constructor(
    private val repository: CryptoSecurityRepository,
) {
    suspend operator fun invoke(key: String, value: String) =
        repository.saveTinkEncrypted(key, value)
}

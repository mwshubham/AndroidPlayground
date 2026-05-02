package com.example.android.playground.cryptosecurity.domain.usecase

import com.example.android.playground.cryptosecurity.domain.repository.CryptoSecurityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoadTinkDecryptedUseCase
    @Inject
    constructor(
        private val repository: CryptoSecurityRepository,
    ) {
        operator fun invoke(key: String): Flow<String?> = repository.loadTinkDecrypted(key)
    }

package com.example.android.playground.cryptosecurity.domain.usecase

import com.example.android.playground.cryptosecurity.domain.model.SecureNetworkResponse
import com.example.android.playground.cryptosecurity.domain.repository.CryptoSecurityRepository
import javax.inject.Inject

class DecryptNetworkResponseUseCase
    @Inject
    constructor(
        private val repository: CryptoSecurityRepository,
    ) {
        operator fun invoke(response: SecureNetworkResponse): String = repository.decryptNetworkResponse(response)
    }

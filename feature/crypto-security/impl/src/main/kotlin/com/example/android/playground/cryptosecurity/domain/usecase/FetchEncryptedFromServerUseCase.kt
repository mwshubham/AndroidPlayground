package com.example.android.playground.cryptosecurity.domain.usecase

import com.example.android.playground.cryptosecurity.domain.model.SecureNetworkResponse
import com.example.android.playground.cryptosecurity.domain.repository.CryptoSecurityRepository
import javax.inject.Inject

class FetchEncryptedFromServerUseCase @Inject constructor(
    private val repository: CryptoSecurityRepository,
) {
    suspend operator fun invoke(): SecureNetworkResponse = repository.fetchEncryptedFromServer()
}

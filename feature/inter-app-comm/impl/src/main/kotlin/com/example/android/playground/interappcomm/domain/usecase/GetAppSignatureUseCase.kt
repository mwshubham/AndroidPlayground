package com.example.android.playground.interappcomm.domain.usecase

import com.example.android.playground.interappcomm.domain.repository.InterAppCommRepository
import javax.inject.Inject

/**
 * Computes and returns the SHA-256 signing-certificate fingerprint for a given package.
 * Returns null when the package is not installed.
 */
class GetAppSignatureUseCase @Inject constructor(
    private val repository: InterAppCommRepository,
) {
    operator fun invoke(packageName: String): String? =
        repository.getAppSignatureFingerprint(packageName)
}

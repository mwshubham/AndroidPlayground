package com.example.android.playground.cryptosecurity.data.service

import android.util.Base64
import com.example.android.playground.cryptosecurity.data.crypto.AesGcmCryptoManager
import com.example.android.playground.cryptosecurity.domain.model.SecureNetworkResponse
import javax.inject.Inject

/**
 * Simulates an encrypted server response.
 *
 * In a real app the server would:
 *   1. Encrypt the response body with a shared AES key (or the client's public key).
 *   2. Return the Base64-encoded IV and ciphertext in the JSON response.
 *
 * Here we replicate that by encrypting a hardcoded message with the same
 * Android Keystore AES key that the client will use to decrypt it.
 */
class FakeSecureApiService
    @Inject
    constructor(
        private val aesGcmCryptoManager: AesGcmCryptoManager,
    ) {
        private val serverMessage =
            "Hello from the server! This payload was encrypted in transit " +
                "using AES-256-GCM and is safe from eavesdropping."

        fun fetchEncryptedResponse(): SecureNetworkResponse {
            val encrypted = aesGcmCryptoManager.encrypt(serverMessage.toByteArray(Charsets.UTF_8))
            return SecureNetworkResponse(
                iv = Base64.encodeToString(encrypted.iv, Base64.NO_WRAP),
                encryptedPayload = Base64.encodeToString(encrypted.ciphertext, Base64.NO_WRAP),
            )
        }
    }

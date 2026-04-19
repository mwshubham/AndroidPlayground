package com.example.android.playground.cryptosecurity.domain.model

import android.util.Base64

/**
 * Simulates the encrypted JSON payload that a server would send over the wire.
 * Both fields are Base64-encoded strings so they can be transmitted as JSON.
 */
data class SecureNetworkResponse(
    /** Base64-encoded AES-GCM IV (12 bytes). */
    val iv: String,
    /** Base64-encoded AES-GCM ciphertext. */
    val encryptedPayload: String,
) {
    fun toEncryptedData(): com.example.android.playground.cryptosecurity.domain.model.EncryptedData =
        EncryptedData(
            iv = Base64.decode(iv, Base64.NO_WRAP),
            ciphertext = Base64.decode(encryptedPayload, Base64.NO_WRAP),
        )
}

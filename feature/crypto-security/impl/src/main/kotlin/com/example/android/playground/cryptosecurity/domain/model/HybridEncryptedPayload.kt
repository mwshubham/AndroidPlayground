package com.example.android.playground.cryptosecurity.domain.model

/**
 * Wire format for hybrid RSA-OAEP + AES-GCM encrypted payloads.
 *
 * - [encryptedKey]   : one-time AES-256 key, encrypted with the server's RSA-2048 public key
 * - [iv]             : AES-GCM IV (12 bytes)
 * - [encryptedPayload]: actual plaintext encrypted with the one-time AES key
 *
 * Only the holder of the RSA private key (the server) can recover the AES key and thus
 * the plaintext — the Android client has no way to decrypt its own outgoing message.
 */
data class HybridEncryptedPayload(
    val encryptedKey: ByteArray,
    val iv: ByteArray,
    val encryptedPayload: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HybridEncryptedPayload) return false
        return encryptedKey.contentEquals(other.encryptedKey) &&
            iv.contentEquals(other.iv) &&
            encryptedPayload.contentEquals(other.encryptedPayload)
    }

    override fun hashCode(): Int {
        var result = encryptedKey.contentHashCode()
        result = 31 * result + iv.contentHashCode()
        result = 31 * result + encryptedPayload.contentHashCode()
        return result
    }
}

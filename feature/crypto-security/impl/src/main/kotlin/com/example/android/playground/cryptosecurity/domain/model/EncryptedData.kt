package com.example.android.playground.cryptosecurity.domain.model

/** Raw output of AES-GCM encryption — IV must be stored alongside ciphertext for decryption. */
data class EncryptedData(
    val iv: ByteArray,
    val ciphertext: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is EncryptedData) return false
        return iv.contentEquals(other.iv) && ciphertext.contentEquals(other.ciphertext)
    }

    override fun hashCode(): Int = 31 * iv.contentHashCode() + ciphertext.contentHashCode()
}

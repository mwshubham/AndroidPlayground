package com.example.android.playground.cryptosecurity.data.crypto

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import com.example.android.playground.cryptosecurity.domain.model.EncryptedData
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject

/**
 * Option B — Android Keystore + AES-256-GCM (manual).
 *
 * The AES key lives inside the Android Keystore and never leaves the secure enclave.
 * Encryption produces a random 12-byte IV + ciphertext that callers can persist anywhere.
 *
 * This is the low-level, educational approach that shows exactly what happens under the hood.
 * Compare with [TinkDataStoreManager] which achieves the same result via Tink's higher-level API.
 */
class AesGcmCryptoManager
    @Inject
    constructor() {
        private val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }

        private fun getOrCreateSecretKey(): SecretKey {
            keyStore.getKey(KEY_ALIAS, null)?.let { return it as SecretKey }

            val keyGenParams =
                KeyGenParameterSpec
                    .Builder(
                        // keystoreAlias =
                        KEY_ALIAS,
                        // purposes =
                        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT,
                    ).setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setKeySize(KEY_SIZE_BITS)
                    .build()

            return KeyGenerator
                .getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
                .apply { init(keyGenParams) }
                .generateKey()
        }

        fun encrypt(plaintext: ByteArray): EncryptedData {
            val cipher =
                Cipher
                    .getInstance(TRANSFORMATION)
                    .apply { init(Cipher.ENCRYPT_MODE, getOrCreateSecretKey()) }
            val iv = cipher.iv
            val ciphertext = cipher.doFinal(plaintext)
            return EncryptedData(iv = iv, ciphertext = ciphertext)
        }

        fun decrypt(data: EncryptedData): ByteArray {
            val cipher =
                Cipher.getInstance(TRANSFORMATION).apply {
                    init(
                        Cipher.DECRYPT_MODE,
                        getOrCreateSecretKey(),
                        GCMParameterSpec(GCM_TAG_LENGTH_BITS, data.iv),
                    )
                }
            return cipher.doFinal(data.ciphertext)
        }

        companion object {
            private const val ANDROID_KEYSTORE = "AndroidKeyStore"
            const val KEY_ALIAS = "crypto_security_aes_key"
            private const val KEY_SIZE_BITS = 256
            private const val TRANSFORMATION = "AES/GCM/NoPadding"
            private const val GCM_TAG_LENGTH_BITS = 128
        }
    }

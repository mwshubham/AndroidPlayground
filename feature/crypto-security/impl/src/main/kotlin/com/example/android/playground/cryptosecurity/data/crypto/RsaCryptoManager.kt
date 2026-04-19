package com.example.android.playground.cryptosecurity.data.crypto

import android.security.keystore.KeyProperties
import com.example.android.playground.cryptosecurity.domain.model.HybridEncryptedPayload
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.spec.MGF1ParameterSpec
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.OAEPParameterSpec
import javax.crypto.spec.PSource
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

/**
 * Hybrid encryption for outgoing payloads: RSA-OAEP wraps a one-time AES-256-GCM key.
 * The server decrypts the AES key with its RSA private key, then decrypts the payload.
 *
 * DEMO ONLY — the key pair is ephemeral and generated in-process on first use.
 * In production, the public key would be pinned from the server certificate and
 * the private key would never exist on the device.
 */
class RsaCryptoManager @Inject constructor() {

    // Demo key pair — created once per process lifetime
    private val demoServerRsaKeyPair: KeyPair by lazy {
        KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA)
            .apply { initialize(RSA_KEY_SIZE) }
            .generateKeyPair()
    }

    fun encryptForServer(plaintext: ByteArray): HybridEncryptedPayload {
        // 1. Generate a fresh one-time AES-256 key
        val aesKey =
            KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES)
                .apply { init(AES_KEY_SIZE) }
                .generateKey()

        // 2. Encrypt the AES key with the server's RSA public key
        val rsaCipher = Cipher.getInstance(RSA_TRANSFORMATION).apply {
            init(
                /* opmode = */ Cipher.ENCRYPT_MODE,
                /* key = */ demoServerRsaKeyPair.public,
                /* params = */ oaepParams()
            )
        }
        val encryptedKey = rsaCipher.doFinal(aesKey.encoded)

        // 3. Encrypt the plaintext with the one-time AES key
        val aesCipher = Cipher.getInstance(AES_TRANSFORMATION).apply {
            init(Cipher.ENCRYPT_MODE, aesKey)
        }
        val iv = aesCipher.iv
        val encryptedPayload = aesCipher.doFinal(plaintext)

        return HybridEncryptedPayload(
            encryptedKey = encryptedKey,
            iv = iv,
            encryptedPayload = encryptedPayload,
        )
    }

    /**
     * Simulates what the server does: unwrap the AES key with the RSA private key,
     * then decrypt the payload. Only possible because this is a demo with the private key on device.
     */
    fun simulateServerDecrypt(payload: HybridEncryptedPayload): ByteArray {
        // 1. Decrypt the AES key using the RSA private key
        val rsaCipher = Cipher.getInstance(RSA_TRANSFORMATION).apply {
            init(
                /* opmode = */ Cipher.DECRYPT_MODE,
                /* key = */ demoServerRsaKeyPair.private,
                /* params = */ oaepParams()
            )
        }
        val aesKeyBytes = rsaCipher.doFinal(payload.encryptedKey)
        val aesKey = SecretKeySpec(aesKeyBytes, KeyProperties.KEY_ALGORITHM_AES)

        // 2. Decrypt the payload with the recovered AES key
        val aesCipher = Cipher.getInstance(AES_TRANSFORMATION).apply {
            init(Cipher.DECRYPT_MODE, aesKey, GCMParameterSpec(GCM_TAG_LENGTH_BITS, payload.iv))
        }
        return aesCipher.doFinal(payload.encryptedPayload)
    }

    private fun oaepParams() = OAEPParameterSpec(
        /* mdName = */ "SHA-256",
        /* mgfName = */ "MGF1",
        /* mgfSpec = */ MGF1ParameterSpec.SHA1,
        /* pSrc = */ PSource.PSpecified.DEFAULT,
    )

    companion object {
        private const val RSA_KEY_SIZE = 2048
        private const val RSA_TRANSFORMATION =
            "${KeyProperties.KEY_ALGORITHM_RSA}/ECB/OAEPWithSHA-256AndMGF1Padding"
        private const val AES_KEY_SIZE = 256
        private const val AES_TRANSFORMATION =
            "${KeyProperties.KEY_ALGORITHM_AES}/${KeyProperties.BLOCK_MODE_GCM}/${KeyProperties.ENCRYPTION_PADDING_NONE}"
        private const val GCM_TAG_LENGTH_BITS = 128
    }
}

package com.example.android.playground.cryptosecurity.domain.repository

import com.example.android.playground.cryptosecurity.domain.model.HybridEncryptedPayload
import com.example.android.playground.cryptosecurity.domain.model.SecureNetworkResponse
import kotlinx.coroutines.flow.Flow

interface CryptoSecurityRepository {

    // --- Scenario 1: Secure network fetch ---
    /** Fetches a simulated encrypted server payload (Base64 IV + ciphertext). */
    suspend fun fetchEncryptedFromServer(): SecureNetworkResponse

    /** Decrypts a [SecureNetworkResponse] and returns the plaintext string. */
    fun decryptNetworkResponse(response: SecureNetworkResponse): String

    // --- Scenario 2: Option B — Android Keystore + AES-GCM + DataStore ---
    suspend fun saveKeystoreEncrypted(key: String, value: String)
    fun loadKeystoreDecrypted(key: String): Flow<String?>
    fun loadKeystoreRawIv(key: String): Flow<ByteArray?>
    fun loadKeystoreRawCiphertext(key: String): Flow<ByteArray?>
    suspend fun clearKeystoreEntry(key: String)

    // --- Scenario 3: Option A — Tink + DataStore ---
    suspend fun saveTinkEncrypted(key: String, value: String)
    fun loadTinkDecrypted(key: String): Flow<String?>
    fun loadTinkRawCiphertext(key: String): Flow<ByteArray?>
    suspend fun clearTinkEntry(key: String)

    // --- Scenario 4: Send encrypted to server ---
    fun encryptForServer(plaintext: String): HybridEncryptedPayload
    fun simulateServerDecrypt(payload: HybridEncryptedPayload): String
}

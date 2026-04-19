package com.example.android.playground.cryptosecurity.data.crypto

import android.content.Context
import androidx.datastore.preferences.core.byteArrayPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.android.playground.cryptosecurity.domain.model.EncryptedData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.keystoreDataStore by preferencesDataStore(name = "keystore_encrypted_store")

/**
 * Option B — Android Keystore + AES-256-GCM + DataStore (manual wiring).
 *
 * What happens step by step:
 *   1. [AesGcmCryptoManager] holds an AES-256 key in the Android Keystore (hardware-backed).
 *   2. encrypt(plaintext) → EncryptedData(iv: ByteArray, ciphertext: ByteArray).
 *   3. Both the IV and ciphertext are stored as raw ByteArray entries in DataStore<Preferences>.
 *   4. On load, IV + ciphertext are read from DataStore and passed back to AesGcmCryptoManager
 *      for decryption — the key never leaves the Keystore secure enclave.
 *
 * Compare with [TinkDataStoreManager] which achieves the same result in fewer lines.
 */
@Singleton
class KeystoreDataStoreManager @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val aesGcmCryptoManager: AesGcmCryptoManager,
) {
    suspend fun encryptAndSave(key: String, value: String) {
        val encrypted = aesGcmCryptoManager.encrypt(value.toByteArray(Charsets.UTF_8))
        context.keystoreDataStore.edit { prefs ->
            prefs[ivKey(key)] = encrypted.iv
            prefs[ciphertextKey(key)] = encrypted.ciphertext
        }
    }

    fun loadAndDecrypt(key: String): Flow<String?> =
        context.keystoreDataStore.data.map { prefs ->
            val iv = prefs[ivKey(key)] ?: return@map null
            val ciphertext = prefs[ciphertextKey(key)] ?: return@map null
            aesGcmCryptoManager.decrypt(EncryptedData(iv = iv, ciphertext = ciphertext))
                .toString(Charsets.UTF_8)
        }

    suspend fun clear(key: String) {
        context.keystoreDataStore.edit { prefs ->
            prefs.remove(ivKey(key))
            prefs.remove(ciphertextKey(key))
        }
    }

    /** Returns the raw IV bytes for the given key, or null if not yet saved. */
    fun loadRawIv(key: String): Flow<ByteArray?> =
        context.keystoreDataStore.data.map { prefs -> prefs[ivKey(key)] }

    /** Returns the raw ciphertext bytes for the given key, or null if not yet saved. */
    fun loadRawCiphertext(key: String): Flow<ByteArray?> =
        context.keystoreDataStore.data.map { prefs -> prefs[ciphertextKey(key)] }

    private fun ivKey(key: String) = byteArrayPreferencesKey("${key}_iv")
    private fun ciphertextKey(key: String) = byteArrayPreferencesKey("${key}_ct")
}

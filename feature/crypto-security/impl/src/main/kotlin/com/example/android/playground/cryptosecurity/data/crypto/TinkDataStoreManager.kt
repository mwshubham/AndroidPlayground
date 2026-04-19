package com.example.android.playground.cryptosecurity.data.crypto

import android.content.Context
import androidx.datastore.preferences.core.byteArrayPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.tinkDataStore by preferencesDataStore(name = "tink_encrypted_store")

/**
 * Option A — Tink + DataStore (idiomatic modern approach).
 *
 * What happens step by step:
 *   1. [AndroidKeysetManager] creates (or loads) a Tink keyset using the AES256-GCM template.
 *      The keyset JSON is stored in DataStore and **wrapped** by an Android Keystore master key,
 *      so it is useless without access to the secure enclave.
 *   2. aead.encrypt(plaintext, associatedData) → ciphertext (Tink prepends its own format/IV).
 *   3. Ciphertext stored as a ByteArray entry in DataStore<Preferences>.
 *   4. On load, aead.decrypt(ciphertext, associatedData) recovers the plaintext.
 *
 * Compare with [KeystoreDataStoreManager] to see how much boilerplate Tink eliminates:
 *   - No manual IV generation or storage
 *   - No explicit Cipher/KeyGenerator wiring
 *   - Keyset rotation and versioning handled automatically
 */
@Singleton
class TinkDataStoreManager @Inject constructor(
    @param:ApplicationContext private val context: Context,
) {
    init {
        AeadConfig.register()
    }

    private val aead: Aead by lazy {
        AndroidKeysetManager.Builder()
            .withSharedPref(context, TINK_KEYSET_NAME, TINK_PREF_FILE)
            .withKeyTemplate(KeyTemplates.get("AES256_GCM"))
            .withMasterKeyUri(MASTER_KEY_URI)
            .build()
            .keysetHandle
            .getPrimitive(Aead::class.java)
    }

    suspend fun encryptAndSave(key: String, value: String) {
        val ciphertext = aead.encrypt(
            value.toByteArray(Charsets.UTF_8),
            key.toByteArray(Charsets.UTF_8),
        )
        context.tinkDataStore.edit { prefs ->
            prefs[ciphertextKey(key)] = ciphertext
        }
    }

    fun loadAndDecrypt(key: String): Flow<String?> =
        context.tinkDataStore.data.map { prefs ->
            val ciphertext = prefs[ciphertextKey(key)] ?: return@map null
            aead.decrypt(ciphertext, key.toByteArray(Charsets.UTF_8))
                .toString(Charsets.UTF_8)
        }

    suspend fun clear(key: String) {
        context.tinkDataStore.edit { prefs ->
            prefs.remove(ciphertextKey(key))
        }
    }

    /** Returns the raw ciphertext bytes for display in the demo UI. */
    fun loadRawCiphertext(key: String): Flow<ByteArray?> =
        context.tinkDataStore.data.map { prefs -> prefs[ciphertextKey(key)] }

    private fun ciphertextKey(key: String) = byteArrayPreferencesKey("tink_${key}")

    companion object {
        private const val TINK_KEYSET_NAME = "tink_aead_keyset"
        private const val TINK_PREF_FILE = "tink_aead_prefs"
        private const val MASTER_KEY_URI = "android-keystore://tink_master_key"
    }
}

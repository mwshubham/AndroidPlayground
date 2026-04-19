package com.example.android.playground.cryptosecurity.data.repository

import com.example.android.playground.cryptosecurity.data.crypto.AesGcmCryptoManager
import com.example.android.playground.cryptosecurity.data.crypto.KeystoreDataStoreManager
import com.example.android.playground.cryptosecurity.data.crypto.RsaCryptoManager
import com.example.android.playground.cryptosecurity.data.crypto.TinkDataStoreManager
import com.example.android.playground.cryptosecurity.data.service.FakeSecureApiService
import com.example.android.playground.cryptosecurity.domain.model.HybridEncryptedPayload
import com.example.android.playground.cryptosecurity.domain.model.SecureNetworkResponse
import com.example.android.playground.cryptosecurity.domain.repository.CryptoSecurityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CryptoSecurityRepositoryImpl @Inject constructor(
    private val fakeApiService: FakeSecureApiService,
    private val aesGcmCryptoManager: AesGcmCryptoManager,
    private val keystoreDataStoreManager: KeystoreDataStoreManager,
    private val tinkDataStoreManager: TinkDataStoreManager,
    private val rsaCryptoManager: RsaCryptoManager,
) : CryptoSecurityRepository {

    override suspend fun fetchEncryptedFromServer(): SecureNetworkResponse =
        fakeApiService.fetchEncryptedResponse()

    override fun decryptNetworkResponse(response: SecureNetworkResponse): String {
        val decrypted = aesGcmCryptoManager.decrypt(response.toEncryptedData())
        return decrypted.toString(Charsets.UTF_8)
    }

    override suspend fun saveKeystoreEncrypted(key: String, value: String) =
        keystoreDataStoreManager.encryptAndSave(key, value)

    override fun loadKeystoreDecrypted(key: String): Flow<String?> =
        keystoreDataStoreManager.loadAndDecrypt(key)

    override fun loadKeystoreRawIv(key: String): Flow<ByteArray?> =
        keystoreDataStoreManager.loadRawIv(key)

    override fun loadKeystoreRawCiphertext(key: String): Flow<ByteArray?> =
        keystoreDataStoreManager.loadRawCiphertext(key)

    override suspend fun clearKeystoreEntry(key: String) =
        keystoreDataStoreManager.clear(key)

    override suspend fun saveTinkEncrypted(key: String, value: String) =
        tinkDataStoreManager.encryptAndSave(key, value)

    override fun loadTinkDecrypted(key: String): Flow<String?> =
        tinkDataStoreManager.loadAndDecrypt(key)

    override fun loadTinkRawCiphertext(key: String): Flow<ByteArray?> =
        tinkDataStoreManager.loadRawCiphertext(key)

    override suspend fun clearTinkEntry(key: String) =
        tinkDataStoreManager.clear(key)

    override fun encryptForServer(plaintext: String): HybridEncryptedPayload =
        rsaCryptoManager.encryptForServer(plaintext.toByteArray(Charsets.UTF_8))

    override fun simulateServerDecrypt(payload: HybridEncryptedPayload): String =
        rsaCryptoManager.simulateServerDecrypt(payload).toString(Charsets.UTF_8)
}

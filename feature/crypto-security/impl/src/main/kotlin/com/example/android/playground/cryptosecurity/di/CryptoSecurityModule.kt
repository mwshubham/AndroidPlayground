package com.example.android.playground.cryptosecurity.di

import android.content.Context
import com.example.android.playground.cryptosecurity.data.crypto.AesGcmCryptoManager
import com.example.android.playground.cryptosecurity.data.crypto.KeystoreDataStoreManager
import com.example.android.playground.cryptosecurity.data.crypto.RsaCryptoManager
import com.example.android.playground.cryptosecurity.data.crypto.TinkDataStoreManager
import com.example.android.playground.cryptosecurity.data.repository.CryptoSecurityRepositoryImpl
import com.example.android.playground.cryptosecurity.data.service.FakeSecureApiService
import com.example.android.playground.cryptosecurity.domain.repository.CryptoSecurityRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface CryptoSecurityModule {
    @Binds
    @Singleton
    fun bindCryptoSecurityRepository(impl: CryptoSecurityRepositoryImpl): CryptoSecurityRepository

    companion object {
        @Provides
        @Singleton
        fun provideAesGcmCryptoManager(): AesGcmCryptoManager = AesGcmCryptoManager()

        @Provides
        @Singleton
        fun provideRsaCryptoManager(): RsaCryptoManager = RsaCryptoManager()

        @Provides
        @Singleton
        fun provideKeystoreDataStoreManager(
            @ApplicationContext context: Context,
            aesGcmCryptoManager: AesGcmCryptoManager,
        ): KeystoreDataStoreManager = KeystoreDataStoreManager(context, aesGcmCryptoManager)

        @Provides
        @Singleton
        fun provideTinkDataStoreManager(
            @ApplicationContext context: Context,
        ): TinkDataStoreManager = TinkDataStoreManager(context)

        @Provides
        @Singleton
        fun provideFakeSecureApiService(aesGcmCryptoManager: AesGcmCryptoManager): FakeSecureApiService = FakeSecureApiService(aesGcmCryptoManager)
    }
}

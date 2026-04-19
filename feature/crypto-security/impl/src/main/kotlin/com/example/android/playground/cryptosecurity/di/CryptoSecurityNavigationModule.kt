package com.example.android.playground.cryptosecurity.di

import com.example.android.playground.core.navigation.AppNavigator
import com.example.android.playground.core.navigation.EntryProviderInstaller
import com.example.android.playground.cryptosecurity.api.CryptoSecurityHomeRoute
import com.example.android.playground.cryptosecurity.api.KeystoreStorageDemoRoute
import com.example.android.playground.cryptosecurity.api.SecureNetworkDemoRoute
import com.example.android.playground.cryptosecurity.api.SendEncryptedDemoRoute
import com.example.android.playground.cryptosecurity.api.TinkStorageDemoRoute
import com.example.android.playground.cryptosecurity.presentation.screen.CryptoSecurityHomeScreen
import com.example.android.playground.cryptosecurity.presentation.screen.KeystoreStorageDemoScreen
import com.example.android.playground.cryptosecurity.presentation.screen.SecureNetworkDemoScreen
import com.example.android.playground.cryptosecurity.presentation.screen.SendEncryptedDemoScreen
import com.example.android.playground.cryptosecurity.presentation.screen.TinkStorageDemoScreen
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityRetainedComponent::class)
object CryptoSecurityNavigationModule {

    @IntoSet
    @Provides
    fun provideEntryProviderInstaller(navigator: AppNavigator): EntryProviderInstaller =
        {
            entry<CryptoSecurityHomeRoute> {
                CryptoSecurityHomeScreen(
                    onNavigateBack = { navigator.goBack() },
                    onSecureNetworkClick = { navigator.goTo(SecureNetworkDemoRoute) },
                    onKeystoreStorageClick = { navigator.goTo(KeystoreStorageDemoRoute) },
                    onTinkStorageClick = { navigator.goTo(TinkStorageDemoRoute) },
                    onSendEncryptedClick = { navigator.goTo(SendEncryptedDemoRoute) },
                )
            }

            entry<SecureNetworkDemoRoute> {
                SecureNetworkDemoScreen(
                    onNavigateBack = { navigator.goBack() },
                )
            }

            entry<KeystoreStorageDemoRoute> {
                KeystoreStorageDemoScreen(
                    onNavigateBack = { navigator.goBack() },
                )
            }

            entry<TinkStorageDemoRoute> {
                TinkStorageDemoScreen(
                    onNavigateBack = { navigator.goBack() },
                )
            }

            entry<SendEncryptedDemoRoute> {
                SendEncryptedDemoScreen(
                    onNavigateBack = { navigator.goBack() },
                )
            }
        }
}

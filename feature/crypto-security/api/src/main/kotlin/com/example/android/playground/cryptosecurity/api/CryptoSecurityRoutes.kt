package com.example.android.playground.cryptosecurity.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object CryptoSecurityHomeRoute : NavKey

@Serializable
data object SecureNetworkDemoRoute : NavKey

@Serializable
data object KeystoreStorageDemoRoute : NavKey

@Serializable
data object TinkStorageDemoRoute : NavKey

@Serializable
data object SendEncryptedDemoRoute : NavKey

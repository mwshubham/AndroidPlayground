package com.example.android.playground.cryptosecurity.presentation.state

import com.example.android.playground.cryptosecurity.domain.model.SecureNetworkResponse

data class SecureNetworkDemoState(
    val isLoading: Boolean = false,
    val serverPayload: SecureNetworkResponse? = null,
    val decryptedMessage: String? = null,
    val error: String? = null,
)

package com.example.android.playground.cryptosecurity.presentation.state

import com.example.android.playground.cryptosecurity.domain.model.HybridEncryptedPayload

data class SendEncryptedDemoState(
    val inputMessage: String = "Hello Server! Keep this private.",
    val isEncrypting: Boolean = false,
    val encryptedPayload: HybridEncryptedPayload? = null,
    val serverDecryptedMessage: String? = null,
    val error: String? = null,
)

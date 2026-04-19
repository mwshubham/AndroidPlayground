package com.example.android.playground.cryptosecurity.presentation.intent

sealed interface SendEncryptedDemoIntent {
    data class MessageChanged(val message: String) : SendEncryptedDemoIntent
    data object EncryptAndSend : SendEncryptedDemoIntent
    data object SimulateServerDecrypt : SendEncryptedDemoIntent
    data object Clear : SendEncryptedDemoIntent
    data object NavigateBack : SendEncryptedDemoIntent
}

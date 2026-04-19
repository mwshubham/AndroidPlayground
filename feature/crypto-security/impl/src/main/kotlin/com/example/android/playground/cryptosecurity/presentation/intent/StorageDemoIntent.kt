package com.example.android.playground.cryptosecurity.presentation.intent

sealed interface StorageDemoIntent {
    data class KeyChanged(val key: String) : StorageDemoIntent
    data class ValueChanged(val value: String) : StorageDemoIntent
    data object EncryptSave : StorageDemoIntent
    data object LoadDecrypt : StorageDemoIntent
    data object Clear : StorageDemoIntent
    data object NavigateBack : StorageDemoIntent
}

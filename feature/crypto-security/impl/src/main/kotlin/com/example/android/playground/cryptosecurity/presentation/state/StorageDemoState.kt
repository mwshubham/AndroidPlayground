package com.example.android.playground.cryptosecurity.presentation.state

data class StorageDemoState(
    val inputKey: String = "my_secret_key",
    val inputValue: String = "",
    val isSaving: Boolean = false,
    val isLoading: Boolean = false,
    val savedIvHex: String? = null,
    val savedCiphertextHex: String? = null,
    val loadedValue: String? = null,
    val error: String? = null,
)

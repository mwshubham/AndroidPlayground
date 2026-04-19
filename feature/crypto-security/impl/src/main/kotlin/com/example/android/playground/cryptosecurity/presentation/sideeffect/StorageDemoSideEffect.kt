package com.example.android.playground.cryptosecurity.presentation.sideeffect

sealed interface StorageDemoSideEffect {
    data class ShowError(val message: String) : StorageDemoSideEffect
    data object SavedSuccessfully : StorageDemoSideEffect
    data object NavigateBack : StorageDemoSideEffect
}

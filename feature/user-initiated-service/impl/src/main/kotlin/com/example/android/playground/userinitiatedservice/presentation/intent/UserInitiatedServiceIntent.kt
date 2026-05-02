package com.example.android.playground.userinitiatedservice.presentation.intent

sealed interface UserInitiatedServiceIntent {
    data object StartTransfer : UserInitiatedServiceIntent

    data object ClearAll : UserInitiatedServiceIntent

    data object NavigateBack : UserInitiatedServiceIntent
}

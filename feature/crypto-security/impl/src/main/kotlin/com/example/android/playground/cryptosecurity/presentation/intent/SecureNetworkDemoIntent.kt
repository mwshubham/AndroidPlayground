package com.example.android.playground.cryptosecurity.presentation.intent

sealed interface SecureNetworkDemoIntent {
    data object FetchData : SecureNetworkDemoIntent
    data object DecryptPayload : SecureNetworkDemoIntent
    data object Clear : SecureNetworkDemoIntent
    data object NavigateBack : SecureNetworkDemoIntent
}

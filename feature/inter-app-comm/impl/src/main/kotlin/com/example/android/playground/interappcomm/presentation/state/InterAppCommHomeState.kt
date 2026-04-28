package com.example.android.playground.interappcomm.presentation.state

import com.example.android.playground.interappcomm.domain.model.IpcChannel

data class InterAppCommHomeState(
    val isLoading: Boolean = false,
    val currentPackage: String = "",
    val targetPackage: String = "",
    val isOtherAppInstalled: Boolean = false,
    val currentAppSignature: String? = null,
    val otherAppSignature: String? = null,
    val signaturesMatch: Boolean = false,
    val ipcChannels: List<IpcChannel> = emptyList(),
    val error: String? = null,
)

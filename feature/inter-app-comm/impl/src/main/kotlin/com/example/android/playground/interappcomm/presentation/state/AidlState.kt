package com.example.android.playground.interappcomm.presentation.state

import com.example.android.playground.interappcomm.domain.model.IpcMessage

data class AidlState(
    val isConnected: Boolean = false,
    val isConnecting: Boolean = false,
    val currentPackage: String = "",
    val targetPackage: String = "",
    val inputText: String = "",
    val pingResult: String? = null,
    val remoteMessages: List<String> = emptyList(),
    val messages: List<IpcMessage> = emptyList(),
    val error: String? = null,
)

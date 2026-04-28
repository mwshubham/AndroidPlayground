package com.example.android.playground.interappcomm.presentation.state

import com.example.android.playground.interappcomm.domain.model.IpcMessage

data class BroadcastState(
    val isLoading: Boolean = false,
    val currentPackage: String = "",
    val targetPackage: String = "",
    val inputText: String = "",
    val messages: List<IpcMessage> = emptyList(),
    val error: String? = null,
)

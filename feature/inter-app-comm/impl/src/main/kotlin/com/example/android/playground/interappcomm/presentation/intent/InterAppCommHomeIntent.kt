package com.example.android.playground.interappcomm.presentation.intent

import com.example.android.playground.interappcomm.domain.model.IpcMethod

sealed interface InterAppCommHomeIntent {
    data object LoadData : InterAppCommHomeIntent
    data class OnChannelClicked(val method: IpcMethod) : InterAppCommHomeIntent
    data object NavigateBack : InterAppCommHomeIntent
}

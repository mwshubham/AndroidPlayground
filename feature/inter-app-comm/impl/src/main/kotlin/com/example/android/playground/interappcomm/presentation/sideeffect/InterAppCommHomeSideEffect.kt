package com.example.android.playground.interappcomm.presentation.sideeffect

import com.example.android.playground.interappcomm.domain.model.IpcMethod

sealed interface InterAppCommHomeSideEffect {
    data object NavigateBack : InterAppCommHomeSideEffect

    data class NavigateToChannel(
        val method: IpcMethod,
    ) : InterAppCommHomeSideEffect
}

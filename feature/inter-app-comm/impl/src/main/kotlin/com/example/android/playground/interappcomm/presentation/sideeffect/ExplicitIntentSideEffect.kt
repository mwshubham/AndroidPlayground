package com.example.android.playground.interappcomm.presentation.sideeffect

import android.content.Intent

sealed interface ExplicitIntentSideEffect {
    data object NavigateBack : ExplicitIntentSideEffect
    data class LaunchIntent(val intent: Intent) : ExplicitIntentSideEffect
    data class ShowMessage(val message: String) : ExplicitIntentSideEffect
}

package com.example.android.playground.interappcomm.presentation.intent

sealed interface ExplicitIntentIntent {
    data object LoadData : ExplicitIntentIntent

    data object LaunchOtherApp : ExplicitIntentIntent

    data object NavigateBack : ExplicitIntentIntent
}

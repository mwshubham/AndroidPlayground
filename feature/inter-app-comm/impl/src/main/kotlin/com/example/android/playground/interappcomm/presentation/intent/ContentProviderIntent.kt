package com.example.android.playground.interappcomm.presentation.intent

sealed interface ContentProviderIntent {
    data object LoadData : ContentProviderIntent

    data class OnInputChanged(
        val text: String,
    ) : ContentProviderIntent

    data object WriteToOtherApp : ContentProviderIntent

    data object ReadFromOtherApp : ContentProviderIntent

    data object ClearOwnMessages : ContentProviderIntent

    data object NavigateBack : ContentProviderIntent
}

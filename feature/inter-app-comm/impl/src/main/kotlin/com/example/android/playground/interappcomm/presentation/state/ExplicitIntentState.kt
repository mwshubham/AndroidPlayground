package com.example.android.playground.interappcomm.presentation.state

data class ExplicitIntentState(
    val isLoading: Boolean = false,
    val currentPackage: String = "",
    val targetPackage: String = "",
    val isOtherAppInstalled: Boolean = false,
    val lastLaunchResult: String? = null,
    val error: String? = null,
)

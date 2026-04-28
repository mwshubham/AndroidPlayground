package com.example.android.playground.interappcomm.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object InterAppCommHomeRoute : NavKey

@Serializable
data object ExplicitIntentRoute : NavKey

@Serializable
data object BroadcastRoute : NavKey

@Serializable
data object ContentProviderRoute : NavKey

@Serializable
data object MessengerRoute : NavKey

@Serializable
data object AidlRoute : NavKey

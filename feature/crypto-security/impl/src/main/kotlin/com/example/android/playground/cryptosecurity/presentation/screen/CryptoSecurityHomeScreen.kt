package com.example.android.playground.cryptosecurity.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.android.playground.core.ui.TrackScreenViewEvent
import com.example.android.playground.cryptosecurity.presentation.component.CryptoSecurityHomeContent

@Composable
fun CryptoSecurityHomeScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    onSecureNetworkClick: () -> Unit = {},
    onKeystoreStorageClick: () -> Unit = {},
    onTinkStorageClick: () -> Unit = {},
    onSendEncryptedClick: () -> Unit = {},
) {
    TrackScreenViewEvent(screenName = "CryptoSecurityHomeScreen")

    CryptoSecurityHomeContent(
        onNavigateBack = onNavigateBack,
        onSecureNetworkClick = onSecureNetworkClick,
        onKeystoreStorageClick = onKeystoreStorageClick,
        onTinkStorageClick = onTinkStorageClick,
        onSendEncryptedClick = onSendEncryptedClick,
        modifier = modifier,
    )
}

package com.example.android.playground.userinitiatedservice.presentation.component

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Banner shown at the top of the screen that informs the user which transfer mechanism
 * is active on this device — UIJ (API 34+) or WorkManager Expedited fallback.
 *
 * Color coding:
 *  - Green  → User-Initiated Job is active (best experience)
 *  - Amber  → Fallback path; FGS notification will appear in the notification shade
 */
@Composable
fun ApiLevelBanner(
    isApi34Plus: Boolean,
    modifier: Modifier = Modifier,
) {
    val containerColor =
        if (isApi34Plus) {
            Color(0xFF1B5E20).copy(alpha = 0.12f)
        } else {
            Color(0xFFE65100).copy(alpha = 0.12f)
        }

    val iconTint =
        if (isApi34Plus) Color(0xFF2E7D32) else Color(0xFFBF360C)

    val headline =
        if (isApi34Plus) {
            "API ${Build.VERSION.SDK_INT} — User-Initiated Job active"
        } else {
            "API ${Build.VERSION.SDK_INT} — WorkManager Expedited fallback"
        }

    val detail =
        if (isApi34Plus) {
            "JobScheduler.setUserInitiated(true) · Job appears in Task Manager · ~10 min grace period · No notification required"
        } else {
            "WorkManager Expedited + DATA_SYNC foreground service · Notification visible in shade · 10 sec grace period · UIJ requires API 34"
        }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Icon(
                imageVector = if (isApi34Plus) Icons.Filled.CheckCircle else Icons.Filled.Info,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(20.dp),
            )
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = headline,
                    style = MaterialTheme.typography.labelMedium,
                    color = iconTint,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = detail,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "API 34+ — UIJ active")
@Composable
private fun ApiLevelBannerApi34Preview() {
    ApiLevelBanner(isApi34Plus = true)
}

@Preview(showBackground = true, name = "Below API 34 — Fallback")
@Composable
private fun ApiLevelBannerFallbackPreview() {
    ApiLevelBanner(isApi34Plus = false)
}


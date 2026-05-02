package com.example.android.playground.userinitiatedservice.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Educational card that explains User-Initiated Jobs from first principles.
 * Collapsible so it doesn't dominate the screen once the user has read it.
 *
 * Covers:
 *  - What UIJ is
 *  - Why it was introduced (solving the FGS 10-second grace-period problem on API 34+)
 *  - OS gesture enforcement (SecurityException if not in user-gesture window)
 *  - Task Manager visibility vs notification shade
 *  - API level requirement and alternatives
 *  - The comparison table between UIJ, WorkManager Expedited, and Regular FGS
 */
@Composable
fun ConceptCard(modifier: Modifier = Modifier) {
    var expanded by rememberSaveable { mutableStateOf(true) }

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // ---- Header row (always visible) ----
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .clickable { expanded = !expanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "What is a User-Initiated Job?",
                    style = MaterialTheme.typography.titleSmall,
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                )
            }

            // ---- Collapsible body ----
            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier.padding(top = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    ConceptPoint(
                        label = "What",
                        text =
                            "A JobScheduler job type (API 34 / Android 14) designed for long-running " +
                                "data transfers that are explicitly started by the user — e.g. uploading a video " +
                                "after tapping a share button, downloading a file the user just requested.",
                    )
                    ConceptPoint(
                        label = "Why it was needed",
                        text =
                            "On API 34+, the OS enforces aggressive FGS visibility: if a foreground " +
                                "service doesn't display a notification within 10 seconds, it is stopped. " +
                                "UIJ replaces the notification-in-shade model with Task Manager visibility, " +
                                "giving the job ~10 minutes of grace before any OS intervention.",
                    )
                    ConceptPoint(
                        label = "OS gesture enforcement",
                        text =
                            "JobScheduler validates that schedule() is called within the user-gesture " +
                                "call stack (e.g., inside a button click handler). If called from a background " +
                                "component (alarm, boot receiver, etc.) it throws SecurityException at schedule " +
                                "time. This makes silent misuse of UIJ for background work physically impossible.",
                    )
                    ConceptPoint(
                        label = "Task Manager visibility",
                        text =
                            "Active UIJ jobs appear in the system Task Manager (long-press Recents on " +
                                "Android 14+). Users can tap 'Stop' to cancel the job, which fires onStopJob() " +
                                "in the service — you can see this as CANCELLED items in the list below.",
                    )
                    ConceptPoint(
                        label = "Network constraint",
                        text =
                            "setRequiredNetworkType() is mandatory for UIJ — schedule() throws " +
                                "IllegalArgumentException if omitted. This ensures the OS can stop the job " +
                                "cleanly when network conditions change, and resume it later.",
                    )
                    ConceptPoint(
                        label = "API level & fallback",
                        text =
                            "Requires API 34 (Android 14). This demo detects the API level at runtime " +
                                "and falls back to WorkManager Expedited with a DATA_SYNC foreground service " +
                                "on API < 34 — same end result, different OS mechanisms.",
                    )
                    ConceptPoint(
                        label = "What it is NOT",
                        text =
                            "UIJ is not a replacement for all background work. It is specifically for " +
                                "user-triggered data transfers. For periodic sync, use WorkManager. For " +
                                "media playback, use a media foreground service. For always-on services, " +
                                "use regular FGS.",
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(4.dp))

                    // ---- Comparison table ----
                    Text(
                        text = "How UIJ compares",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    ComparisonTableRow(
                        aspect = "",
                        uij = "UIJ",
                        wmExpedited = "WM Expedited",
                        regularFgs = "Regular FGS",
                        isHeader = true,
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 2.dp))
                    ComparisonTableRow("Min API", "34", "Any", "26")
                    ComparisonTableRow("User gesture", "✅ Required", "❌ Optional", "❌ Optional")
                    ComparisonTableRow("Visible via", "Task Manager", "Notification", "Notification")
                    ComparisonTableRow("Grace period", "~10 min", "10 sec", "10 sec")
                    ComparisonTableRow("Network priority", "Elevated", "Normal", "Normal")
                    ComparisonTableRow("Expedited quota", "❌ None", "✅ Limited", "❌ None")
                    ComparisonTableRow("User can stop via", "Task Manager", "Notif action", "Notif action")
                    ComparisonTableRow("POST_NOTIFICATIONS", "❌ Not needed", "✅ API 33+", "✅ API 33+")
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "ConceptCard — Expanded")
@Composable
private fun ConceptCardExpandedPreview() {
    ConceptCard()
}

@Preview(showBackground = true, name = "ConceptCard — Collapsed")
@Composable
private fun ConceptCardCollapsedPreview() {
    // rememberSaveable initialises to true; wrap in a stateless shell to force collapsed state
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "What is a User-Initiated Job?",
                    style = MaterialTheme.typography.titleSmall,
                )
                Icon(
                    imageVector = Icons.Default.ExpandMore,
                    contentDescription = "Expand",
                )
            }
        }
    }
}

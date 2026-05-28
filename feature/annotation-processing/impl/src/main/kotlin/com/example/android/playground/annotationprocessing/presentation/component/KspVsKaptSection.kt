package com.example.android.playground.annotationprocessing.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.android.playground.core.ui.preview.ComponentPreview
import com.example.android.playground.core.ui.preview.PreviewContainer

private val comparisonRows =
    listOf(
        KspVsKaptRowData("Language", "Kotlin-first API", "Java APT API"),
        KspVsKaptRowData("Speed", "~2× faster", "Slower (stub gen)"),
        KspVsKaptRowData("Incremental", "Yes (built-in)", "Limited"),
        KspVsKaptRowData("Kotlin types", "First-class", "Erased to Java"),
        KspVsKaptRowData("Multi-round", "Yes", "Yes"),
        KspVsKaptRowData("Status", "Recommended", "Deprecated in AGP9"),
    )

@Composable
internal fun KspVsKaptSection(modifier: Modifier = Modifier) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "KSP vs KAPT",
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text =
                "KAPT (Kotlin Annotation Processing Tool) was the original bridge between " +
                    "Kotlin and the Java Annotation Processing API. KSP (Kotlin Symbol Processing) " +
                    "is its modern replacement with a native Kotlin API and significant performance " +
                    "improvements.",
            style = MaterialTheme.typography.bodyMedium,
        )

        // Header row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "Aspect",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = "KSP",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = "KAPT",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.weight(1f),
            )
        }

        HorizontalDivider()

        comparisonRows.forEach { row ->
            KspVsKaptRow(row = row)
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        }

        Text(
            text =
                "This project (and this demo) use KSP exclusively. The @AutoToString " +
                    "processor is a KSP SymbolProcessor — no Java stub generation required.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

// ---- Previews ----

@ComponentPreview
@Composable
private fun KspVsKaptSectionPreview() {
    PreviewContainer {
        KspVsKaptSection()
    }
}

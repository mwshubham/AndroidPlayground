package com.example.android.playground.userinitiatedservice.presentation.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.tooling.preview.Preview

@Composable
internal fun ComparisonTableRow(
    aspect: String,
    uij: String,
    wmExpedited: String,
    regularFgs: String,
    isHeader: Boolean = false,
    modifier: Modifier = Modifier,
) {
    val textStyle =
        if (isHeader) {
            MaterialTheme.typography.labelSmall
        } else {
            MaterialTheme.typography.bodySmall
        }
    val uijColor =
        if (isHeader) {
            MaterialTheme.colorScheme.onSurfaceVariant
        } else {
            MaterialTheme.colorScheme.primary
        }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1.4f),
            text = aspect,
            style = textStyle,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            modifier = Modifier.weight(1f),
            text = uij,
            style = textStyle,
            color = uijColor,
            textAlign = TextAlign.Center,
        )
        Text(
            modifier = Modifier.weight(1.1f),
            text = wmExpedited,
            style = textStyle,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Text(
            modifier = Modifier.weight(1.1f),
            text = regularFgs,
            style = textStyle,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(showBackground = true, name = "ComparisonTableRow — Header")
@Composable
private fun ComparisonTableRowHeaderPreview() {
    ComparisonTableRow(
        aspect = "",
        uij = "UIJ",
        wmExpedited = "WM Expedited",
        regularFgs = "Regular FGS",
        isHeader = true,
    )
}

@Preview(showBackground = true, name = "ComparisonTableRow — Data row")
@Composable
private fun ComparisonTableRowDataPreview() {
    ComparisonTableRow(
        aspect = "Min API",
        uij = "34",
        wmExpedited = "Any",
        regularFgs = "26",
    )
}


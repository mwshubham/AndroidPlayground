package com.example.android.playground.cryptosecurity.presentation.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android.playground.core.ui.preview.ComponentPreview
import com.example.android.playground.core.ui.preview.PreviewContainer

/**
 * Displays a labeled hex/base64 value in a monospace font.
 * Long values are collapsed by default and can be expanded by tapping.
 */
@Composable
fun CryptoInfoCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 2.dp,
    ) {
        Column(
            modifier = Modifier
                .animateContentSize()
                .padding(12.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f),
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Text(
                text = value,
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp,
                lineHeight = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(4.dp),
                    )
                    .padding(8.dp),
                maxLines = if (expanded) Int.MAX_VALUE else 3,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

// ---- Previews ----

@ComponentPreview
@Composable
private fun CryptoInfoCardCollapsedPreview() {
    PreviewContainer {
        CryptoInfoCard(
            label = "IV (Base64)",
            value = "A1B2C3D4E5F6A1B2C3D4E5F6A1B2C3D4=",
        )
    }
}

@ComponentPreview
@Composable
private fun CryptoInfoCardLongValuePreview() {
    PreviewContainer {
        CryptoInfoCard(
            label = "Ciphertext (Base64)",
            value = "VGhpcyBpcyBhIHZlcnkgbG9uZyBjaXBoZXJ0ZXh0IHZhbHVlIHRoYXQgc2hvdWxkIGJlIHRydW5jYXRlZCBieSBkZWZhdWx0IHVubGVzcyB0aGUgdXNlciB0YXBzIHRvIGV4cGFuZCBpdA==",
        )
    }
}

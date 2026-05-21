package com.example.android.playground.flowlivedata.presentation.component

import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.android.playground.core.ui.preview.DualThemePreview
import com.example.android.playground.core.ui.preview.PreviewContainer
import com.example.android.playground.flowlivedata.presentation.model.StreamType

@Composable
internal fun StreamTypeTabRow(
    selectedTab: StreamType,
    onTabSelected: (StreamType) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tabs = StreamType.entries
    ScrollableTabRow(
        selectedTabIndex = tabs.indexOf(selectedTab),
        modifier = modifier,
    ) {
        tabs.forEach { tab ->
            Tab(
                selected = tab == selectedTab,
                onClick = { onTabSelected(tab) },
                text = {
                    Text(text = tab.displayName())
                },
            )
        }
    }
}

private fun StreamType.displayName(): String =
    when (this) {
        StreamType.STATE_FLOW -> "StateFlow"
        StreamType.SHARED_FLOW -> "SharedFlow"
        StreamType.LIVE_DATA -> "LiveData"
        StreamType.CHANNEL -> "Channel"
    }

@DualThemePreview
@Composable
private fun StreamTypeTabRowPreview() {
    PreviewContainer {
        StreamTypeTabRow(
            selectedTab = StreamType.STATE_FLOW,
            onTabSelected = {},
        )
    }
}

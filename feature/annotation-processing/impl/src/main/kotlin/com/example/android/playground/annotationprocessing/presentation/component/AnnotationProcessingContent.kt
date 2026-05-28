package com.example.android.playground.annotationprocessing.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.android.playground.annotationprocessing.presentation.intent.AnnotationProcessingIntent
import com.example.android.playground.annotationprocessing.presentation.state.AnnotationProcessingState
import com.example.android.playground.annotationprocessing.presentation.state.AnnotationProcessingTab
import com.example.android.playground.core.ui.components.AppTopAppBar
import com.example.android.playground.core.ui.preview.PreviewContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AnnotationProcessingContent(
    state: AnnotationProcessingState,
    onIntent: (AnnotationProcessingIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            AppTopAppBar(
                title = "Annotation Processing",
                onNavigationClick = { onIntent(AnnotationProcessingIntent.NavigateBack) },
            )
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
        ) {
            val tabs = AnnotationProcessingTab.entries
            TabRow(
                selectedTabIndex = tabs.indexOf(state.selectedTab),
                modifier = Modifier.fillMaxWidth(),
            ) {
                tabs.forEach { tab ->
                    Tab(
                        selected = state.selectedTab == tab,
                        onClick = { onIntent(AnnotationProcessingIntent.SelectTab(tab)) },
                        text = { Text(text = tab.label) },
                    )
                }
            }

            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
            ) {
                when (state.selectedTab) {
                    AnnotationProcessingTab.OVERVIEW -> OverviewSection()
                    AnnotationProcessingTab.PIPELINE -> PipelineSection()
                    AnnotationProcessingTab.KSP_VS_KAPT -> KspVsKaptSection()
                    AnnotationProcessingTab.LIVE_DEMO -> LiveDemoSection()
                }
            }
        }
    }
}

// ---- Previews ----

@Preview
@Composable
private fun AnnotationProcessingContentPreview() {
    PreviewContainer {
        AnnotationProcessingContent(
            state = AnnotationProcessingState(),
            onIntent = {},
        )
    }
}

package com.example.android.playground.annotationprocessing.presentation.intent

import com.example.android.playground.annotationprocessing.presentation.state.AnnotationProcessingTab

sealed interface AnnotationProcessingIntent {
    data class SelectTab(
        val tab: AnnotationProcessingTab,
    ) : AnnotationProcessingIntent

    data object NavigateBack : AnnotationProcessingIntent
}

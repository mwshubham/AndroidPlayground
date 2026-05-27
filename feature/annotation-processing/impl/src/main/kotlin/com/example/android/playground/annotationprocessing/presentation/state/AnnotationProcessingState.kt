package com.example.android.playground.annotationprocessing.presentation.state

data class AnnotationProcessingState(
    val selectedTab: AnnotationProcessingTab = AnnotationProcessingTab.OVERVIEW,
)

enum class AnnotationProcessingTab(
    val label: String,
) {
    OVERVIEW("Overview"),
    PIPELINE("Pipeline"),
    KSP_VS_KAPT("KSP vs KAPT"),
    LIVE_DEMO("Live Demo"),
}

package com.example.android.playground.annotationprocessing.presentation.sideeffect

sealed interface AnnotationProcessingSideEffect {
    data object NavigateBack : AnnotationProcessingSideEffect
}

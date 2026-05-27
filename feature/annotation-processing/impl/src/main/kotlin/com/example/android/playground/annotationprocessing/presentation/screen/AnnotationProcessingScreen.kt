package com.example.android.playground.annotationprocessing.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android.playground.annotationprocessing.presentation.component.AnnotationProcessingContent
import com.example.android.playground.annotationprocessing.presentation.sideeffect.AnnotationProcessingSideEffect
import com.example.android.playground.annotationprocessing.presentation.viewmodel.AnnotationProcessingViewModel
import com.example.android.playground.core.ui.TrackScreenViewEvent

@Composable
fun AnnotationProcessingScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AnnotationProcessingViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    TrackScreenViewEvent(screenName = "AnnotationProcessingScreen")

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is AnnotationProcessingSideEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    AnnotationProcessingContent(
        state = state,
        onIntent = viewModel::handleIntent,
        modifier = modifier,
    )
}

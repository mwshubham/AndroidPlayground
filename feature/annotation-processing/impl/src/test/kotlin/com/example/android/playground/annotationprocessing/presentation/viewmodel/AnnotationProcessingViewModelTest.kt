package com.example.android.playground.annotationprocessing.presentation.viewmodel
import app.cash.turbine.test
import com.example.android.playground.annotationprocessing.presentation.intent.AnnotationProcessingIntent
import com.example.android.playground.annotationprocessing.presentation.sideeffect.AnnotationProcessingSideEffect
import com.example.android.playground.annotationprocessing.presentation.state.AnnotationProcessingState
import com.example.android.playground.annotationprocessing.presentation.state.AnnotationProcessingTab
import com.example.android.playground.core.testing.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class AnnotationProcessingViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun createViewModel() = AnnotationProcessingViewModel()

    @Test
    fun initialStateEqualsDefault() =
        runTest {
            val viewModel = createViewModel()
            viewModel.state.test {
                assertEquals(AnnotationProcessingState(), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun selectTabUpdatesPipelineTab() =
        runTest {
            val viewModel = createViewModel()
            viewModel.handleIntent(AnnotationProcessingIntent.SelectTab(AnnotationProcessingTab.PIPELINE))
            viewModel.state.test {
                assertEquals(AnnotationProcessingTab.PIPELINE, awaitItem().selectedTab)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun selectTabUpdatesKspVsKaptTab() =
        runTest {
            val viewModel = createViewModel()
            viewModel.handleIntent(AnnotationProcessingIntent.SelectTab(AnnotationProcessingTab.KSP_VS_KAPT))
            viewModel.state.test {
                assertEquals(AnnotationProcessingTab.KSP_VS_KAPT, awaitItem().selectedTab)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun navigateBackEmitsNavigateBackSideEffect() =
        runTest {
            val viewModel = createViewModel()
            viewModel.handleIntent(AnnotationProcessingIntent.NavigateBack)
            viewModel.sideEffect.test {
                assertEquals(AnnotationProcessingSideEffect.NavigateBack, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }
}

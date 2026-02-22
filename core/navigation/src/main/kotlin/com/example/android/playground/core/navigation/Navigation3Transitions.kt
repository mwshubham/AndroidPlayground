package com.example.android.playground.core.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.scene.Scene

/**
 * Navigation 3 compatible transitions
 */
object Navigation3Transitions {

    /**
     * Default animation duration in milliseconds
     */
    private const val DEFAULT_ANIMATION_DURATION = 300

    /**
     * Predictive pop animation duration (typically faster)
     */
    private const val PREDICTIVE_POP_DURATION = 200

    /**
     * Creates a horizontal slide transition for Navigation 3
     */
    fun horizontalSlideTransition(
        durationMillis: Int = DEFAULT_ANIMATION_DURATION
    ): AnimatedContentTransitionScope<Scene<NavKey>>.() -> ContentTransform =
        createHorizontalSlideTransition(
            enterFromRight = true,
            durationMillis = durationMillis
        )

    /**
     * Creates a horizontal slide transition for popping (going back)
     */
    fun horizontalSlidePopTransition(
        durationMillis: Int = DEFAULT_ANIMATION_DURATION
    ): AnimatedContentTransitionScope<Scene<NavKey>>.() -> ContentTransform =
        createHorizontalSlideTransition(
            enterFromRight = false,
            durationMillis = durationMillis
        )

    /**
     * Creates a predictive pop transition for gesture-based navigation
     * This has a different signature than regular transitions as it receives an offset parameter
     */
    fun predictivePopTransition(
        durationMillis: Int = PREDICTIVE_POP_DURATION
    ): (AnimatedContentTransitionScope<Scene<NavKey>>, Int) -> ContentTransform = { scope, offset ->
        with(scope) {
            createHorizontalSlideTransition(
                enterFromRight = false,
                durationMillis = durationMillis,
                offset = offset
            ).invoke(this)
        }
    }

    /**
     * Private helper function to create horizontal slide transitions
     * Promotes code reusability by centralizing transition logic
     * @param offset Optional offset for predictive transitions, defaults to 0
     */
    private fun createHorizontalSlideTransition(
        enterFromRight: Boolean,
        durationMillis: Int,
        offset: Int = 0
    ): AnimatedContentTransitionScope<Scene<NavKey>>.() -> ContentTransform = {
        val (enterOffset, exitOffset) = if (enterFromRight) {
            { fullWidth: Int -> fullWidth + offset } to { fullWidth: Int -> -fullWidth + offset }
        } else {
            { fullWidth: Int -> -fullWidth + offset } to { fullWidth: Int -> fullWidth + offset }
        }

        slideInHorizontally(
            initialOffsetX = enterOffset,
            animationSpec = tween(durationMillis)
        ) togetherWith slideOutHorizontally(
            targetOffsetX = exitOffset,
            animationSpec = tween(durationMillis)
        )
    }
}

package com.example.android.playground.core.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween

/**
 * Utility object containing reusable navigation transition animations
 */
object NavigationAnimations {

    /**
     * Default animation duration in milliseconds
     */
    private const val DEFAULT_ANIMATION_DURATION = 300

    /**
     * Creates a horizontal slide transition for navigating forward
     * @param durationMillis Animation duration in milliseconds
     */
    fun slideInFromRight(
        durationMillis: Int = DEFAULT_ANIMATION_DURATION
    ): AnimatedContentTransitionScope<*>.() -> EnterTransition = {
        slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Companion.Start,
            animationSpec = tween(durationMillis)
        )
    }

    /**
     * Creates a horizontal slide transition for navigating back
     * @param durationMillis Animation duration in milliseconds
     */
    fun slideInFromLeft(
        durationMillis: Int = DEFAULT_ANIMATION_DURATION
    ): AnimatedContentTransitionScope<*>.() -> EnterTransition = {
        slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Companion.End,
            animationSpec = tween(durationMillis)
        )
    }

    /**
     * Creates a horizontal slide transition for exiting when navigating forward
     * @param durationMillis Animation duration in milliseconds
     */
    fun slideOutToLeft(
        durationMillis: Int = DEFAULT_ANIMATION_DURATION
    ): AnimatedContentTransitionScope<*>.() -> ExitTransition = {
        slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Companion.Start,
            animationSpec = tween(durationMillis)
        )
    }

    /**
     * Creates a horizontal slide transition for exiting when navigating back
     * @param durationMillis Animation duration in milliseconds
     */
    fun slideOutToRight(
        durationMillis: Int = DEFAULT_ANIMATION_DURATION
    ): AnimatedContentTransitionScope<*>.() -> ExitTransition = {
        slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Companion.End,
            animationSpec = tween(durationMillis)
        )
    }
}

/**
 * Data class representing a complete set of navigation transitions
 * @param enterTransition Transition when entering the screen
 * @param exitTransition Transition when exiting the screen
 * @param popEnterTransition Transition when returning to the screen via back navigation
 * @param popExitTransition Transition when leaving the screen via back navigation
 */
data class NavigationTransitions(
    val enterTransition: AnimatedContentTransitionScope<*>.() -> EnterTransition,
    val exitTransition: AnimatedContentTransitionScope<*>.() -> ExitTransition,
    val popEnterTransition: AnimatedContentTransitionScope<*>.() -> EnterTransition,
    val popExitTransition: AnimatedContentTransitionScope<*>.() -> ExitTransition
) {
    companion object {
        /**
         * Creates a standard horizontal slide transition set
         * @param durationMillis Animation duration in milliseconds
         */
        fun horizontalSlide(durationMillis: Int = 300) = NavigationTransitions(
            enterTransition = NavigationAnimations.slideInFromRight(durationMillis),
            exitTransition = NavigationAnimations.slideOutToLeft(durationMillis),
            popEnterTransition = NavigationAnimations.slideInFromLeft(durationMillis),
            popExitTransition = NavigationAnimations.slideOutToRight(durationMillis)
        )
    }
}

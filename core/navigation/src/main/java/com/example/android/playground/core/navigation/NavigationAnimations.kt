package com.example.android.playground.core.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry

/**
 * Utility object containing reusable navigation transition animations
 * Updated for Navigation 3 compatibility
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
    fun slideInFromRight(durationMillis: Int = DEFAULT_ANIMATION_DURATION): AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition =
        {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(durationMillis),
            )
        }

    /**
     * Creates a horizontal slide transition for navigating back
     * @param durationMillis Animation duration in milliseconds
     */
    fun slideInFromLeft(durationMillis: Int = DEFAULT_ANIMATION_DURATION): AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition =
        {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(durationMillis),
            )
        }

    /**
     * Creates a horizontal slide transition for exiting when navigating forward
     * @param durationMillis Animation duration in milliseconds
     */
    fun slideOutToLeft(durationMillis: Int = DEFAULT_ANIMATION_DURATION): AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition =
        {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(durationMillis),
            )
        }

    /**
     * Creates a horizontal slide transition for exiting when navigating back
     * @param durationMillis Animation duration in milliseconds
     */
    fun slideOutToRight(durationMillis: Int = DEFAULT_ANIMATION_DURATION): AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition =
        {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(durationMillis),
            )
        }
}

/**
 * Data class representing a complete set of navigation transitions
 * Updated for Navigation 3 type safety
 * @param enterTransition Transition when entering the screen
 * @param exitTransition Transition when exiting the screen
 * @param popEnterTransition Transition when returning to the screen via back navigation
 * @param popExitTransition Transition when leaving the screen via back navigation
 */
data class NavigationTransitions(
    val enterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition,
    val exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition,
    val popEnterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition,
    val popExitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition,
) {
    companion object {
        /**
         * Creates a standard horizontal slide transition set
         * @param durationMillis Animation duration in milliseconds
         */
        fun horizontalSlide(durationMillis: Int = 300) =
            NavigationTransitions(
                enterTransition = NavigationAnimations.slideInFromRight(durationMillis),
                exitTransition = NavigationAnimations.slideOutToLeft(durationMillis),
                popEnterTransition = NavigationAnimations.slideInFromLeft(durationMillis),
                popExitTransition = NavigationAnimations.slideOutToRight(durationMillis),
            )
    }
}

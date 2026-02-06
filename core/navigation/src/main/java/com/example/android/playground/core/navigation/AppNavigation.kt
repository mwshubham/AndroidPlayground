package com.example.android.playground.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavigation(
    navController: NavHostController,
    feedScreen: @Composable () -> Unit,
    imageUploadScreen: @Composable (() -> Unit) -> Unit,
    loginScreen: @Composable () -> Unit,
    noteListScreen: @Composable ((Long?) -> Unit) -> Unit,
    noteDetailScreen: @Composable (() -> Unit) -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = FeedRoute,
    ) {
        composable<FeedRoute> {
            feedScreen()
        }

        composable<ImageUploadRoute>(
            enterTransition = NavigationTransitions.horizontalSlide().enterTransition,
            exitTransition = NavigationTransitions.horizontalSlide().exitTransition,
            popEnterTransition = NavigationTransitions.horizontalSlide().popEnterTransition,
            popExitTransition = NavigationTransitions.horizontalSlide().popExitTransition,
        ) {
            imageUploadScreen {
                // Check if another entry is present in the back stack
                val hasPrev = navController.previousBackStackEntry != null
                navController.popBackStack()
                if (!hasPrev) {
                    navController.navigate(FeedRoute)
                }
            }
        }

        composable<LoginRoute>(
            enterTransition = NavigationTransitions.horizontalSlide().enterTransition,
            exitTransition = NavigationTransitions.horizontalSlide().exitTransition,
            popEnterTransition = NavigationTransitions.horizontalSlide().popEnterTransition,
            popExitTransition = NavigationTransitions.horizontalSlide().popExitTransition,
        ) {
            loginScreen()
        }

        composable<NoteListRoute>(
            enterTransition = NavigationTransitions.horizontalSlide().enterTransition,
            exitTransition = NavigationTransitions.horizontalSlide().exitTransition,
            popEnterTransition = NavigationTransitions.horizontalSlide().popEnterTransition,
            popExitTransition = NavigationTransitions.horizontalSlide().popExitTransition,
        ) {
            noteListScreen { noteId ->
                if (noteId == null) {
                    navController.navigate(NoteDetailRoute(noteId = NavigationConstants.NEW_NOTE_ID))
                } else {
                    navController.navigate(NoteDetailRoute(noteId = noteId.toString()))
                }
            }
        }

        composable<NoteDetailRoute>(
            enterTransition = NavigationTransitions.horizontalSlide().enterTransition,
            exitTransition = NavigationTransitions.horizontalSlide().exitTransition,
            popEnterTransition = NavigationTransitions.horizontalSlide().popEnterTransition,
            popExitTransition = NavigationTransitions.horizontalSlide().popExitTransition,
        ) {
            noteDetailScreen {
                navController.popBackStack()
            }
        }
    }
}

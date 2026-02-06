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
    val transitions = NavigationTransitions.horizontalSlide()
    NavHost(
        navController = navController,
        startDestination = FeedRoute,
        enterTransition = transitions.enterTransition,
        exitTransition = transitions.exitTransition,
        popEnterTransition = transitions.popEnterTransition,
        popExitTransition = transitions.popExitTransition,
    ) {
        composable<FeedRoute> {
            feedScreen()
        }

        composable<ImageUploadRoute> {
            imageUploadScreen {
                // Check if another entry is present in the back stack
                val hasPrev = navController.previousBackStackEntry != null
                navController.popBackStack()
                if (!hasPrev) {
                    navController.navigate(FeedRoute)
                }
            }
        }

        composable<LoginRoute> {
            loginScreen()
        }

        composable<NoteListRoute> {
            noteListScreen { noteId ->
                if (noteId == null) {
                    navController.navigate(NoteDetailRoute(noteId = NavigationConstants.NEW_NOTE_ID))
                } else {
                    navController.navigate(NoteDetailRoute(noteId = noteId.toString()))
                }
            }
        }

        composable<NoteDetailRoute> {
            noteDetailScreen {
                navController.popBackStack()
            }
        }
    }
}

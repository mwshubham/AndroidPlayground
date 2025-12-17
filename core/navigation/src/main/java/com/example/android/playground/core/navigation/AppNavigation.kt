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
    noteDetailScreen: @Composable (() -> Unit) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.FEED
    ) {
        composable(NavigationRoutes.FEED) {
            feedScreen()
        }

        composable(
            route = NavigationRoutes.IMAGE_UPLOAD,
            enterTransition = NavigationTransitions.horizontalSlide().enterTransition,
            exitTransition = NavigationTransitions.horizontalSlide().exitTransition,
            popEnterTransition = NavigationTransitions.horizontalSlide().popEnterTransition,
            popExitTransition = NavigationTransitions.horizontalSlide().popExitTransition
        ) {
            imageUploadScreen {
                // Check if another entry is present in the back stack
                val hasPrev = navController.previousBackStackEntry != null
                navController.popBackStack()
                if (!hasPrev) {
                    navController.navigate(NavigationRoutes.FEED)
                }
            }
        }

        composable(
            route = NavigationRoutes.LOGIN,
            enterTransition = NavigationTransitions.horizontalSlide().enterTransition,
            exitTransition = NavigationTransitions.horizontalSlide().exitTransition,
            popEnterTransition = NavigationTransitions.horizontalSlide().popEnterTransition,
            popExitTransition = NavigationTransitions.horizontalSlide().popExitTransition
        ) {
            loginScreen()
        }

        composable(
            route = NavigationRoutes.NOTE_LIST,
            enterTransition = NavigationTransitions.horizontalSlide().enterTransition,
            exitTransition = NavigationTransitions.horizontalSlide().exitTransition,
            popEnterTransition = NavigationTransitions.horizontalSlide().popEnterTransition,
            popExitTransition = NavigationTransitions.horizontalSlide().popExitTransition
        ) {
            noteListScreen { noteId ->
                if (noteId == null) {
                    navController.navigate("${NavigationRoutes.NOTE_DETAIL}/new")
                } else {
                    navController.navigate("${NavigationRoutes.NOTE_DETAIL}/$noteId")
                }
            }
        }

        composable(
            route = "${NavigationRoutes.NOTE_DETAIL}/{noteId}",
            enterTransition = NavigationTransitions.horizontalSlide().enterTransition,
            exitTransition = NavigationTransitions.horizontalSlide().exitTransition,
            popEnterTransition = NavigationTransitions.horizontalSlide().popEnterTransition,
            popExitTransition = NavigationTransitions.horizontalSlide().popExitTransition
        ) {
            noteDetailScreen {
                navController.popBackStack()
            }
        }
    }
}

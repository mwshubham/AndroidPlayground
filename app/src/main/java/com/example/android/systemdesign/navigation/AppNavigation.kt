package com.example.android.systemdesign.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.android.systemdesign.domain.model.SystemDesignTopicId
import com.example.android.systemdesign.imageupload.presentation.ImageUploadScreen
import com.example.android.systemdesign.imageupload.presentation.LoginScreen
import com.example.android.systemdesign.presentation.MainScreen

object NavigationRoutes {
    const val MAIN = "main"
    const val IMAGE_UPLOAD = "image_upload"
    const val LOGIN = "login"
}

@Composable
fun AppNavigation(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.MAIN
    ) {
        composable(NavigationRoutes.MAIN) {
            MainScreen(
                onTopicClick = { topicId ->
                    when (topicId) {
                        SystemDesignTopicId.ImageUploadApp -> {
                            navController.navigate(NavigationRoutes.IMAGE_UPLOAD)
                        }

                        SystemDesignTopicId.LoginScreen -> {
                            navController.navigate(NavigationRoutes.LOGIN)
                        }
                        // Add other topics navigation here in the future
                        else -> {}
                    }
                }
            )
        }

        composable(NavigationRoutes.IMAGE_UPLOAD) {
            ImageUploadScreen(
                onNavigateBack = {
                    // Check if another entry is present in the back stack
                    val hasPrev = navController.previousBackStackEntry != null
                    navController.popBackStack()
                    if (!hasPrev) {
                        navController.navigate(NavigationRoutes.MAIN)
                    }
                }
            )
        }

        composable(NavigationRoutes.LOGIN) {
            LoginScreen()
        }
    }
}

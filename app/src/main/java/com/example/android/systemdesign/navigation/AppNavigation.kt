package com.example.android.systemdesign.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.android.systemdesign.domain.model.SystemDesignTopicId
import com.example.android.systemdesign.presentation.imageupload.ImageUploadScreen
import com.example.android.systemdesign.presentation.main.MainScreen

object NavigationRoutes {
    const val MAIN = "main"
    const val IMAGE_UPLOAD = "image_upload"
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
                        // Add other topics navigation here in the future
                        else -> {}
                    }
                }
            )
        }

        composable(NavigationRoutes.IMAGE_UPLOAD) {
            ImageUploadScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

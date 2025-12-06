package com.example.android.systemdesign

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.android.systemdesign.core.navigation.AppNavigation
import com.example.android.systemdesign.core.navigation.NavigationRoutes
import com.example.android.systemdesign.core.ui.theme.AndroidSystemDesignTheme
import com.example.android.systemdesign.feed.domain.model.TopicId
import com.example.android.systemdesign.feed.presentation.FeedScreen
import com.example.android.systemdesign.imageupload.presentation.ImageUploadScreen
import com.example.android.systemdesign.login.presentation.LoginScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidSystemDesignTheme {
                val navController = rememberNavController()
                AppNavigation(
                    navController = navController,
                    feedScreen = {
                        FeedScreen(
                            onTopicClick = { topicId ->
                                when (topicId) {
                                    TopicId.ImageUploadApp -> {
                                        navController.navigate(NavigationRoutes.IMAGE_UPLOAD)
                                    }
                                    TopicId.LoginScreen -> {
                                        navController.navigate(NavigationRoutes.LOGIN)
                                    }
                                    // Add other topics navigation here in the future
                                    else -> {}
                                }
                            }
                        )
                    },
                    imageUploadScreen = { onNavigateBack ->
                        ImageUploadScreen(
                            onNavigateBack = onNavigateBack
                        )
                    },
                    loginScreen = {
                        LoginScreen()
                    }
                )
            }
        }
    }
}

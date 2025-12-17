package com.example.android.playground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.android.playground.core.navigation.AppNavigation
import com.example.android.playground.core.navigation.NavigationRoutes
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.feed.domain.model.TopicId
import com.example.android.playground.feed.presentation.FeedScreen
import com.example.android.playground.imageupload.presentation.ImageUploadScreen
import com.example.android.playground.login.presentation.LoginScreen
import com.example.android.playground.note.presentation.screen.NoteDetailScreen
import com.example.android.playground.note.presentation.screen.NoteListScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                val navController = rememberNavController()
                AppNavigation(
                    navController = navController,
                    feedScreen = {
                        FeedScreen(
                            onNavigateBack = {
                                // Since FeedScreen is the start destination, finish the activity
                                finish()
                            },
                            onTopicClick = { topicId ->
                                when (topicId) {
                                    TopicId.ImageUploadApp -> {
                                        navController.navigate(NavigationRoutes.IMAGE_UPLOAD)
                                    }
                                    TopicId.LoginScreen -> {
                                        navController.navigate(NavigationRoutes.LOGIN)
                                    }
                                    TopicId.NoteApp -> {
                                        navController.navigate(NavigationRoutes.NOTE_LIST)
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
                        LoginScreen(
                            onNavigateBack = {
                                navController.popBackStack()
                            }
                        )
                    },
                    noteListScreen = { onNavigateToDetail ->
                        NoteListScreen(
                            onNavigateBack = {
                                navController.popBackStack()
                            },
                            onNavigateToDetail = { noteId -> onNavigateToDetail(noteId) },
                            onNavigateToAdd = { onNavigateToDetail(null) }
                        )
                    },
                    noteDetailScreen = { onNavigateBack ->
                        NoteDetailScreen(
                            onNavigateBack = onNavigateBack
                        )
                    }
                )
            }
        }
    }
}

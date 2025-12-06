package com.example.android.systemdesign

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.android.systemdesign.core.navigation.AppNavigation
import com.example.android.systemdesign.core.navigation.NavigationRoutes
import com.example.android.systemdesign.core.ui.theme.AppTheme
import com.example.android.systemdesign.feed.domain.model.TopicId
import com.example.android.systemdesign.feed.presentation.FeedScreen
import com.example.android.systemdesign.imageupload.presentation.ImageUploadScreen
import com.example.android.systemdesign.login.presentation.LoginScreen
import com.example.android.systemdesign.note.presentation.screen.NoteDetailScreen
import com.example.android.systemdesign.note.presentation.screen.NoteListScreen
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
                        LoginScreen()
                    },
                    noteListScreen = { onNavigateToDetail ->
                        NoteListScreen(
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

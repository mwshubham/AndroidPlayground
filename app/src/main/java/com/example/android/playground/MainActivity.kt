package com.example.android.playground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.rememberNavController
import com.example.android.playground.analytics.AnalyticsHelper
import com.example.android.playground.analytics.LocalAnalyticsHelper
import com.example.android.playground.core.navigation.AppNavigation
import com.example.android.playground.core.navigation.ImageUploadRoute
import com.example.android.playground.core.navigation.LoginRoute
import com.example.android.playground.core.navigation.NoteListRoute
import com.example.android.playground.core.ui.theme.AppTheme
import com.example.android.playground.feed.domain.model.TopicId
import com.example.android.playground.feed.presentation.FeedScreen
import com.example.android.playground.imageupload.presentation.ImageUploadScreen
import com.example.android.playground.login.presentation.LoginScreen
import com.example.android.playground.note.presentation.screen.NoteDetailScreen
import com.example.android.playground.note.presentation.screen.NoteListScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CompositionLocalProvider(
                LocalAnalyticsHelper provides analyticsHelper,
            ) {
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
                                            navController.navigate(ImageUploadRoute)
                                        }
                                        TopicId.LoginScreen -> {
                                            navController.navigate(LoginRoute)
                                        }
                                        TopicId.NoteApp -> {
                                            navController.navigate(NoteListRoute)
                                        }
                                        // Add other topics navigation here in the future
                                        else -> {}
                                    }
                                },
                            )
                        },
                        imageUploadScreen = { onNavigateBack ->
                            ImageUploadScreen(
                                onNavigateBack = onNavigateBack,
                            )
                        },
                        loginScreen = {
                            LoginScreen(
                                onNavigateBack = {
                                    navController.popBackStack()
                                },
                            )
                        },
                        noteListScreen = { onNavigateToDetail ->
                            NoteListScreen(
                                onNavigateBack = {
                                    navController.popBackStack()
                                },
                                onNavigateToDetail = { noteId -> onNavigateToDetail(noteId) },
                                onNavigateToAdd = { onNavigateToDetail(null) },
                            )
                        },
                        noteDetailScreen = { onNavigateBack ->
                            NoteDetailScreen(
                                onNavigateBack = onNavigateBack,
                            )
                        },
                    )
                }
            }
        }
    }
}

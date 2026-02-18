package com.example.android.playground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation3.runtime.rememberNavBackStack
import com.example.android.playground.analytics.AnalyticsHelper
import com.example.android.playground.analytics.LocalAnalyticsHelper
import com.example.android.playground.core.navigation.AppNavigation
import com.example.android.playground.core.navigation.FeedRoute
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
                    val backStack = rememberNavBackStack(FeedRoute)

                    AppNavigation(
                        backStack = backStack,
                        feedScreen = { navigate ->
                            FeedScreen(
                                onNavigateBack = {
                                    // Since FeedScreen is the start destination, finish the activity
                                    finish()
                                },
                                onTopicClick = { topicId ->
                                    when (topicId) {
                                        TopicId.ImageUploadApp -> {
                                            navigate(ImageUploadRoute)
                                        }

                                        TopicId.LoginScreen -> {
                                            navigate(LoginRoute)
                                        }

                                        TopicId.NoteApp -> {
                                            navigate(NoteListRoute)
                                        }
                                        // Add other topics navigation here in the future
                                        else -> {}
                                    }
                                },
                            )
                        },
                        imageUploadScreen = {
                            ImageUploadScreen(
                                onNavigateBack = {
                                    backStack.removeLastOrNull()
                                },
                            )
                        },
                        loginScreen = {
                            LoginScreen(
                                onNavigateBack = {
                                    backStack.removeLastOrNull()
                                },
                            )
                        },
                        noteListScreen = { onNavigateToDetail ->
                            NoteListScreen(
                                onNavigateBack = {
                                    backStack.removeLastOrNull()
                                },
                                onNavigateToDetail = { noteId -> onNavigateToDetail(noteId) },
                                onNavigateToAdd = { onNavigateToDetail(null) },
                            )
                        },
                        noteDetailScreen = { route ->
                            NoteDetailScreen(
                                route = route,
                                onNavigateBack = {
                                    backStack.removeLastOrNull()
                                },
                            )
                        },
                    )
                }
            }
        }
    }
}

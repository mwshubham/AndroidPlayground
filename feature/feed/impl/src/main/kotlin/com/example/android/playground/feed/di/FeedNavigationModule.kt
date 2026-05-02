package com.example.android.playground.feed.di

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.ui.platform.LocalContext
import com.example.android.playground.core.navigation.AppNavigator
import com.example.android.playground.core.navigation.EntryProviderInstaller
import com.example.android.playground.feed.api.FeedRoute
import com.example.android.playground.feed.domain.model.TopicId
import com.example.android.playground.feed.presentation.FeedScreen
import com.example.android.playground.imageupload.api.ImageUploadRoute
import com.example.android.playground.login.api.LoginRoute
import com.example.android.playground.cryptosecurity.api.CryptoSecurityHomeRoute
import com.example.android.playground.mediaorchestrator.api.MediaOrchestratorRoute
import com.example.android.playground.note.api.NoteListRoute
import com.example.android.playground.roomdatabase.api.RoomDatabaseRoute
import com.example.android.playground.userinitiatedservice.api.UserInitiatedServiceRoute
import com.example.android.playground.interappcomm.api.InterAppCommHomeRoute
import com.example.android.playground.graphql.api.GitHubExplorerRoute
import com.example.android.playground.media3player.api.Media3PlayerRoute
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityRetainedComponent::class)
object FeedNavigationModule {
    @IntoSet
    @Provides
    fun provideEntryProviderInstaller(navigator: AppNavigator): EntryProviderInstaller =
        {
            entry<FeedRoute> {
                val context = LocalContext.current
                BackHandler {
                    (context as? Activity)?.finish()
                }
                FeedScreen(
                    onNavigateBack = {
                        (context as? Activity)?.finish()
                    },
                    onTopicClick = { topicId ->
                        when (topicId) {
                            TopicId.ImageUploadApp -> {
                                navigator.goTo(ImageUploadRoute)
                            }

                            TopicId.LoginScreen -> {
                                navigator.goTo(LoginRoute)
                            }

                            TopicId.NoteApp -> {
                                navigator.goTo(NoteListRoute)
                            }

                            TopicId.MediaOrchestratorApp -> {
                                navigator.goTo(MediaOrchestratorRoute)
                            }

                            TopicId.UserInitiatedServiceApp -> {
                                navigator.goTo(UserInitiatedServiceRoute)
                            }

                            TopicId.AndroidSecurity -> {
                                navigator.goTo(CryptoSecurityHomeRoute)
                            }

                            TopicId.RoomDatabaseApp -> {
                                navigator.goTo(RoomDatabaseRoute)
                            }

                            TopicId.InterAppCommunication -> {
                                navigator.goTo(InterAppCommHomeRoute)
                            }

                            TopicId.GraphQL -> {
                                navigator.goTo(GitHubExplorerRoute)
                            }

                            TopicId.Media3Player -> {
                                navigator.goTo(Media3PlayerRoute)
                            }
                        }
                    },
                )
            }
        }
}

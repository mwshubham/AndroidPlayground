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
import com.example.android.playground.note.api.NoteListRoute
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
                            // Add other topics navigation here in the future
                            else -> {}
                        }
                    },
                )
            }
        }
}

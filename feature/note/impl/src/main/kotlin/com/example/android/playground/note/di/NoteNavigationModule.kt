package com.example.android.playground.note.di

import com.example.android.playground.core.navigation.EntryProviderInstaller
import com.example.android.playground.core.navigation.AppNavigator
import com.example.android.playground.note.api.NoteDetailRoute
import com.example.android.playground.note.api.NoteListRoute
import com.example.android.playground.note.presentation.screen.NoteDetailScreen
import com.example.android.playground.note.presentation.screen.NoteListScreen
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityRetainedComponent::class)
object NoteNavigationModule {
    @IntoSet
    @Provides
    fun provideEntryProviderInstaller(navigator: AppNavigator): EntryProviderInstaller =
        {
            entry<NoteListRoute> {
                NoteListScreen(
                    onNavigateBack = {
                        navigator.goBack()
                    },
                    onNavigateToDetail = { noteId ->
                        navigator.goTo(NoteDetailRoute(noteId = noteId))
                    },
                    onNavigateToAdd = {
                        navigator.goTo(NoteDetailRoute(noteId = null))
                    },
                )
            }

            entry<NoteDetailRoute> { route ->
                NoteDetailScreen(
                    route = route,
                    onNavigateBack = {
                        navigator.goBack()
                    },
                )
            }
        }
}

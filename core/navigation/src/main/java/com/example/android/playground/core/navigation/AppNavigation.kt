package com.example.android.playground.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay

@Composable
fun AppNavigation(
    backStack: NavBackStack<NavKey>,
    feedScreen: @Composable (((NavKey) -> Unit) -> Unit),
    imageUploadScreen: @Composable (() -> Unit),
    loginScreen: @Composable (() -> Unit),
    noteListScreen: @Composable (((Long?) -> Unit) -> Unit),
    noteDetailScreen: @Composable (() -> Unit),
) {
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        transitionSpec = Navigation3Transitions.horizontalSlideTransition(),
        popTransitionSpec = Navigation3Transitions.horizontalSlidePopTransition(),
        predictivePopTransitionSpec = Navigation3Transitions.predictivePopTransition(),
        entryProvider = entryProvider {
            entry<FeedRoute> {
                feedScreen { route ->
                    backStack.add(route)
                }
            }

            entry<ImageUploadRoute> {
                imageUploadScreen()
            }

            entry<LoginRoute> {
                loginScreen()
            }

            entry<NoteListRoute> {
                noteListScreen { noteId ->
                    if (noteId == null) {
                        backStack.add(NoteDetailRoute(noteId = NavigationConstants.NEW_NOTE_ID))
                    } else {
                        backStack.add(NoteDetailRoute(noteId = noteId.toString()))
                    }
                }
            }

            entry<NoteDetailRoute> {
                noteDetailScreen()
            }
        }
    )
}

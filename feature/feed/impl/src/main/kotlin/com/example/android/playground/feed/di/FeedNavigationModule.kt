package com.example.android.playground.feed.di

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.ui.platform.LocalContext
import com.example.android.playground.annotationprocessing.api.AnnotationProcessingRoute
import com.example.android.playground.core.navigation.AppNavigator
import com.example.android.playground.core.navigation.EntryProviderInstaller
import com.example.android.playground.cryptosecurity.api.CryptoSecurityHomeRoute
import com.example.android.playground.deviceclassifier.api.DeviceClassifierRoute
import com.example.android.playground.feed.api.FeedRoute
import com.example.android.playground.feed.domain.model.TopicId
import com.example.android.playground.feed.presentation.FeedScreen
import com.example.android.playground.flowlivedata.api.FlowLiveDataRoute
import com.example.android.playground.graphql.api.GraphQLRoute
import com.example.android.playground.grpc.api.GrpcRoute
import com.example.android.playground.imageupload.api.ImageUploadRoute
import com.example.android.playground.interappcomm.api.InterAppCommHomeRoute
import com.example.android.playground.login.api.LoginRoute
import com.example.android.playground.media3player.api.Media3PlayerRoute
import com.example.android.playground.mediaorchestrator.api.MediaOrchestratorRoute
import com.example.android.playground.note.api.NoteListRoute
import com.example.android.playground.roomdatabase.api.RoomDatabaseRoute
import com.example.android.playground.sse.api.SseRoute
import com.example.android.playground.tictactoe.api.TicTacToeSetupRoute
import com.example.android.playground.userinitiatedservice.api.UserInitiatedServiceRoute
import com.example.android.playground.websocket.api.WebSocketRoute
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityRetainedComponent::class)
object FeedNavigationModule {
    private val topicRoutes: Map<TopicId, androidx.navigation3.runtime.NavKey> =
        mapOf(
            TopicId.ImageUploadApp to ImageUploadRoute,
            TopicId.LoginScreen to LoginRoute,
            TopicId.NoteApp to NoteListRoute,
            TopicId.MediaOrchestratorApp to MediaOrchestratorRoute,
            TopicId.UserInitiatedServiceApp to UserInitiatedServiceRoute,
            TopicId.AndroidSecurity to CryptoSecurityHomeRoute,
            TopicId.RoomDatabaseApp to RoomDatabaseRoute,
            TopicId.InterAppCommunication to InterAppCommHomeRoute,
            TopicId.GraphQL to GraphQLRoute,
            TopicId.Media3Player to Media3PlayerRoute,
            TopicId.WebSocket to WebSocketRoute,
            TopicId.Sse to SseRoute,
            TopicId.Grpc to GrpcRoute,
            TopicId.TicTacToe to TicTacToeSetupRoute,
            TopicId.FlowVsLiveData to FlowLiveDataRoute,
            TopicId.AnnotationProcessing to AnnotationProcessingRoute,
            TopicId.DeviceClassifier to DeviceClassifierRoute,
        )

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
                        topicRoutes[topicId]?.let { navigator.goTo(it) }
                    },
                )
            }
        }
}

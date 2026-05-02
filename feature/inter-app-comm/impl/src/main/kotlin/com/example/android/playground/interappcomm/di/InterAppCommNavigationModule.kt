package com.example.android.playground.interappcomm.di

import com.example.android.playground.core.navigation.AppNavigator
import com.example.android.playground.core.navigation.EntryProviderInstaller
import com.example.android.playground.interappcomm.api.AidlRoute
import com.example.android.playground.interappcomm.api.BroadcastRoute
import com.example.android.playground.interappcomm.api.ContentProviderRoute
import com.example.android.playground.interappcomm.api.ExplicitIntentRoute
import com.example.android.playground.interappcomm.api.InterAppCommHomeRoute
import com.example.android.playground.interappcomm.api.MessengerRoute
import com.example.android.playground.interappcomm.domain.model.IpcMethod
import com.example.android.playground.interappcomm.presentation.screen.AidlScreen
import com.example.android.playground.interappcomm.presentation.screen.BroadcastScreen
import com.example.android.playground.interappcomm.presentation.screen.ContentProviderScreen
import com.example.android.playground.interappcomm.presentation.screen.ExplicitIntentScreen
import com.example.android.playground.interappcomm.presentation.screen.InterAppCommHomeScreen
import com.example.android.playground.interappcomm.presentation.screen.MessengerScreen
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityRetainedComponent::class)
object InterAppCommNavigationModule {
    @IntoSet
    @Provides
    fun provideEntryProviderInstaller(navigator: AppNavigator): EntryProviderInstaller =
        {
            entry<InterAppCommHomeRoute> {
                InterAppCommHomeScreen(
                    onNavigateBack = { navigator.goBack() },
                    onNavigateToChannel = { method ->
                        when (method) {
                            IpcMethod.EXPLICIT_INTENT -> navigator.goTo(ExplicitIntentRoute)
                            IpcMethod.BROADCAST -> navigator.goTo(BroadcastRoute)
                            IpcMethod.CONTENT_PROVIDER -> navigator.goTo(ContentProviderRoute)
                            IpcMethod.MESSENGER -> navigator.goTo(MessengerRoute)
                            IpcMethod.AIDL -> navigator.goTo(AidlRoute)
                        }
                    },
                )
            }

            entry<ExplicitIntentRoute> {
                ExplicitIntentScreen(
                    onNavigateBack = { navigator.goBack() },
                )
            }

            entry<BroadcastRoute> {
                BroadcastScreen(
                    onNavigateBack = { navigator.goBack() },
                )
            }

            entry<ContentProviderRoute> {
                ContentProviderScreen(
                    onNavigateBack = { navigator.goBack() },
                )
            }

            entry<MessengerRoute> {
                MessengerScreen(
                    onNavigateBack = { navigator.goBack() },
                )
            }

            entry<AidlRoute> {
                AidlScreen(
                    onNavigateBack = { navigator.goBack() },
                )
            }
        }
}

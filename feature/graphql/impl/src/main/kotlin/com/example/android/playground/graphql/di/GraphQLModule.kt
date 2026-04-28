package com.example.android.playground.graphql.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.apollographql.apollo.ApolloClient
import com.example.android.playground.graphql.data.network.GitHubGraphQLService
import com.example.android.playground.graphql.data.repository.GitHubRepositoryImpl
import com.example.android.playground.graphql.domain.repository.GitHubRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

private val Context.githubDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "github_prefs",
)

@Module
@InstallIn(SingletonComponent::class)
abstract class GraphQLModule {

    @Binds
    @Singleton
    internal abstract fun bindGitHubRepository(impl: GitHubRepositoryImpl): GitHubRepository

    companion object {

        @Provides
        @Singleton
        fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                },
            )
            .build()

        @Provides
        @Singleton
        fun provideJson(): Json = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }

        @Provides
        @Singleton
        internal fun provideGitHubGraphQLService(
            okHttpClient: OkHttpClient,
            json: Json,
        ): GitHubGraphQLService = GitHubGraphQLService(okHttpClient, json)

        @Provides
        @Singleton
        fun provideGitHubDataStore(
            @ApplicationContext context: Context,
        ): DataStore<Preferences> = context.githubDataStore

        /**
         * Provides the Apollo Kotlin client.
         *
         * The base client has no auth header — the token is injected per-request
         * in [ApolloSearchReposUseCase] via `.addHttpHeader("Authorization", "Bearer $token")`.
         * This keeps the client stateless and reusable across different user sessions.
         *
         * Apollo automatically uses the OkHttp engine under the hood on Android/JVM.
         */
        @Provides
        @Singleton
        fun provideApolloClient(): ApolloClient = ApolloClient.Builder()
            .serverUrl("https://api.github.com/graphql")
            .build()
    }
}

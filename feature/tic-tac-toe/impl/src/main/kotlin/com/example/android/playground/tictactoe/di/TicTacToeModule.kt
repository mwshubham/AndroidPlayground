package com.example.android.playground.tictactoe.di

import android.content.Context
import androidx.room.Room
import com.example.android.playground.tictactoe.data.local.GameResultDao
import com.example.android.playground.tictactoe.data.local.TicTacToeDatabase
import com.example.android.playground.tictactoe.data.repository.TicTacToeRepositoryImpl
import com.example.android.playground.tictactoe.domain.repository.TicTacToeRepository
import com.example.android.playground.tictactoe.domain.strategy.AiStrategy
import com.example.android.playground.tictactoe.domain.strategy.MinimaxAiStrategy
import com.example.android.playground.tictactoe.util.TicTacToeConstants
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TicTacToeModule {
    @Binds
    abstract fun bindTicTacToeRepository(impl: TicTacToeRepositoryImpl): TicTacToeRepository

    @Binds
    abstract fun bindAiStrategy(impl: MinimaxAiStrategy): AiStrategy

    companion object {
        @Provides
        @Singleton
        fun provideTicTacToeDatabase(
            @ApplicationContext context: Context,
        ): TicTacToeDatabase =
            Room
                .databaseBuilder(context, TicTacToeDatabase::class.java, TicTacToeConstants.DATABASE_NAME)
                .build()

        @Provides
        fun provideGameResultDao(database: TicTacToeDatabase): GameResultDao = database.gameResultDao()
    }
}

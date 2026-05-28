package com.example.android.playground.tictactoe.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameResultDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: GameResultEntity)

    @Query("SELECT * FROM game_results ORDER BY playedAt DESC")
    fun observeAll(): Flow<List<GameResultEntity>>
}

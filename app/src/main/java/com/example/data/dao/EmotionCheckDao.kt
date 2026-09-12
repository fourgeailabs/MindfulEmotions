package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.EmotionCheckLog
import kotlinx.coroutines.flow.Flow

@Dao
interface EmotionCheckDao {
    @Query("SELECT * FROM emotion_logs ORDER BY timestamp DESC")
    fun getAllLogs(): Flow<List<EmotionCheckLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: EmotionCheckLog): Long

    @Query("DELETE FROM emotion_logs")
    suspend fun clearAllLogs()
}

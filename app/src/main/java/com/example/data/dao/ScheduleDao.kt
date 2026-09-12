package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.EmotionSchedule
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {
    @Query("SELECT * FROM schedules ORDER BY hour ASC, minute ASC")
    fun getAllSchedules(): Flow<List<EmotionSchedule>>

    @Query("SELECT * FROM schedules WHERE isEnabled = 1")
    suspend fun getActiveSchedules(): List<EmotionSchedule>

    @Query("SELECT * FROM schedules")
    suspend fun getAllSchedulesList(): List<EmotionSchedule>

    @Query("SELECT * FROM schedules WHERE id = :id")
    suspend fun getScheduleById(id: Long): EmotionSchedule?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedule(schedule: EmotionSchedule): Long

    @Update
    suspend fun updateSchedule(schedule: EmotionSchedule)

    @Delete
    suspend fun deleteSchedule(schedule: EmotionSchedule)

    @Query("SELECT COUNT(*) FROM schedules")
    suspend fun getCount(): Int
}

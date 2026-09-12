package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "emotion_logs")
data class EmotionCheckLog(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val selectedEmotion: String,
    val promptQuestion: String,
    val quoteText: String,
    val quoteAuthor: String
) {
    fun formattedDate(): String {
        val sdf = SimpleDateFormat("MMM dd, yyyy · hh:mm a", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}

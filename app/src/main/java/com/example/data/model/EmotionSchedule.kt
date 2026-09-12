package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Entity(tableName = "schedules")
data class EmotionSchedule(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val hour: Int,
    val minute: Int,
    val label: String = "Emotion Check",
    // Repeating days of the week (no preset days)
    val monday: Boolean = false,
    val tuesday: Boolean = false,
    val wednesday: Boolean = false,
    val thursday: Boolean = false,
    val friday: Boolean = false,
    val saturday: Boolean = false,
    val sunday: Boolean = false,
    // Specific date schedule: year, month (0-indexed), dayOfMonth (no preset dates)
    val specificYear: Int? = null,
    val specificMonth: Int? = null,
    val specificDay: Int? = null,
    val isEnabled: Boolean = true
) {
    fun isDateSpecific(): Boolean {
        return specificYear != null && specificMonth != null && specificDay != null
    }

    fun isDayActive(dayOfWeekCalendarIndex: Int): Boolean {
        if (isDateSpecific()) return false
        // java.util.Calendar: SUNDAY=1, MONDAY=2, TUESDAY=3, WEDNESDAY=4, THURSDAY=5, FRIDAY=6, SATURDAY=7
        return when (dayOfWeekCalendarIndex) {
            Calendar.MONDAY -> monday
            Calendar.TUESDAY -> tuesday
            Calendar.WEDNESDAY -> wednesday
            Calendar.THURSDAY -> thursday
            Calendar.FRIDAY -> friday
            Calendar.SATURDAY -> saturday
            Calendar.SUNDAY -> sunday
            else -> false
        }
    }

    fun formattedTime(): String {
        val h12 = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
        val amPm = if (hour >= 12) "PM" else "AM"
        val minStr = if (minute < 10) "0$minute" else "$minute"
        return "$h12:$minStr $amPm"
    }

    fun formattedSpecificDate(): String? {
        if (!isDateSpecific()) return null
        val cal = Calendar.getInstance().apply {
            set(Calendar.YEAR, specificYear!!)
            set(Calendar.MONTH, specificMonth!!)
            set(Calendar.DAY_OF_MONTH, specificDay!!)
        }
        val format = SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault())
        return format.format(cal.time)
    }

    fun activeDaysSummary(): String {
        if (isDateSpecific()) {
            val cal = Calendar.getInstance().apply {
                set(Calendar.YEAR, specificYear!!)
                set(Calendar.MONTH, specificMonth!!)
                set(Calendar.DAY_OF_MONTH, specificDay!!)
            }
            val today = Calendar.getInstance()
            val isToday = today.get(Calendar.YEAR) == specificYear &&
                    today.get(Calendar.MONTH) == specificMonth &&
                    today.get(Calendar.DAY_OF_MONTH) == specificDay
            val tomorrow = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) }
            val isTomorrow = tomorrow.get(Calendar.YEAR) == specificYear &&
                    tomorrow.get(Calendar.MONTH) == specificMonth &&
                    tomorrow.get(Calendar.DAY_OF_MONTH) == specificDay

            val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
            return when {
                isToday -> "Scheduled for Today (${SimpleDateFormat("MMM d", Locale.getDefault()).format(cal.time)})"
                isTomorrow -> "Scheduled for Tomorrow (${SimpleDateFormat("MMM d", Locale.getDefault()).format(cal.time)})"
                else -> "Scheduled for ${dateFormat.format(cal.time)}"
            }
        }

        val days = mutableListOf<String>()
        if (monday) days.add("Mon")
        if (tuesday) days.add("Tue")
        if (wednesday) days.add("Wed")
        if (thursday) days.add("Thu")
        if (friday) days.add("Fri")
        if (saturday) days.add("Sat")
        if (sunday) days.add("Sun")

        return when {
            days.size == 7 -> "Every day"
            days.size == 5 && !saturday && !sunday -> "Weekdays"
            days.size == 2 && saturday && sunday -> "Weekends"
            days.isEmpty() -> "No days or dates chosen"
            else -> days.joinToString(", ")
        }
    }
}

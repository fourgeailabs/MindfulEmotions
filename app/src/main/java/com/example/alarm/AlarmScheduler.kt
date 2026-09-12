package com.example.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.data.PreferencesManager
import com.example.data.model.EmotionSchedule
import com.example.receiver.AlarmReceiver
import java.util.Calendar

object AlarmScheduler {
    private const val TAG = "AlarmScheduler"
    private const val REQUEST_CODE_BASE = 1000

    fun scheduleNextAlarm(context: Context, schedules: List<EmotionSchedule>) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val prefs = PreferencesManager(context).loadSettings()

        val activeSchedules = schedules.filter { it.isEnabled }
        if (activeSchedules.isEmpty()) {
            cancelAllAlarms(context, schedules)
            return
        }

        val now = Calendar.getInstance()
        var earliestTime: Long = Long.MAX_VALUE
        var selectedSchedule: EmotionSchedule? = null

        for (schedule in activeSchedules) {
            val nextOccurrence = getNextOccurrence(schedule, now)
            if (nextOccurrence != null && nextOccurrence.timeInMillis < earliestTime) {
                earliestTime = nextOccurrence.timeInMillis
                selectedSchedule = schedule
            }
        }

        if (selectedSchedule != null && earliestTime != Long.MAX_VALUE) {
            scheduleAlarmAt(context, alarmManager, earliestTime, selectedSchedule.id, prefs.useAndroidAlarm)
            Log.d(TAG, "Scheduled next alarm for: ${selectedSchedule.formattedTime()} at epoch $earliestTime")
        }
    }

    private fun getNextOccurrence(schedule: EmotionSchedule, now: Calendar): Calendar? {
        if (schedule.isDateSpecific()) {
            val candidate = Calendar.getInstance().apply {
                set(Calendar.YEAR, schedule.specificYear!!)
                set(Calendar.MONTH, schedule.specificMonth!!)
                set(Calendar.DAY_OF_MONTH, schedule.specificDay!!)
                set(Calendar.HOUR_OF_DAY, schedule.hour)
                set(Calendar.MINUTE, schedule.minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            return if (candidate.timeInMillis > now.timeInMillis) candidate else null
        }

        // Repeating days check
        val hasAnyDayActive = schedule.monday || schedule.tuesday || schedule.wednesday ||
                schedule.thursday || schedule.friday || schedule.saturday || schedule.sunday
        if (!hasAnyDayActive) {
            return null
        }

        val candidate = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, schedule.hour)
            set(Calendar.MINUTE, schedule.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // If today's time already passed, advance to tomorrow
        if (candidate.timeInMillis <= now.timeInMillis) {
            candidate.add(Calendar.DAY_OF_YEAR, 1)
        }

        // Look up to 7 days ahead for an active day
        for (i in 0..7) {
            val dayOfWeek = candidate.get(Calendar.DAY_OF_WEEK)
            if (schedule.isDayActive(dayOfWeek)) {
                return candidate
            }
            candidate.add(Calendar.DAY_OF_YEAR, 1)
        }

        return null
    }

    private fun scheduleAlarmAt(
        context: Context,
        alarmManager: AlarmManager,
        triggerAtMillis: Long,
        scheduleId: Long,
        useAndroidAlarmClock: Boolean
    ) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(AlarmReceiver.EXTRA_SCHEDULE_ID, scheduleId)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_BASE + (scheduleId % 1000).toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (useAndroidAlarmClock) {
                // Integrates with Android's system built-in alarm clock indicator & stream
                val showIntent = Intent(context, com.example.MainActivity::class.java)
                val showPendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    showIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                val alarmClockInfo = AlarmManager.AlarmClockInfo(triggerAtMillis, showPendingIntent)
                alarmManager.setAlarmClock(alarmClockInfo, pendingIntent)
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerAtMillis,
                        pendingIntent
                    )
                } else {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
                }
            }
        } catch (e: SecurityException) {
            Log.e(TAG, "Exact alarm permission issue: ${e.message}")
        }
    }

    fun triggerTestCheckIn(context: Context, delaySeconds: Int = 1) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val prefs = PreferencesManager(context).loadSettings()
        val triggerTime = System.currentTimeMillis() + (delaySeconds * 1000)

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(AlarmReceiver.EXTRA_SCHEDULE_ID, -1L)
            putExtra(AlarmReceiver.EXTRA_IS_TEST, true)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            9999,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (prefs.useAndroidAlarm) {
                val showIntent = Intent(context, com.example.MainActivity::class.java)
                val showPending = PendingIntent.getActivity(
                    context,
                    0,
                    showIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                alarmManager.setAlarmClock(AlarmManager.AlarmClockInfo(triggerTime, showPending), pendingIntent)
            } else {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
            }
        } catch (e: Exception) {
            // Fallback: send broadcast directly
            context.sendBroadcast(intent)
        }
    }

    fun cancelAllAlarms(context: Context, schedules: List<EmotionSchedule>) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        for (schedule in schedules) {
            val intent = Intent(context, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                REQUEST_CODE_BASE + (schedule.id % 1000).toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.cancel(pendingIntent)
        }
    }
}

package com.example.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.core.app.NotificationCompat
import com.example.EmotionCheckActivity
import com.example.R
import com.example.alarm.AlarmScheduler
import com.example.data.MindfulDatabase
import com.example.data.PreferencesManager
import com.example.data.QuotesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val prefs = PreferencesManager(context).loadSettings()
        val quote = QuotesRepository.getRandomQuote()
        val prompt = prefs.promptWording

        // Play gentle vibration if enabled
        if (prefs.vibrateOnAlert) {
            triggerMindfulVibration(context)
        }

        // Prepare full-screen intent
        val fullScreenIntent = Intent(context, EmotionCheckActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(EmotionCheckActivity.EXTRA_QUOTE_ID, quote.id)
            putExtra(EmotionCheckActivity.EXTRA_QUOTE_TEXT, quote.text)
            putExtra(EmotionCheckActivity.EXTRA_QUOTE_AUTHOR, quote.author)
            putExtra(EmotionCheckActivity.EXTRA_PROMPT_WORDING, prompt)
        }

        val fullScreenPendingIntent = PendingIntent.getActivity(
            context,
            2001,
            fullScreenIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val dismissIntent = Intent(context, DismissReceiver::class.java)
        val dismissPendingIntent = PendingIntent.getBroadcast(
            context,
            2002,
            dismissIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = if (prefs.useAndroidAlarm) "mindful_alarm_channel" else "mindful_gentle_channel"
        val channelName = if (prefs.useAndroidAlarm) "Mindful Emotion Alarm" else "Mindful Emotion Check"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = "Full screen popups and notifications to check emotions mindfully."
                enableLights(true)
                lightColor = 0xFFC87D65.toInt() // pastel peach
                if (prefs.playAlarmSound) {
                    val soundUri = if (prefs.useAndroidAlarm) {
                        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                            ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                    } else {
                        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                    }
                    val audioAttributes = AudioAttributes.Builder()
                        .setUsage(if (prefs.useAndroidAlarm) AudioAttributes.USAGE_ALARM else AudioAttributes.USAGE_NOTIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                    setSound(soundUri, audioAttributes)
                } else {
                    setSound(null, null)
                }
                if (prefs.vibrateOnAlert) {
                    enableVibration(true)
                    vibrationPattern = longArrayOf(0, 400, 200, 400)
                }
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(prompt)
            .setContentText("\"${quote.text}\" — ${quote.author}")
            .setStyle(NotificationCompat.BigTextStyle().bigText("\"${quote.text}\"\n\n— ${quote.author}"))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(if (prefs.useAndroidAlarm) NotificationCompat.CATEGORY_ALARM else NotificationCompat.CATEGORY_REMINDER)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .setContentIntent(fullScreenPendingIntent)
            .setDeleteIntent(dismissPendingIntent)
            .setAutoCancel(true)
            .addAction(
                android.R.drawable.ic_menu_close_clear_cancel,
                "Dismiss",
                dismissPendingIntent
            )

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())

        // Launch full-screen activity directly
        try {
            context.startActivity(fullScreenIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Reschedule next active schedule (and mark one-time date specific schedule as disabled)
        val scheduleId = intent.getLongExtra(EXTRA_SCHEDULE_ID, -1L)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val db = MindfulDatabase.getDatabase(context)
                if (scheduleId > 0) {
                    val sched = db.scheduleDao().getScheduleById(scheduleId)
                    if (sched != null && sched.isDateSpecific()) {
                        db.scheduleDao().updateSchedule(sched.copy(isEnabled = false))
                    }
                }
                val activeSchedules = db.scheduleDao().getActiveSchedules()
                AlarmScheduler.scheduleNextAlarm(context, activeSchedules)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun triggerMindfulVibration(context: Context) {
        try {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vm?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }

            if (vibrator != null && vibrator.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val effect = VibrationEffect.createWaveform(
                        longArrayOf(0, 300, 200, 300),
                        -1
                    )
                    vibrator.vibrate(effect)
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(longArrayOf(0, 300, 200, 300), -1)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        const val NOTIFICATION_ID = 8888
        const val EXTRA_SCHEDULE_ID = "extra_schedule_id"
        const val EXTRA_IS_TEST = "extra_is_test"
    }
}

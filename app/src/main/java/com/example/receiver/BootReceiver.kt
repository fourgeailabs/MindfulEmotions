package com.example.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.alarm.AlarmScheduler
import com.example.data.MindfulDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val action = intent?.action
        if (action == Intent.ACTION_BOOT_COMPLETED || action == Intent.ACTION_MY_PACKAGE_REPLACED) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val db = MindfulDatabase.getDatabase(context)
                    val activeSchedules = db.scheduleDao().getActiveSchedules()
                    AlarmScheduler.scheduleNextAlarm(context, activeSchedules)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}

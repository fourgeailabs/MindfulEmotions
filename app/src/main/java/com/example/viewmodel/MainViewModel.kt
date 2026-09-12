package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.alarm.AlarmScheduler
import com.example.data.AppSettings
import com.example.data.MindfulDatabase
import com.example.data.PreferencesManager
import com.example.data.model.EmotionCheckLog
import com.example.data.model.EmotionSchedule
import com.example.data.model.JournalEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val db = MindfulDatabase.getDatabase(application)
    private val scheduleDao = db.scheduleDao()
    private val emotionDao = db.emotionCheckDao()
    private val journalDao = db.journalDao()
    private val prefsManager = PreferencesManager(application)

    val schedules: StateFlow<List<EmotionSchedule>> = scheduleDao.getAllSchedules()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val logs: StateFlow<List<EmotionCheckLog>> = emotionDao.getAllLogs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val journalEntries: StateFlow<List<JournalEntry>> = journalDao.getAllEntries()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val settings: StateFlow<AppSettings> = prefsManager.settings

    private val _isShowingFullScreenPopup = MutableStateFlow(false)
    val isShowingFullScreenPopup: StateFlow<Boolean> = _isShowingFullScreenPopup.asStateFlow()

    init {
        viewModelScope.launch {
            // Remove any legacy preset schedules if they exist so the app has no presets
            val legacyPresets = setOf(
                "Morning Mindful Check-in",
                "Midday Peace & Balance",
                "Afternoon Reset",
                "Evening Calm & Reflection"
            )
            val allSchedules = scheduleDao.getAllSchedulesList()
            for (sched in allSchedules) {
                if (sched.label in legacyPresets && sched.specificYear == null) {
                    scheduleDao.deleteSchedule(sched)
                }
            }
            val active = scheduleDao.getActiveSchedules()
            AlarmScheduler.scheduleNextAlarm(getApplication(), active)

            if (journalDao.getCount() == 0) {
                journalDao.insertEntry(
                    JournalEntry(
                        emotion = "Peaceful",
                        emotionEmoji = "🌿",
                        note = "Took three deep unhurried breaths before starting the morning. Allowed stillness to anchor the day.",
                        promptQuestion = "What is one small thing that brought you joy today?",
                        quoteSnippet = "Peace comes from within. Do not seek it without."
                    )
                )
            }
        }
    }

    fun addSchedule(schedule: EmotionSchedule) {
        viewModelScope.launch {
            scheduleDao.insertSchedule(schedule)
            val active = scheduleDao.getActiveSchedules()
            AlarmScheduler.scheduleNextAlarm(getApplication(), active)
        }
    }

    fun updateSchedule(schedule: EmotionSchedule) {
        viewModelScope.launch {
            scheduleDao.updateSchedule(schedule)
            val active = scheduleDao.getActiveSchedules()
            AlarmScheduler.scheduleNextAlarm(getApplication(), active)
        }
    }

    fun deleteSchedule(schedule: EmotionSchedule) {
        viewModelScope.launch {
            scheduleDao.deleteSchedule(schedule)
            val active = scheduleDao.getActiveSchedules()
            AlarmScheduler.scheduleNextAlarm(getApplication(), active)
        }
    }

    fun addJournalEntry(entry: JournalEntry) {
        viewModelScope.launch {
            journalDao.insertEntry(entry)
        }
    }

    fun deleteJournalEntry(entry: JournalEntry) {
        viewModelScope.launch {
            journalDao.deleteEntry(entry)
        }
    }

    fun updatePromptWording(wording: String) {
        prefsManager.updatePromptWording(wording)
    }

    fun updateUseAndroidAlarm(enabled: Boolean) {
        prefsManager.updateUseAndroidAlarm(enabled)
        viewModelScope.launch {
            val active = scheduleDao.getActiveSchedules()
            AlarmScheduler.scheduleNextAlarm(getApplication(), active)
        }
    }

    fun updatePlaySound(enabled: Boolean) {
        prefsManager.updatePlayAlarmSound(enabled)
    }

    fun updateVibrate(enabled: Boolean) {
        prefsManager.updateVibrateOnAlert(enabled)
    }

    fun updateSubtleHaptics(enabled: Boolean) {
        prefsManager.updateSubtleHaptics(enabled)
    }

    fun showFullScreenPopup() {
        _isShowingFullScreenPopup.value = true
    }

    fun dismissFullScreenPopup() {
        _isShowingFullScreenPopup.value = false
    }

    fun triggerSystemTestAlert() {
        AlarmScheduler.triggerTestCheckIn(getApplication(), delaySeconds = 1)
    }
}

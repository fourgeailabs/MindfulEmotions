package com.example.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AppSettings(
    val promptWording: String = "How are you feeling right now?",
    val useAndroidAlarm: Boolean = true,
    val playAlarmSound: Boolean = true,
    val vibrateOnAlert: Boolean = true,
    val subtleHapticsEnabled: Boolean = true,
    val checkInIntervalHours: Int = 0 // 0 means custom scheduled times
)

class PreferencesManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("mindful_emotions_prefs", Context.MODE_PRIVATE)

    private val _settings = MutableStateFlow(loadSettings())
    val settings: StateFlow<AppSettings> = _settings.asStateFlow()

    fun loadSettings(): AppSettings {
        return AppSettings(
            promptWording = prefs.getString(KEY_PROMPT_WORDING, "How are you feeling right now?")
                ?: "How are you feeling right now?",
            useAndroidAlarm = prefs.getBoolean(KEY_USE_ANDROID_ALARM, true),
            playAlarmSound = prefs.getBoolean(KEY_PLAY_ALARM_SOUND, true),
            vibrateOnAlert = prefs.getBoolean(KEY_VIBRATE_ON_ALERT, true),
            subtleHapticsEnabled = prefs.getBoolean(KEY_SUBTLE_HAPTICS, true),
            checkInIntervalHours = prefs.getInt(KEY_CHECK_IN_INTERVAL, 0)
        )
    }

    fun updatePromptWording(wording: String) {
        prefs.edit().putString(KEY_PROMPT_WORDING, wording).apply()
        _settings.value = _settings.value.copy(promptWording = wording)
    }

    fun updateUseAndroidAlarm(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_USE_ANDROID_ALARM, enabled).apply()
        _settings.value = _settings.value.copy(useAndroidAlarm = enabled)
    }

    fun updatePlayAlarmSound(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_PLAY_ALARM_SOUND, enabled).apply()
        _settings.value = _settings.value.copy(playAlarmSound = enabled)
    }

    fun updateVibrateOnAlert(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_VIBRATE_ON_ALERT, enabled).apply()
        _settings.value = _settings.value.copy(vibrateOnAlert = enabled)
    }

    fun updateSubtleHaptics(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_SUBTLE_HAPTICS, enabled).apply()
        _settings.value = _settings.value.copy(subtleHapticsEnabled = enabled)
    }

    fun updateCheckInInterval(hours: Int) {
        prefs.edit().putInt(KEY_CHECK_IN_INTERVAL, hours).apply()
        _settings.value = _settings.value.copy(checkInIntervalHours = hours)
    }

    companion object {
        private const val KEY_PROMPT_WORDING = "prompt_wording"
        private const val KEY_USE_ANDROID_ALARM = "use_android_alarm"
        private const val KEY_PLAY_ALARM_SOUND = "play_alarm_sound"
        private const val KEY_VIBRATE_ON_ALERT = "vibrate_on_alert"
        private const val KEY_SUBTLE_HAPTICS = "subtle_haptics"
        private const val KEY_CHECK_IN_INTERVAL = "check_in_interval"
    }
}

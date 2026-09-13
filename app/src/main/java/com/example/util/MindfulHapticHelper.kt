package com.example.util

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.example.data.PreferencesManager

object MindfulHapticHelper {

    private fun getVibrator(context: Context): Vibrator? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vm?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun isHapticsEnabled(context: Context): Boolean {
        return try {
            PreferencesManager(context).loadSettings().subtleHapticsEnabled
        } catch (e: Exception) {
            true
        }
    }

    /**
     * Subtle micro-tick when the user drags an emotion check circle past the dismissal threshold.
     */
    fun triggerThresholdTick(context: Context) {
        if (!isHapticsEnabled(context)) return
        val vibrator = getVibrator(context) ?: return
        if (!vibrator.hasVibrator()) return

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK))
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(12, 60))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(12)
            }
        } catch (e: Exception) {
            // Ignore if vibration service fails on device
        }
    }

    /**
     * Subtle tactile confirmation when a user successfully swipes away an emotion check-in
     * (either from the in-app gesture or the notification dismissal).
     * Crisp, gentle, and peaceful.
     */
    fun triggerDismissSuccess(context: Context) {
        if (!isHapticsEnabled(context)) return
        val vibrator = getVibrator(context) ?: return
        if (!vibrator.hasVibrator()) return

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val timings = longArrayOf(0, 18, 32, 22)
                val amplitudes = intArrayOf(0, 90, 0, 130)
                vibrator.vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(longArrayOf(0, 18, 32, 22), -1)
            }
        } catch (e: Exception) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(20, 110))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(20)
                }
            } catch (_: Exception) {}
        }
    }

    /**
     * Subtle haptic feedback when completing an interaction
     * (e.g. saving a reflection, adding a schedule, or logging an emotion).
     */
    fun triggerCompletionSuccess(context: Context) {
        if (!isHapticsEnabled(context)) return
        val vibrator = getVibrator(context) ?: return
        if (!vibrator.hasVibrator()) return

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val timings = longArrayOf(0, 15, 30, 25)
                val amplitudes = intArrayOf(0, 80, 0, 120)
                vibrator.vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(longArrayOf(0, 15, 30, 25), -1)
            }
        } catch (e: Exception) {
            // Ignore if vibration fails
        }
    }

    /**
     * Subtle micro-tap for buttons, chip selections, and switches.
     */
    fun triggerSubtleClick(context: Context) {
        if (!isHapticsEnabled(context)) return
        val vibrator = getVibrator(context) ?: return
        if (!vibrator.hasVibrator()) return

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK))
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(10, 60))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(10)
            }
        } catch (e: Exception) {
            // Ignore if vibration fails
        }
    }

    /**
     * Gentle sensory cue for breathing transitions (Inhale, Hold, Exhale).
     * Soft and comforting so users can follow the rhythm with their eyes closed.
     */
    fun triggerBreathingCue(context: Context) {
        if (!isHapticsEnabled(context)) return
        val vibrator = getVibrator(context) ?: return
        if (!vibrator.hasVibrator()) return

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val timings = longArrayOf(0, 16, 24, 18)
                val amplitudes = intArrayOf(0, 70, 0, 95)
                vibrator.vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(25)
            }
        } catch (e: Exception) {
            // Ignore if vibration fails
        }
    }
}

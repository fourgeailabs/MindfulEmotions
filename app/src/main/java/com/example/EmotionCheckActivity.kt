package com.example

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.data.MindfulQuote
import com.example.data.PreferencesManager
import com.example.data.QuotesRepository
import com.example.receiver.AlarmReceiver
import com.example.ui.components.FullScreenEmotionCheckContent
import com.example.ui.theme.MyApplicationTheme

class EmotionCheckActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Turn screen on and show over lockscreen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            @Suppress("DEPRECATION")
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
            )
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val prefs = PreferencesManager(this).loadSettings()
        val quoteId = intent.getIntExtra(EXTRA_QUOTE_ID, -1)
        val quoteText = intent.getStringExtra(EXTRA_QUOTE_TEXT)
        val quoteAuthor = intent.getStringExtra(EXTRA_QUOTE_AUTHOR)
        val promptWording = intent.getStringExtra(EXTRA_PROMPT_WORDING) ?: prefs.promptWording

        val quote = if (quoteId != -1 && quoteText != null && quoteAuthor != null) {
            MindfulQuote(quoteId, quoteText, quoteAuthor, "Life")
        } else {
            QuotesRepository.getRandomQuote()
        }

        setContent {
            MyApplicationTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    FullScreenEmotionCheckContent(
                        promptWording = promptWording,
                        initialQuote = quote,
                        onDismiss = {
                            dismissAndFinish()
                        }
                    )
                }
            }
        }
    }

    private fun dismissAndFinish() {
        // Cancel notification
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
        notificationManager?.cancel(AlarmReceiver.NOTIFICATION_ID)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask()
        } else {
            finish()
        }
    }

    companion object {
        const val EXTRA_QUOTE_ID = "extra_quote_id"
        const val EXTRA_QUOTE_TEXT = "extra_quote_text"
        const val EXTRA_QUOTE_AUTHOR = "extra_quote_author"
        const val EXTRA_PROMPT_WORDING = "extra_prompt_wording"
    }
}

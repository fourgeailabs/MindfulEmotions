package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.MindfulDatabase
import com.example.data.MindfulQuote
import com.example.data.MindfulnessPromptsRepository
import com.example.data.QuotesRepository
import com.example.data.model.EmotionCheckLog
import com.example.data.model.JournalEntry
import com.example.ui.theme.EmotionGratitude
import com.example.ui.theme.EmotionHeavy
import com.example.ui.theme.EmotionJoy
import com.example.ui.theme.EmotionLove
import com.example.ui.theme.EmotionPeace
import com.example.ui.theme.EmotionTired
import com.example.ui.theme.PastelCream
import com.example.ui.theme.PastelPeach
import com.example.ui.theme.PastelPeachLight
import com.example.ui.theme.PastelSage
import com.example.ui.theme.PastelSageLight
import com.example.ui.theme.PastelSurface
import com.example.ui.theme.PastelTextPrimary
import com.example.ui.theme.PastelTextSecondary
import com.example.util.MindfulHapticHelper
import kotlinx.coroutines.launch

data class EmotionOption(
    val name: String,
    val emoji: String,
    val color: Color
)

val defaultEmotions = listOf(
    EmotionOption("Peaceful", "🌿", EmotionPeace),
    EmotionOption("Joyful", "☀️", EmotionJoy),
    EmotionOption("Loving", "💖", EmotionLove),
    EmotionOption("Grateful", "🌸", EmotionGratitude),
    EmotionOption("Centered", "🪷", PastelSage),
    EmotionOption("Tired", "🌙", EmotionTired),
    EmotionOption("Agitated", "🌪️", EmotionHeavy),
    EmotionOption("Overwhelmed", "🌊", PastelPeach)
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FullScreenEmotionCheckContent(
    modifier: Modifier = Modifier,
    promptWording: String,
    initialQuote: MindfulQuote = QuotesRepository.getRandomQuote(),
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var currentQuote by remember { mutableStateOf(initialQuote) }
    var selectedEmotion by remember { mutableStateOf<String?>(null) }
    var acknowledgementMessage by remember { mutableStateOf<String?>(null) }
    var showQuickJournalOption by remember { mutableStateOf(false) }
    var quickNoteText by remember { mutableStateOf("") }

    fun selectEmotion(emotion: EmotionOption) {
        selectedEmotion = emotion.name
        acknowledgementMessage = "Embracing this moment with warmth and kindness."
        MindfulHapticHelper.triggerSubtleClick(context)
        coroutineScope.launch {
            try {
                val db = MindfulDatabase.getDatabase(context)
                db.emotionCheckDao().insertLog(
                    EmotionCheckLog(
                        selectedEmotion = emotion.name,
                        promptQuestion = promptWording,
                        quoteText = currentQuote.text,
                        quoteAuthor = currentQuote.author
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        PastelCream,
                        Color(0xFFFFF7F2),
                        Color(0xFFF9F5F0)
                    )
                )
            )
            .padding(horizontal = 24.dp, vertical = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 540.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // TOP CENTER: Mindfulness icon & The actual emotion check wording
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Calming emblem
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(PastelPeachLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Spa,
                        contentDescription = "Mindfulness",
                        tint = PastelPeach,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // TOP CENTER ACTUAL WORDING:
                Text(
                    text = promptWording,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Light,
                        letterSpacing = (-0.2).sp
                    ),
                    textAlign = TextAlign.Center,
                    color = PastelTextPrimary,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Subtle feeling reflection chips
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    defaultEmotions.forEach { emotion ->
                        val isSelected = selectedEmotion == emotion.name
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .clickable { selectEmotion(emotion) }
                                .testTag("emotion_${emotion.name.lowercase()}"),
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) emotion.color.copy(alpha = 0.25f) else PastelSurface,
                            border = if (isSelected) {
                                androidx.compose.foundation.BorderStroke(1.5.dp, emotion.color)
                            } else {
                                androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE8E0D7))
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = emotion.emoji,
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(end = 6.dp)
                                )
                                Text(
                                    text = emotion.name,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                        fontSize = 12.sp
                                    ),
                                    color = if (isSelected) PastelTextPrimary else PastelTextSecondary
                                )
                            }
                        }
                    }
                }

                AnimatedVisibility(
                    visible = acknowledgementMessage != null,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Text(
                        text = acknowledgementMessage ?: "",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontStyle = FontStyle.Italic,
                            fontSize = 12.sp
                        ),
                        color = PastelSage,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // CENTER: Tactile Swipe Away Circle
            SwipeDismissCircle(
                modifier = Modifier.padding(vertical = 12.dp),
                onDismiss = {
                    showQuickJournalOption = true
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // BOTTOM CENTER: 1-2 sentence mindful quote about emotions, life, love, anger
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = PastelSurface
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEEE6DE)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Category theme badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(PastelSageLight)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Reflections on ${currentQuote.theme}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 11.sp
                                ),
                                color = PastelSage
                            )
                        }

                        IconButton(
                            onClick = {
                                MindfulHapticHelper.triggerSubtleClick(context)
                                currentQuote = QuotesRepository.getRandomQuote()
                            },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "New Quote",
                                tint = PastelTextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FormatQuote,
                            contentDescription = null,
                            tint = PastelPeach.copy(alpha = 0.6f),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "\"${currentQuote.text}\"",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 15.sp,
                            lineHeight = 24.sp,
                            fontStyle = FontStyle.Normal
                        ),
                        textAlign = TextAlign.Center,
                        color = PastelTextPrimary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "— ${currentQuote.author}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp,
                            letterSpacing = 0.5.sp
                        ),
                        color = PastelPeach,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Post-Dismissal Unobtrusive Quick Journal Option
        if (showQuickJournalOption) {
            val todayPrompt = remember { MindfulnessPromptsRepository.getTodayPrompt() }
            Dialog(onDismissRequest = { onDismiss() }) {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = PastelSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEEE6DF)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("post_dismiss_journal_dialog")
                ) {
                    Column(
                        modifier = Modifier
                            .padding(22.dp)
                            .fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(PastelPeachLight),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Spa,
                                        contentDescription = null,
                                        tint = PastelPeach,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Record this moment?",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = PastelTextPrimary
                                )
                            }

                            IconButton(
                                onClick = onDismiss,
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Dismiss",
                                    tint = PastelTextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "A gentle, private space to capture how you are feeling right now.",
                            style = MaterialTheme.typography.bodySmall,
                            color = PastelTextSecondary
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Daily prompt
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(PastelCream)
                                .padding(10.dp)
                        ) {
                            Text(
                                text = "\"$todayPrompt\"",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontStyle = FontStyle.Italic,
                                    fontSize = 12.sp,
                                    lineHeight = 18.sp
                                ),
                                color = PastelTextPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Current feeling:",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = PastelTextSecondary
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            defaultEmotions.forEach { emotion ->
                                val isSelected = (selectedEmotion ?: "Peaceful") == emotion.name
                                Surface(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable {
                                            MindfulHapticHelper.triggerSubtleClick(context)
                                            selectedEmotion = emotion.name
                                        },
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isSelected) emotion.color.copy(alpha = 0.25f) else PastelCream,
                                    border = if (isSelected) {
                                        androidx.compose.foundation.BorderStroke(1.2.dp, emotion.color)
                                    } else {
                                        androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5DDD5))
                                    }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = emotion.emoji, fontSize = 11.sp, modifier = Modifier.padding(end = 4.dp))
                                        Text(
                                            text = emotion.name,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontSize = 11.sp,
                                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                            ),
                                            color = if (isSelected) PastelTextPrimary else PastelTextSecondary
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = quickNoteText,
                            onValueChange = { quickNoteText = it },
                            placeholder = {
                                Text(
                                    text = "A brief note or thought (optional)...",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = PastelTextSecondary.copy(alpha = 0.6f)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(90.dp)
                                .testTag("quick_journal_note_input"),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PastelPeach,
                                unfocusedBorderColor = Color(0xFFE5DDD5),
                                focusedContainerColor = PastelCream,
                                unfocusedContainerColor = PastelCream
                            ),
                            maxLines = 3
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(
                                onClick = onDismiss,
                                modifier = Modifier.testTag("skip_journal_button")
                            ) {
                                Text("Skip & Dismiss", color = PastelTextSecondary, fontSize = 13.sp)
                            }

                            Button(
                                onClick = {
                                    val emotionName = selectedEmotion ?: "Peaceful"
                                    val emotionObj = defaultEmotions.find { it.name == emotionName }
                                    val note = quickNoteText.ifBlank { "Reflected with gentle awareness." }
                                    MindfulHapticHelper.triggerCompletionSuccess(context)
                                    coroutineScope.launch {
                                        try {
                                            val db = MindfulDatabase.getDatabase(context)
                                            db.journalDao().insertEntry(
                                                JournalEntry(
                                                    emotion = emotionName,
                                                    emotionEmoji = emotionObj?.emoji ?: "🌿",
                                                    note = note.trim(),
                                                    promptQuestion = todayPrompt,
                                                    quoteSnippet = currentQuote.text
                                                )
                                            )
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                        onDismiss()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = PastelPeach),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.testTag("save_quick_journal_button")
                            ) {
                                Text("Save Reflection", fontSize = 13.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

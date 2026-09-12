package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.MindfulnessPromptsRepository
import com.example.data.model.JournalEntry
import com.example.ui.components.defaultEmotions
import com.example.ui.theme.PastelCream
import com.example.ui.theme.PastelPeach
import com.example.ui.theme.PastelPeachLight
import com.example.ui.theme.PastelSage
import com.example.ui.theme.PastelSageLight
import com.example.ui.theme.PastelSurface
import com.example.ui.theme.PastelTextPrimary
import com.example.ui.theme.PastelTextSecondary
import com.example.util.MindfulHapticHelper
import java.util.Calendar

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MoodJournalScreen(
    journalEntries: List<JournalEntry>,
    onAddEntry: (JournalEntry) -> Unit,
    onDeleteEntry: (JournalEntry) -> Unit
) {
    val context = LocalContext.current
    var selectedFilter by remember { mutableStateOf("All") }
    var showNewEntryDialog by remember { mutableStateOf(false) }
    var entryToDelete by remember { mutableStateOf<JournalEntry?>(null) }

    // Today's mindfulness prompt state (starts with calendar day index out of 1,000)
    val dayOfYear = remember { Calendar.getInstance().get(Calendar.DAY_OF_YEAR) }
    var activePromptIndex by remember { mutableIntStateOf((dayOfYear - 1) % MindfulnessPromptsRepository.PROMPTS_COUNT) }
    val currentPrompt = MindfulnessPromptsRepository.getPromptByIndex(activePromptIndex)

    val filteredEntries = remember(journalEntries, selectedFilter) {
        if (selectedFilter == "All") {
            journalEntries
        } else {
            journalEntries.filter { it.emotion.equals(selectedFilter, ignoreCase = true) }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PastelCream)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Mood Journal",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Normal),
                            color = PastelTextPrimary
                        )
                        Text(
                            text = "Private, mindful reflections saved securely on device.",
                            style = MaterialTheme.typography.bodySmall,
                            color = PastelTextSecondary
                        )
                    }
                }
            }

            // Daily Mindfulness Prompt Card (from the 1,000 prompts repository)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("daily_prompt_card"),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = PastelSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEEE5DC))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clip(CircleShape)
                                        .background(PastelPeachLight),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = PastelPeach,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Daily Mindfulness Prompt #${activePromptIndex + 1} / 1,000",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        letterSpacing = 0.5.sp
                                    ),
                                    color = PastelPeach
                                )
                            }

                            IconButton(
                                onClick = {
                                    activePromptIndex = (activePromptIndex + 1) % MindfulnessPromptsRepository.PROMPTS_COUNT
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Cycle Prompt",
                                    tint = PastelPeach,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "\"$currentPrompt\"",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 15.sp,
                                lineHeight = 23.sp,
                                fontWeight = FontWeight.Normal
                            ),
                            color = PastelTextPrimary
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            OutlinedButton(
                                onClick = { showNewEntryDialog = true },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = PastelPeach
                                ),
                                border = androidx.compose.foundation.BorderStroke(1.dp, PastelPeach)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.EditNote,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Reflect on this", fontSize = 13.sp)
                            }
                        }
                    }
                }
            }

            // Emotion Filter Chips
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val filterOptions = listOf("All") + defaultEmotions.map { it.name }
                    filterOptions.forEach { filter ->
                        val isSelected = selectedFilter == filter
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .clickable {
                                    MindfulHapticHelper.triggerSubtleClick(context)
                                    selectedFilter = filter
                                },
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) PastelPeachLight else PastelSurface,
                            border = if (isSelected) {
                                androidx.compose.foundation.BorderStroke(1.2.dp, PastelPeach)
                            } else {
                                androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE8DFD7))
                            }
                        ) {
                            val emoji = defaultEmotions.find { it.name == filter }?.emoji ?: ""
                            Text(
                                text = if (emoji.isNotEmpty()) "$emoji $filter" else filter,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                    fontSize = 12.sp
                                ),
                                color = if (isSelected) PastelPeach else PastelTextSecondary,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }

            // List of Journal Entries
            if (filteredEntries.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = PastelSurface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEEE6DF))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(PastelSageLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SelfImprovement,
                                    contentDescription = null,
                                    tint = PastelSage,
                                    modifier = Modifier.size(32.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = if (selectedFilter == "All") "Your private journal is peaceful and clear." else "No reflections for \"$selectedFilter\".",
                                style = MaterialTheme.typography.titleMedium,
                                color = PastelTextPrimary,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Take a moment to record your thoughts, feelings, or lessons after your emotion check-ins.",
                                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 20.sp),
                                color = PastelTextSecondary,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(18.dp))

                            Button(
                                onClick = { showNewEntryDialog = true },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = PastelPeach)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("New Reflection")
                            }
                        }
                    }
                }
            } else {
                items(filteredEntries, key = { it.id }) { entry ->
                    JournalCard(
                        entry = entry,
                        onDelete = { entryToDelete = entry }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        // Floating Action Button to Add New Reflection
        FloatingActionButton(
            onClick = { showNewEntryDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
                .testTag("add_journal_fab"),
            containerColor = PastelPeach,
            contentColor = Color.White,
            shape = CircleShape
        ) {
            Icon(Icons.Default.EditNote, contentDescription = "Add Reflection", modifier = Modifier.size(26.dp))
        }

        // Dialog for New Journal Entry
        if (showNewEntryDialog) {
            NewJournalEntryDialog(
                defaultPrompt = currentPrompt,
                onDismiss = { showNewEntryDialog = false },
                onSave = { emotion, emoji, note, prompt ->
                    onAddEntry(
                        JournalEntry(
                            emotion = emotion,
                            emotionEmoji = emoji,
                            note = note,
                            promptQuestion = prompt
                        )
                    )
                    showNewEntryDialog = false
                    Toast.makeText(context, "Reflection saved privately 🌿", Toast.LENGTH_SHORT).show()
                }
            )
        }

        // Delete Confirmation Dialog
        entryToDelete?.let { entry ->
            AlertDialog(
                onDismissRequest = { entryToDelete = null },
                title = { Text("Delete Reflection", color = PastelTextPrimary) },
                text = { Text("Are you sure you wish to remove this reflection from your private journal?", color = PastelTextSecondary) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onDeleteEntry(entry)
                            entryToDelete = null
                        }
                    ) {
                        Text("Delete", color = Color(0xFFC05445))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { entryToDelete = null }) {
                        Text("Keep", color = PastelTextSecondary)
                    }
                },
                containerColor = PastelSurface,
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}

@Composable
fun JournalCard(
    entry: JournalEntry,
    onDelete: () -> Unit
) {
    val emotionObj = defaultEmotions.find { it.name.equals(entry.emotion, ignoreCase = true) }
    val badgeColor = emotionObj?.color ?: PastelSage

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = PastelSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEEE6DF))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(badgeColor.copy(alpha = 0.2f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${entry.emotionEmoji.ifEmpty { emotionObj?.emoji ?: "🌿" }} ${entry.emotion}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 11.sp
                            ),
                            color = PastelTextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = entry.formattedDate(),
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = PastelTextSecondary
                    )
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Delete entry",
                        tint = PastelTextSecondary.copy(alpha = 0.6f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            if (entry.promptQuestion.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "\"${entry.promptQuestion}\"",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontStyle = FontStyle.Italic,
                        color = PastelPeach,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = entry.note,
                style = MaterialTheme.typography.bodyMedium.copy(
                    lineHeight = 22.sp,
                    fontSize = 14.sp
                ),
                color = PastelTextPrimary
            )

            if (entry.quoteSnippet.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(PastelCream)
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.FormatQuote,
                        contentDescription = null,
                        tint = PastelPeach,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = entry.quoteSnippet,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.sp,
                            fontStyle = FontStyle.Italic
                        ),
                        color = PastelTextSecondary
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NewJournalEntryDialog(
    defaultPrompt: String,
    onDismiss: () -> Unit,
    onSave: (emotion: String, emoji: String, note: String, prompt: String) -> Unit
) {
    val context = LocalContext.current
    var promptText by remember { mutableStateOf(defaultPrompt) }
    var selectedEmotion by remember { mutableStateOf(defaultEmotions[0]) }
    var noteText by remember { mutableStateOf("") }
    var promptCycleCount by remember { mutableIntStateOf(0) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = PastelSurface),
            modifier = Modifier.fillMaxWidth()
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
                    Text(
                        text = "Mindful Reflection",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = PastelTextPrimary
                    )

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = PastelTextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Prompt banner with cycle button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(PastelCream)
                        .padding(12.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Introspective Prompt",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                ),
                                color = PastelPeach
                            )

                            IconButton(
                                onClick = {
                                    MindfulHapticHelper.triggerSubtleClick(context)
                                    promptText = MindfulnessPromptsRepository.getRandomPrompt()
                                    promptCycleCount++
                                },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Cycle Prompt",
                                    tint = PastelPeach,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = promptText,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 13.sp,
                                fontStyle = FontStyle.Italic,
                                lineHeight = 19.sp
                            ),
                            color = PastelTextPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "How are you feeling?",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = PastelTextSecondary
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Feeling chips selector
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    defaultEmotions.forEach { emotion ->
                        val isSelected = selectedEmotion.name == emotion.name
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .clickable {
                                    MindfulHapticHelper.triggerSubtleClick(context)
                                    selectedEmotion = emotion
                                },
                            shape = RoundedCornerShape(14.dp),
                            color = if (isSelected) emotion.color.copy(alpha = 0.25f) else PastelCream,
                            border = if (isSelected) {
                                androidx.compose.foundation.BorderStroke(1.2.dp, emotion.color)
                            } else {
                                androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE8E0D8))
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

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Brief Reflection Note",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = PastelTextSecondary
                )

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    placeholder = {
                        Text(
                            text = "Write a brief thought, feeling, or observation...",
                            style = MaterialTheme.typography.bodySmall,
                            color = PastelTextSecondary.copy(alpha = 0.6f)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .testTag("journal_note_input"),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PastelPeach,
                        unfocusedBorderColor = Color(0xFFE5DDD5),
                        focusedContainerColor = PastelCream,
                        unfocusedContainerColor = PastelCream
                    ),
                    maxLines = 4
                )

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = PastelTextSecondary)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            MindfulHapticHelper.triggerCompletionSuccess(context)
                            if (noteText.isBlank()) {
                                noteText = "Reflected with ${selectedEmotion.name} presence."
                            }
                            onSave(selectedEmotion.name, selectedEmotion.emoji, noteText.trim(), promptText)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PastelPeach),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("save_journal_button")
                    ) {
                        Text("Save Reflection")
                    }
                }
            }
        }
    }
}

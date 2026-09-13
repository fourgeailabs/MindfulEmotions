package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.AppSettings
import com.example.ui.theme.PastelCream
import com.example.ui.theme.PastelPeach
import com.example.ui.theme.PastelPeachLight
import com.example.ui.theme.PastelSage
import com.example.ui.theme.PastelSageLight
import com.example.ui.theme.PastelSurface
import com.example.ui.theme.PastelTextPrimary
import com.example.ui.theme.PastelTextSecondary
import com.example.util.MindfulHapticHelper

@Composable
fun SettingsScreen(
    settings: AppSettings,
    onUpdatePromptWording: (String) -> Unit,
    onUpdateUseAndroidAlarm: (Boolean) -> Unit,
    onUpdatePlaySound: (Boolean) -> Unit,
    onUpdateVibrate: (Boolean) -> Unit,
    onUpdateSubtleHaptics: (Boolean) -> Unit = {}
) {
    val context = LocalContext.current
    var showWhatsNewDialog by remember { mutableStateOf(false) }
    var showPromptEditDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(PastelCream)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Preferences & Settings",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Normal),
                color = PastelTextPrimary
            )
            Text(
                text = "Configure mindful check-in wording and alarm behavior.",
                style = MaterialTheme.typography.bodySmall,
                color = PastelTextSecondary
            )
        }

        // Top Center Wording Configuration
        item {
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
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(PastelPeachLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null,
                                    tint = PastelPeach,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Emotion Check Wording",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = PastelTextPrimary
                                )
                                Text(
                                    text = "Shown at the top center of popups",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = PastelTextSecondary
                                )
                            }
                        }

                        IconButton(onClick = { showPromptEditDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Wording",
                                tint = PastelPeach
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(PastelCream)
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "\"${settings.promptWording}\"",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = PastelTextPrimary
                        )
                    }
                }
            }
        }

        // Android Built-in Alarm Settings Option
        item {
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
                        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(PastelSageLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Alarm,
                                    contentDescription = null,
                                    tint = PastelSage,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Android Built-in Alarm",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Medium
                                    ),
                                    color = PastelTextPrimary
                                )
                                Text(
                                    text = "Trigger with system alarm clock and audio stream",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = PastelTextSecondary
                                )
                            }
                        }

                        Switch(
                            checked = settings.useAndroidAlarm,
                            onCheckedChange = {
                                MindfulHapticHelper.triggerSubtleClick(context)
                                onUpdateUseAndroidAlarm(it)
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = PastelSurface,
                                checkedTrackColor = PastelPeach
                            ),
                            modifier = Modifier.testTag("use_android_alarm_switch")
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = Color(0xFFF0E8E1)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Play Alarm Sound",
                                style = MaterialTheme.typography.bodyMedium,
                                color = PastelTextPrimary
                            )
                            Text(
                                text = "Gentle chime or ringtone on notification",
                                style = MaterialTheme.typography.bodySmall,
                                color = PastelTextSecondary
                            )
                        }
                        Switch(
                            checked = settings.playAlarmSound,
                            onCheckedChange = {
                                MindfulHapticHelper.triggerSubtleClick(context)
                                onUpdatePlaySound(it)
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = PastelSurface,
                                checkedTrackColor = PastelPeach
                            )
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = Color(0xFFF0E8E1)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Mindful Vibration",
                                style = MaterialTheme.typography.bodyMedium,
                                color = PastelTextPrimary
                            )
                            Text(
                                text = "Calm sensory pulse to draw mindful focus",
                                style = MaterialTheme.typography.bodySmall,
                                color = PastelTextSecondary
                            )
                        }
                        Switch(
                            checked = settings.vibrateOnAlert,
                            onCheckedChange = {
                                MindfulHapticHelper.triggerSubtleClick(context)
                                onUpdateVibrate(it)
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = PastelSurface,
                                checkedTrackColor = PastelPeach
                            )
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = Color(0xFFF0E8E1)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                            Text(
                                text = "Subtle Haptic Feedback",
                                style = MaterialTheme.typography.bodyMedium,
                                color = PastelTextPrimary
                            )
                            Text(
                                text = "Tactile feedback when swiping away check-ins or completing interactions",
                                style = MaterialTheme.typography.bodySmall,
                                color = PastelTextSecondary
                            )
                        }
                        Switch(
                            checked = settings.subtleHapticsEnabled,
                            onCheckedChange = {
                                MindfulHapticHelper.triggerSubtleClick(context)
                                onUpdateSubtleHaptics(it)
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = PastelSurface,
                                checkedTrackColor = PastelPeach
                            ),
                            modifier = Modifier.testTag("subtle_haptics_switch")
                        )
                    }
                }
            }
        }

        // What's New Menu Entry
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showWhatsNewDialog = true }
                    .testTag("whats_new_menu_button"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = PastelSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEEE6DF))
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(PastelPeachLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.NewReleases,
                                contentDescription = null,
                                tint = PastelPeach,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "What's New",
                                style = MaterialTheme.typography.titleMedium,
                                color = PastelTextPrimary
                            )
                            Text(
                                text = "Current & historical release updates",
                                style = MaterialTheme.typography.bodySmall,
                                color = PastelTextSecondary
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = "Open",
                        tint = PastelTextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        // About Section (FourgeAI LABS)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = PastelSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEEE6DF))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(PastelSageLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = PastelSage,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "About Mindful Emotions",
                            style = MaterialTheme.typography.titleMedium,
                            color = PastelTextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Version: 1.05.00",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = PastelTextSecondary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Creator Link
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://github.com/fourgeailabs")
                                )
                                context.startActivity(intent)
                            }
                            .background(PastelPeachLight)
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "App Creator",
                                style = MaterialTheme.typography.labelSmall,
                                color = PastelTextSecondary
                            )
                            Text(
                                text = "FourgeAI LABS",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = PastelPeach
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.OpenInNew,
                            contentDescription = "GitHub",
                            tint = PastelPeach,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // App GitHub Link
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://github.com/fourgeailabs/mindfulemotions")
                                )
                                context.startActivity(intent)
                            }
                            .background(PastelCream)
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Source Repository",
                                style = MaterialTheme.typography.labelSmall,
                                color = PastelTextSecondary
                            )
                            Text(
                                text = "github.com/fourgeailabs/mindfulemotions",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = PastelTextPrimary
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.OpenInNew,
                            contentDescription = "Repository",
                            tint = PastelTextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }

    if (showPromptEditDialog) {
        PromptEditDialog(
            currentPrompt = settings.promptWording,
            onDismiss = { showPromptEditDialog = false },
            onSave = {
                onUpdatePromptWording(it)
                showPromptEditDialog = false
            }
        )
    }

    if (showWhatsNewDialog) {
        WhatsNewDialog(onDismiss = { showWhatsNewDialog = false })
    }
}

@Composable
fun PromptEditDialog(
    currentPrompt: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var text by remember { mutableStateOf(currentPrompt) }
    val presets = listOf(
        "How are you feeling right now?",
        "Take a deep breath. What emotion is present?",
        "Notice your feelings without judgment. How are you?",
        "Check in with yourself: how is your heart today?"
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = PastelSurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "Customize Emotion Prompt",
                    style = MaterialTheme.typography.titleLarge,
                    color = PastelTextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "This is the exact inquiry displayed at the top center of your full-screen check-in popup.",
                    style = MaterialTheme.typography.bodySmall,
                    color = PastelTextSecondary
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PastelPeach,
                        unfocusedBorderColor = Color(0xFFDDD5CE)
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Or choose a mindful preset:",
                    style = MaterialTheme.typography.labelSmall,
                    color = PastelTextSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))

                presets.forEach { preset ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (text == preset) PastelPeachLight else PastelCream)
                            .clickable { text = preset }
                            .padding(8.dp)
                    ) {
                        Text(
                            text = preset,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = if (text == preset) FontWeight.SemiBold else FontWeight.Normal
                            ),
                            color = if (text == preset) PastelPeach else PastelTextPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEFE8E1))
                    ) {
                        Text("Cancel", color = PastelTextPrimary)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        onClick = { onSave(text.ifBlank { "How are you feeling right now?" }) },
                        colors = ButtonDefaults.buttonColors(containerColor = PastelPeach)
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}

data class ReleaseUpdate(
    val version: String,
    val title: String,
    val highlights: List<String>
)

@Composable
fun WhatsNewDialog(onDismiss: () -> Unit) {
    // Dropdown updates that start closed, opening one closes previously opened one
    var expandedIndex by remember { mutableStateOf<Int?>(null) }

    val updates = listOf(
        ReleaseUpdate(
            version = "1.05.00",
            title = "Calm Corner & Guided Breathing Exercise",
            highlights = listOf(
                "Brand-new Calm Corner section designed to help de-escalate acute stress and overwhelm through rhythmic somatic breathing.",
                "Guided shrinking and expanding breathing circle animation with soothing pastel aura, phase prompts, and real-time second countdown.",
                "Curated evidence-based breathing presets: Box Breathing (4-4-4-4), 4-7-8 Relaxing Breath, Calm Balance (4-4), and Deep Sigh (4-2-6).",
                "Gentle sensory haptic cues on breath phase transitions (inhale, hold, exhale) so you can close your eyes and breathe naturally.",
                "Collapsible Body De-escalation Checklist with 3 somatic physical resets: dropping shoulders, unclenching jaw, and grounding feet.",
                "Direct Home Page entry card for instant one-tap access to calming breathing exercises."
            )
        ),
        ReleaseUpdate(
            version = "1.04.00",
            title = "Top-Center Wisdom Quotes on Home Page",
            highlights = listOf(
                "Featured daily wisdom quotes on the top center of the Home Page for immediate mindfulness upon opening the app.",
                "Centered reflection badge and typography offering serene insights on peace, love, life, and emotional awareness.",
                "Quick quote shuffle button and interactive tap gesture with subtle micro-haptic feedback to explore quotes.",
                "Harmonious visual balance pairing the top-center quote banner with upcoming mindful check-in reminders."
            )
        ),
        ReleaseUpdate(
            version = "1.03.00",
            title = "Subtle Tactile Feedback & Interaction Responsiveness",
            highlights = listOf(
                "Subtle haptic feedback triggers when swiping away emotion check-in notifications from the system notification bar.",
                "Tactile feedback trigger on the full-screen floating dismiss circle when reaching the drag threshold and on swipe dismissal.",
                "Responsive micro-haptics when completing interactions: saving mood reflections, creating schedules, and selecting feelings.",
                "Personalized Subtle Haptic Feedback toggle in Settings to control tactile sensations according to your comfort.",
                "Reliable system notification dismiss listener ensuring seamless haptic cues across all Android notification surfaces."
            )
        ),
        ReleaseUpdate(
            version = "1.02.00",
            title = "Flexible Custom Scheduling · No Preset Times or Dates",
            highlights = listOf(
                "Removed all preset schedules, default times, and pre-selected dates/days for true user control.",
                "New Specific Date scheduling mode: schedule one-off check-ins on specific calendar dates with interactive date picking.",
                "Custom Repeating Days mode: pick exact days of the week (Mon–Sun) with zero preset selections.",
                "Smart Alarm Scheduler: handles both date-specific alarms and weekly repeating schedules.",
                "One-time date-specific schedules automatically deactivate after firing to keep your reminder list clean.",
                "Clean slate migration: automatically purges legacy preset schedules from prior installations."
            )
        ),
        ReleaseUpdate(
            version = "1.01.00",
            title = "Mood Journaling & 1,000 Daily Mindfulness Prompts",
            highlights = listOf(
                "Integrated 1,000 daily mindfulness prompts (1-2 sentences each) for gentle introspection and daily reflection.",
                "New intuitive Mood Journal feature: quickly and unobtrusively jot down feelings and notes after dismissing an emotion check.",
                "Dedicated peaceful Mood Journal section with emotion filtering, prompt cycling, and private on-device storage.",
                "Removed Wear OS companion layer to streamline the experience and focus entirely on the mobile experience.",
                "Enhanced swipe-away circle dismiss interaction with seamless reflection dialog transition."
            )
        ),
        ReleaseUpdate(
            version = "1.00.00",
            title = "Initial Release · Mindful Emotions Launch",
            highlights = listOf(
                "Mindful schedule engine with customizable Monday through Sunday selections.",
                "Full-screen emotion check popup with customizable top inquiry and bottom wisdom quotes.",
                "Tactile center circle with smooth swipe-away gesture to dismiss.",
                "Curated library of 50+ quotes on why negative emotions are destructive and why positive feelings benefit yourself and others.",
                "Android system alarm clock integration for reliable wakeup and alarm stream audio.",
                "Exclusively warm pastel light theme with minimalist typography and zero dark mode clutter."
            )
        )
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = PastelSurface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.NewReleases,
                            contentDescription = null,
                            tint = PastelPeach,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "What's New",
                            style = MaterialTheme.typography.titleLarge,
                            color = PastelTextPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Release history and updates. Tap an update to view details.",
                    style = MaterialTheme.typography.bodySmall,
                    color = PastelTextSecondary
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(updates.size) { index ->
                        val update = updates[index]
                        val isExpanded = expandedIndex == index

                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = PastelCream),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEEE6DE)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    // Opening one closes previously opened one; tapping again closes it
                                    expandedIndex = if (isExpanded) null else index
                                }
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "v${update.version}",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = PastelPeach
                                            )
                                        )
                                        Text(
                                            text = update.title,
                                            style = MaterialTheme.typography.titleSmall,
                                            color = PastelTextPrimary
                                        )
                                    }
                                    Icon(
                                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                        contentDescription = if (isExpanded) "Collapse" else "Expand",
                                        tint = PastelPeach
                                    )
                                }

                                AnimatedVisibility(
                                    visible = isExpanded,
                                    enter = expandVertically() + fadeIn(),
                                    exit = shrinkVertically() + fadeOut()
                                ) {
                                    Column(modifier = Modifier.padding(top = 10.dp)) {
                                        update.highlights.forEach { highlight ->
                                            Row(
                                                modifier = Modifier.padding(vertical = 3.dp),
                                                verticalAlignment = Alignment.Top
                                            ) {
                                                Text(
                                                    text = "•",
                                                    color = PastelSage,
                                                    modifier = Modifier.padding(end = 8.dp)
                                                )
                                                Text(
                                                    text = highlight,
                                                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                                                    color = PastelTextSecondary
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = PastelPeach),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Close")
                }
            }
        }
    }
}

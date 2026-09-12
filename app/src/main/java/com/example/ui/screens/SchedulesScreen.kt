package com.example.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.QuotesRepository
import com.example.data.model.EmotionSchedule
import com.example.ui.theme.PastelCream
import com.example.ui.theme.PastelPeach
import com.example.ui.theme.PastelPeachLight
import com.example.ui.theme.PastelSage
import com.example.ui.theme.PastelSageLight
import com.example.ui.theme.PastelSurface
import com.example.ui.theme.PastelTextPrimary
import com.example.ui.theme.PastelTextSecondary
import com.example.util.MindfulHapticHelper
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun SchedulesScreen(
    schedules: List<EmotionSchedule>,
    onAddSchedule: (EmotionSchedule) -> Unit,
    onUpdateSchedule: (EmotionSchedule) -> Unit,
    onDeleteSchedule: (EmotionSchedule) -> Unit,
    onTriggerTestCheckIn: () -> Unit
) {
    val context = LocalContext.current
    var showAddDialog by remember { mutableStateOf(false) }
    var currentQuote by remember { mutableStateOf(QuotesRepository.getRandomQuote()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PastelCream)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Top Center Quotes Card on the Home Page
            item {
                Spacer(modifier = Modifier.height(4.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .clickable {
                            MindfulHapticHelper.triggerSubtleClick(context)
                            currentQuote = QuotesRepository.getRandomQuote()
                        }
                        .testTag("home_top_quote_card"),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = PastelSurface
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEEE6DF)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
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
                                modifier = Modifier
                                    .size(28.dp)
                                    .testTag("home_quote_refresh_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "New Quote",
                                    tint = PastelPeach,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(PastelPeachLight.copy(alpha = 0.7f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.FormatQuote,
                                contentDescription = null,
                                tint = PastelPeach,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "\"${currentQuote.text}\"",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 15.sp,
                                lineHeight = 22.sp,
                                fontWeight = FontWeight.Medium
                            ),
                            textAlign = TextAlign.Center,
                            color = PastelTextPrimary,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "— ${currentQuote.author}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 13.sp,
                                fontStyle = FontStyle.Italic
                            ),
                            textAlign = TextAlign.Center,
                            color = PastelTextSecondary
                        )
                    }
                }
            }

            item {
                // Header banner
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = PastelPeachLight
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(PastelPeach),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Alarm,
                                    contentDescription = null,
                                    tint = PastelSurface,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Mindful Check-Ins",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = PastelTextPrimary
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Schedule custom times throughout your day or on specific dates to pause, reflect, and center your emotions.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = PastelTextSecondary
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Test Check-in action button
                        Button(
                            onClick = onTriggerTestCheckIn,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PastelPeach
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("test_checkin_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Preview Full-Screen Check-in Now",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Scheduled Reminders (${schedules.size})",
                        style = MaterialTheme.typography.titleMedium,
                        color = PastelTextPrimary
                    )

                    OutlinedButton(
                        onClick = { showAddDialog = true },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.testTag("add_schedule_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Schedule Check-in", fontSize = 13.sp)
                    }
                }
            }

            if (schedules.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
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
                                    .background(PastelPeachLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccessTime,
                                    contentDescription = null,
                                    tint = PastelPeach,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = "No Scheduled Check-ins",
                                style = MaterialTheme.typography.titleMedium,
                                color = PastelTextPrimary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "No preset times or dates are pre-configured. Tap '+ Schedule Check-in' to schedule a time on specific dates or repeating days.",
                                style = MaterialTheme.typography.bodySmall,
                                color = PastelTextSecondary,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(18.dp))

                            Button(
                                onClick = { showAddDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = PastelPeach),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.testTag("empty_schedule_button")
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Schedule Check-in")
                            }
                        }
                    }
                }
            } else {
                items(schedules, key = { it.id }) { schedule ->
                    ScheduleItemCard(
                        schedule = schedule,
                        onToggle = { isEnabled ->
                            onUpdateSchedule(schedule.copy(isEnabled = isEnabled))
                        },
                        onDayToggle = { dayIndex ->
                            val updated = when (dayIndex) {
                                0 -> schedule.copy(monday = !schedule.monday)
                                1 -> schedule.copy(tuesday = !schedule.tuesday)
                                2 -> schedule.copy(wednesday = !schedule.wednesday)
                                3 -> schedule.copy(thursday = !schedule.thursday)
                                4 -> schedule.copy(friday = !schedule.friday)
                                5 -> schedule.copy(saturday = !schedule.saturday)
                                6 -> schedule.copy(sunday = !schedule.sunday)
                                else -> schedule
                            }
                            onUpdateSchedule(updated)
                        },
                        onDelete = { onDeleteSchedule(schedule) }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }

    if (showAddDialog) {
        AddScheduleDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { newSchedule ->
                onAddSchedule(newSchedule)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun ScheduleItemCard(
    schedule: EmotionSchedule,
    onToggle: (Boolean) -> Unit,
    onDayToggle: (Int) -> Unit,
    onDelete: () -> Unit
) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("schedule_card_${schedule.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = PastelSurface
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEEE6DF)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = schedule.formattedTime(),
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Normal
                        ),
                        color = if (schedule.isEnabled) PastelTextPrimary else PastelTextSecondary.copy(alpha = 0.6f)
                    )
                    Text(
                        text = schedule.label,
                        style = MaterialTheme.typography.bodySmall,
                        color = PastelTextSecondary
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(
                        checked = schedule.isEnabled,
                        onCheckedChange = { isChecked ->
                            MindfulHapticHelper.triggerSubtleClick(context)
                            onToggle(isChecked)
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = PastelSurface,
                            checkedTrackColor = PastelPeach,
                            uncheckedTrackColor = Color(0xFFE2D9D1)
                        ),
                        modifier = Modifier.testTag("schedule_switch_${schedule.id}")
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    IconButton(
                        onClick = {
                            MindfulHapticHelper.triggerSubtleClick(context)
                            onDelete()
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Delete",
                            tint = PastelTextSecondary.copy(alpha = 0.7f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (schedule.isDateSpecific()) {
                // Specific date badge
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = PastelPeachLight,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Event,
                            contentDescription = null,
                            tint = PastelPeach,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = schedule.activeDaysSummary(),
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp
                            ),
                            color = PastelTextPrimary
                        )
                    }
                }
            } else {
                // Monday - Sunday selector chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val days = listOf("M", "T", "W", "T", "F", "S", "S")
                    val activeDays = listOf(
                        schedule.monday,
                        schedule.tuesday,
                        schedule.wednesday,
                        schedule.thursday,
                        schedule.friday,
                        schedule.saturday,
                        schedule.sunday
                    )

                    days.forEachIndexed { index, label ->
                        val isActive = activeDays[index]
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isActive) PastelSage else PastelSageLight.copy(alpha = 0.4f)
                                )
                                .clickable {
                                    MindfulHapticHelper.triggerSubtleClick(context)
                                    onDayToggle(index)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 12.sp
                                ),
                                color = if (isActive) PastelSurface else PastelTextSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = schedule.activeDaysSummary(),
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = PastelTextSecondary
                )
            }
        }
    }
}

enum class ScheduleMode {
    SPECIFIC_DATE,
    REPEATING_DAYS
}

@Composable
fun AddScheduleDialog(
    onDismiss: () -> Unit,
    onConfirm: (EmotionSchedule) -> Unit
) {
    val context = LocalContext.current
    val now = remember { Calendar.getInstance() }

    // Start with current device time (no preset hardcoded time)
    var selectedHour by remember { mutableStateOf(now.get(Calendar.HOUR_OF_DAY)) }
    var selectedMinute by remember { mutableStateOf(now.get(Calendar.MINUTE)) }
    var label by remember { mutableStateOf("") }

    // Schedule mode: specific calendar date vs repeating days of week
    var scheduleMode by remember { mutableStateOf(ScheduleMode.SPECIFIC_DATE) }

    // Specific Date fields (defaults to today's date for user convenience)
    var selectedYear by remember { mutableStateOf(now.get(Calendar.YEAR)) }
    var selectedMonth by remember { mutableStateOf(now.get(Calendar.MONTH)) }
    var selectedDay by remember { mutableStateOf(now.get(Calendar.DAY_OF_MONTH)) }

    // Repeating days (no days preset to true - user selects them)
    var mon by remember { mutableStateOf(false) }
    var tue by remember { mutableStateOf(false) }
    var wed by remember { mutableStateOf(false) }
    var thu by remember { mutableStateOf(false) }
    var fri by remember { mutableStateOf(false) }
    var sat by remember { mutableStateOf(false) }
    var sun by remember { mutableStateOf(false) }

    val formattedSelectedDate = remember(selectedYear, selectedMonth, selectedDay) {
        val cal = Calendar.getInstance().apply {
            set(Calendar.YEAR, selectedYear)
            set(Calendar.MONTH, selectedMonth)
            set(Calendar.DAY_OF_MONTH, selectedDay)
        }
        val sdf = SimpleDateFormat("EEEE, MMM d, yyyy", Locale.getDefault())
        sdf.format(cal.time)
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = PastelSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEEE6DF)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(22.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Schedule Emotion Check",
                    style = MaterialTheme.typography.titleLarge,
                    color = PastelTextPrimary
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Choose your check-in time and schedule for a specific date or repeating days.",
                    style = MaterialTheme.typography.bodySmall,
                    color = PastelTextSecondary,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Time picker button
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = PastelPeachLight,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val timePicker = TimePickerDialog(
                                context,
                                { _, hour, minute ->
                                    selectedHour = hour
                                    selectedMinute = minute
                                },
                                selectedHour,
                                selectedMinute,
                                false
                            )
                            timePicker.show()
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = PastelPeach,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        val h12 = if (selectedHour == 0) 12 else if (selectedHour > 12) selectedHour - 12 else selectedHour
                        val amPm = if (selectedHour >= 12) "PM" else "AM"
                        val minStr = if (selectedMinute < 10) "0$selectedMinute" else "$selectedMinute"
                        Text(
                            text = "$h12:$minStr $amPm",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = PastelTextPrimary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "(tap to change)",
                            style = MaterialTheme.typography.bodySmall,
                            color = PastelTextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Mode switcher tabs: Specific Date vs Repeating Days
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(PastelCream)
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Specific Date Option
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { scheduleMode = ScheduleMode.SPECIFIC_DATE },
                        color = if (scheduleMode == ScheduleMode.SPECIFIC_DATE) PastelSurface else Color.Transparent,
                        shape = RoundedCornerShape(10.dp),
                        shadowElevation = if (scheduleMode == ScheduleMode.SPECIFIC_DATE) 1.dp else 0.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = if (scheduleMode == ScheduleMode.SPECIFIC_DATE) PastelPeach else PastelTextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Specific Date",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (scheduleMode == ScheduleMode.SPECIFIC_DATE) FontWeight.SemiBold else FontWeight.Normal
                                ),
                                color = if (scheduleMode == ScheduleMode.SPECIFIC_DATE) PastelTextPrimary else PastelTextSecondary
                            )
                        }
                    }

                    // Repeating Days Option
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { scheduleMode = ScheduleMode.REPEATING_DAYS },
                        color = if (scheduleMode == ScheduleMode.REPEATING_DAYS) PastelSurface else Color.Transparent,
                        shape = RoundedCornerShape(10.dp),
                        shadowElevation = if (scheduleMode == ScheduleMode.REPEATING_DAYS) 1.dp else 0.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Repeat,
                                contentDescription = null,
                                tint = if (scheduleMode == ScheduleMode.REPEATING_DAYS) PastelSage else PastelTextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Repeating Days",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (scheduleMode == ScheduleMode.REPEATING_DAYS) FontWeight.SemiBold else FontWeight.Normal
                                ),
                                color = if (scheduleMode == ScheduleMode.REPEATING_DAYS) PastelTextPrimary else PastelTextSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (scheduleMode == ScheduleMode.SPECIFIC_DATE) {
                    // Date picker card
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = PastelCream,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5DDD5)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val datePicker = DatePickerDialog(
                                    context,
                                    { _, year, month, dayOfMonth ->
                                        selectedYear = year
                                        selectedMonth = month
                                        selectedDay = dayOfMonth
                                    },
                                    selectedYear,
                                    selectedMonth,
                                    selectedDay
                                )
                                datePicker.show()
                            }
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(PastelPeachLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CalendarMonth,
                                    contentDescription = null,
                                    tint = PastelPeach,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Date for Check-In",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = PastelTextSecondary
                                )
                                Text(
                                    text = formattedSelectedDate,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                    color = PastelTextPrimary
                                )
                            }
                            Text(
                                text = "Change",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = PastelPeach
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Quick date shortcuts
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = PastelSurface,
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5DDD5)),
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    val t = Calendar.getInstance()
                                    selectedYear = t.get(Calendar.YEAR)
                                    selectedMonth = t.get(Calendar.MONTH)
                                    selectedDay = t.get(Calendar.DAY_OF_MONTH)
                                }
                        ) {
                            Text(
                                text = "Today",
                                style = MaterialTheme.typography.labelSmall,
                                color = PastelTextPrimary,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                modifier = Modifier.padding(vertical = 6.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = PastelSurface,
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5DDD5)),
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    val tm = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) }
                                    selectedYear = tm.get(Calendar.YEAR)
                                    selectedMonth = tm.get(Calendar.MONTH)
                                    selectedDay = tm.get(Calendar.DAY_OF_MONTH)
                                }
                        ) {
                            Text(
                                text = "Tomorrow",
                                style = MaterialTheme.typography.labelSmall,
                                color = PastelTextPrimary,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                modifier = Modifier.padding(vertical = 6.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = PastelSurface,
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5DDD5)),
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    val datePicker = DatePickerDialog(
                                        context,
                                        { _, year, month, dayOfMonth ->
                                            selectedYear = year
                                            selectedMonth = month
                                            selectedDay = dayOfMonth
                                        },
                                        selectedYear,
                                        selectedMonth,
                                        selectedDay
                                    )
                                    datePicker.show()
                                }
                        ) {
                            Text(
                                text = "Pick Date 📅",
                                style = MaterialTheme.typography.labelSmall,
                                color = PastelPeach,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                modifier = Modifier.padding(vertical = 6.dp)
                            )
                        }
                    }
                } else {
                    // Repeating Days selector (no preset days)
                    Text(
                        text = "Select Days to Repeat (Mon - Sun):",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                        color = PastelTextSecondary,
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val days = listOf("M", "T", "W", "T", "F", "S", "S")
                        val toggles = listOf(
                            Pair(mon) { mon = !mon },
                            Pair(tue) { tue = !tue },
                            Pair(wed) { wed = !wed },
                            Pair(thu) { thu = !thu },
                            Pair(fri) { fri = !fri },
                            Pair(sat) { sat = !sat },
                            Pair(sun) { sun = !sun }
                        )

                        days.forEachIndexed { i, d ->
                            val active = toggles[i].first
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(if (active) PastelSage else PastelSageLight.copy(alpha = 0.5f))
                                    .clickable { toggles[i].second() },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = d,
                                    color = if (active) PastelSurface else PastelTextSecondary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Quick day presets chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = PastelCream,
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    mon = true; tue = true; wed = true; thu = true; fri = true
                                    sat = false; sun = false
                                }
                        ) {
                            Text(
                                text = "Weekdays",
                                fontSize = 11.sp,
                                color = PastelTextPrimary,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = PastelCream,
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    mon = false; tue = false; wed = false; thu = false; fri = false
                                    sat = true; sun = true
                                }
                        ) {
                            Text(
                                text = "Weekends",
                                fontSize = 11.sp,
                                color = PastelTextPrimary,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = PastelCream,
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    mon = true; tue = true; wed = true; thu = true; fri = true
                                    sat = true; sun = true
                                }
                        ) {
                            Text(
                                text = "Every Day",
                                fontSize = 11.sp,
                                color = PastelTextPrimary,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = PastelCream,
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    mon = false; tue = false; wed = false; thu = false; fri = false
                                    sat = false; sun = false
                                }
                        ) {
                            Text(
                                text = "Clear",
                                fontSize = 11.sp,
                                color = PastelTextSecondary,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Check-in label field
                OutlinedTextField(
                    value = label,
                    onValueChange = { label = it },
                    placeholder = {
                        Text(
                            text = "Check-in Label (e.g. Afternoon Reset, Bedtime Gratitude)",
                            fontSize = 12.sp,
                            color = PastelTextSecondary.copy(alpha = 0.6f)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PastelPeach,
                        unfocusedBorderColor = Color(0xFFE5DDD5),
                        focusedContainerColor = PastelCream,
                        unfocusedContainerColor = PastelCream
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = {
                            if (scheduleMode == ScheduleMode.REPEATING_DAYS) {
                                val hasAnyDay = mon || tue || wed || thu || fri || sat || sun
                                if (!hasAnyDay) {
                                    Toast.makeText(context, "Please select at least one day to repeat, or switch to Specific Date.", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                MindfulHapticHelper.triggerCompletionSuccess(context)
                                onConfirm(
                                    EmotionSchedule(
                                        hour = selectedHour,
                                        minute = selectedMinute,
                                        label = label.ifBlank { "Daily Emotion Check" },
                                        monday = mon,
                                        tuesday = tue,
                                        wednesday = wed,
                                        thursday = thu,
                                        friday = fri,
                                        saturday = sat,
                                        sunday = sun,
                                        isEnabled = true
                                    )
                                )
                            } else {
                                MindfulHapticHelper.triggerCompletionSuccess(context)
                                onConfirm(
                                    EmotionSchedule(
                                        hour = selectedHour,
                                        minute = selectedMinute,
                                        label = label.ifBlank { "Mindful Check-in" },
                                        specificYear = selectedYear,
                                        specificMonth = selectedMonth,
                                        specificDay = selectedDay,
                                        isEnabled = true
                                    )
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PastelPeach),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Save Schedule")
                    }
                }
            }
        }
    }
}

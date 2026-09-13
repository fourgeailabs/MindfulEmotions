package com.example

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.FullScreenEmotionCheckContent
import com.example.ui.screens.CalmCornerScreen
import com.example.ui.screens.MoodJournalScreen
import com.example.ui.screens.QuotesHistoryScreen
import com.example.ui.screens.SchedulesScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.PastelCream
import com.example.ui.theme.PastelPeach
import com.example.ui.theme.PastelPeachLight
import com.example.ui.theme.PastelSage
import com.example.ui.theme.PastelSurface
import com.example.ui.theme.PastelTextPrimary
import com.example.ui.theme.PastelTextSecondary
import com.example.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                MainAppScreen(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) }

    val schedules by viewModel.schedules.collectAsStateWithLifecycle()
    val logs by viewModel.logs.collectAsStateWithLifecycle()
    val journalEntries by viewModel.journalEntries.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val isShowingFullScreenPopup by viewModel.isShowingFullScreenPopup.collectAsStateWithLifecycle()

    // Permission launcher for Android 13+ notifications
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(
                context,
                "Notifications enabled in settings help deliver timely emotion check-ins.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
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
                            androidx.compose.foundation.layout.Spacer(modifier = Modifier.size(8.dp))
                            Text(
                                text = "Mindful Emotions",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Medium,
                                    letterSpacing = 0.3.sp
                                ),
                                color = PastelTextPrimary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = PastelSurface
                    ),
                    actions = {
                        IconButton(
                            onClick = {
                                viewModel.showFullScreenPopup()
                            },
                            modifier = Modifier.testTag("quick_checkin_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Quick Check-in",
                                tint = PastelPeach
                            )
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar(
                    modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
                    containerColor = PastelSurface,
                    tonalElevation = 0.dp
                ) {
                    NavigationBarItem(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        icon = {
                            Icon(Icons.Default.Alarm, contentDescription = "Schedules")
                        },
                        label = { Text("Reminders", fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PastelPeach,
                            selectedTextColor = PastelPeach,
                            indicatorColor = PastelPeachLight,
                            unselectedIconColor = PastelTextSecondary,
                            unselectedTextColor = PastelTextSecondary
                        ),
                        modifier = Modifier.testTag("nav_schedules")
                    )

                    NavigationBarItem(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        icon = {
                            Icon(Icons.Default.SelfImprovement, contentDescription = "Calm Corner")
                        },
                        label = { Text("Calm", fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PastelPeach,
                            selectedTextColor = PastelPeach,
                            indicatorColor = PastelPeachLight,
                            unselectedIconColor = PastelTextSecondary,
                            unselectedTextColor = PastelTextSecondary
                        ),
                        modifier = Modifier.testTag("nav_calm_corner")
                    )

                    NavigationBarItem(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        icon = {
                            Icon(Icons.Default.EditNote, contentDescription = "Mood Journal")
                        },
                        label = { Text("Journal", fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PastelPeach,
                            selectedTextColor = PastelPeach,
                            indicatorColor = PastelPeachLight,
                            unselectedIconColor = PastelTextSecondary,
                            unselectedTextColor = PastelTextSecondary
                        ),
                        modifier = Modifier.testTag("nav_journal")
                    )

                    NavigationBarItem(
                        selected = selectedTab == 3,
                        onClick = { selectedTab = 3 },
                        icon = {
                            Icon(Icons.Default.FormatQuote, contentDescription = "Wisdom & History")
                        },
                        label = { Text("Wisdom", fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PastelPeach,
                            selectedTextColor = PastelPeach,
                            indicatorColor = PastelPeachLight,
                            unselectedIconColor = PastelTextSecondary,
                            unselectedTextColor = PastelTextSecondary
                        ),
                        modifier = Modifier.testTag("nav_wisdom")
                    )

                    NavigationBarItem(
                        selected = selectedTab == 4,
                        onClick = { selectedTab = 4 },
                        icon = {
                            Icon(Icons.Default.Settings, contentDescription = "Settings")
                        },
                        label = { Text("Settings", fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PastelPeach,
                            selectedTextColor = PastelPeach,
                            indicatorColor = PastelPeachLight,
                            unselectedIconColor = PastelTextSecondary,
                            unselectedTextColor = PastelTextSecondary
                        ),
                        modifier = Modifier.testTag("nav_settings")
                    )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (selectedTab) {
                    0 -> SchedulesScreen(
                        schedules = schedules,
                        onAddSchedule = { viewModel.addSchedule(it) },
                        onUpdateSchedule = { viewModel.updateSchedule(it) },
                        onDeleteSchedule = { viewModel.deleteSchedule(it) },
                        onTriggerTestCheckIn = {
                            viewModel.showFullScreenPopup()
                        },
                        onNavigateToCalmCorner = {
                            selectedTab = 1
                        }
                    )
                    1 -> CalmCornerScreen()
                    2 -> MoodJournalScreen(
                        journalEntries = journalEntries,
                        onAddEntry = { viewModel.addJournalEntry(it) },
                        onDeleteEntry = { viewModel.deleteJournalEntry(it) }
                    )
                    3 -> QuotesHistoryScreen(
                        logs = logs
                    )
                    4 -> SettingsScreen(
                        settings = settings,
                        onUpdatePromptWording = { viewModel.updatePromptWording(it) },
                        onUpdateUseAndroidAlarm = { viewModel.updateUseAndroidAlarm(it) },
                        onUpdatePlaySound = { viewModel.updatePlaySound(it) },
                        onUpdateVibrate = { viewModel.updateVibrate(it) },
                        onUpdateSubtleHaptics = { viewModel.updateSubtleHaptics(it) }
                    )
                }
            }
        }

        // Full Screen Popup Overlay (Can be opened in-app or via EmotionCheckActivity)
        AnimatedVisibility(
            visible = isShowingFullScreenPopup,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
        ) {
            FullScreenEmotionCheckContent(
                promptWording = settings.promptWording,
                onDismiss = {
                    viewModel.dismissFullScreenPopup()
                }
            )
        }
    }
}

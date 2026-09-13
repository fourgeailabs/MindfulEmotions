package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PastelCream
import com.example.ui.theme.PastelPeach
import com.example.ui.theme.PastelPeachDark
import com.example.ui.theme.PastelPeachLight
import com.example.ui.theme.PastelSage
import com.example.ui.theme.PastelSageLight
import com.example.ui.theme.PastelSurface
import com.example.ui.theme.PastelSurfaceVariant
import com.example.ui.theme.PastelTextPrimary
import com.example.ui.theme.PastelTextSecondary
import com.example.util.MindfulHapticHelper
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

enum class BreathingPhase(val label: String, val instruction: String) {
    INHALE("Inhale", "Breathe in slowly through your nose, expanding your belly"),
    HOLD_IN("Hold", "Pause gently in stillness, keeping your body soft"),
    EXHALE("Exhale", "Release your breath slowly, releasing tension and stress"),
    HOLD_OUT("Rest", "Rest in the quiet pause before the next breath")
}

data class BreathingTechnique(
    val id: String,
    val name: String,
    val subtitle: String,
    val description: String,
    val inhaleSeconds: Int,
    val holdInSeconds: Int,
    val exhaleSeconds: Int,
    val holdOutSeconds: Int
) {
    val totalSeconds: Int get() = inhaleSeconds + holdInSeconds + exhaleSeconds + holdOutSeconds
}

val BreathingTechniques = listOf(
    BreathingTechnique(
        id = "box",
        name = "Box Breathing",
        subtitle = "4-4-4-4",
        description = "Navy SEAL technique for instant focus and nervous system reset",
        inhaleSeconds = 4,
        holdInSeconds = 4,
        exhaleSeconds = 4,
        holdOutSeconds = 4
    ),
    BreathingTechnique(
        id = "478",
        name = "4-7-8 Relax",
        subtitle = "4-7-8",
        description = "Deep vagal relaxation to swiftly de-escalate anxiety and acute stress",
        inhaleSeconds = 4,
        holdInSeconds = 7,
        exhaleSeconds = 8,
        holdOutSeconds = 0
    ),
    BreathingTechnique(
        id = "balance",
        name = "Calm Balance",
        subtitle = "4-4",
        description = "Simple equal rhythm for effortless centering throughout the day",
        inhaleSeconds = 4,
        holdInSeconds = 0,
        exhaleSeconds = 4,
        holdOutSeconds = 0
    ),
    BreathingTechnique(
        id = "deep_sigh",
        name = "Deep Sigh",
        subtitle = "4-2-6",
        description = "Extended exhalation to trigger your parasympathetic calming response",
        inhaleSeconds = 4,
        holdInSeconds = 2,
        exhaleSeconds = 6,
        holdOutSeconds = 0
    )
)

@Composable
fun CalmCornerScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedTechnique by remember { mutableStateOf(BreathingTechniques[0]) }
    var isRunning by remember { mutableStateOf(false) }
    var currentPhase by remember { mutableStateOf(BreathingPhase.INHALE) }
    var secondsRemainingInPhase by remember { mutableIntStateOf(selectedTechnique.inhaleSeconds) }
    var totalCyclesCompleted by remember { mutableIntStateOf(0) }
    var showGroundingGuide by remember { mutableStateOf(false) }

    // Circle animated scale:
    // Min scale: 0.58f, Max scale: 1.0f
    val circleScale = remember { Animatable(0.58f) }

    // Subtle pulsing animation when holding breath
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_transition")
    val pulseShimmer by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_shimmer"
    )

    // Breathing engine loop
    LaunchedEffect(isRunning, selectedTechnique) {
        if (!isRunning) {
            // Reset smoothly
            circleScale.animateTo(0.58f, tween(600, easing = FastOutSlowInEasing))
            currentPhase = BreathingPhase.INHALE
            secondsRemainingInPhase = selectedTechnique.inhaleSeconds
            return@LaunchedEffect
        }

        while (isActive && isRunning) {
            // Phase 1: INHALE
            currentPhase = BreathingPhase.INHALE
            MindfulHapticHelper.triggerBreathingCue(context)
            val inhaleSec = selectedTechnique.inhaleSeconds
            // Animate circle expansion simultaneously with timer
            launch {
                circleScale.animateTo(
                    targetValue = 1.0f,
                    animationSpec = tween(inhaleSec * 1000, easing = FastOutSlowInEasing)
                )
            }
            for (s in inhaleSec downTo 1) {
                secondsRemainingInPhase = s
                delay(1000)
            }

            // Phase 2: HOLD IN (if any)
            if (selectedTechnique.holdInSeconds > 0) {
                currentPhase = BreathingPhase.HOLD_IN
                MindfulHapticHelper.triggerBreathingCue(context)
                val holdSec = selectedTechnique.holdInSeconds
                for (s in holdSec downTo 1) {
                    secondsRemainingInPhase = s
                    delay(1000)
                }
            }

            // Phase 3: EXHALE
            currentPhase = BreathingPhase.EXHALE
            MindfulHapticHelper.triggerBreathingCue(context)
            val exhaleSec = selectedTechnique.exhaleSeconds
            // Animate circle shrinking simultaneously with timer
            launch {
                circleScale.animateTo(
                    targetValue = 0.58f,
                    animationSpec = tween(exhaleSec * 1000, easing = FastOutSlowInEasing)
                )
            }
            for (s in exhaleSec downTo 1) {
                secondsRemainingInPhase = s
                delay(1000)
            }

            // Phase 4: HOLD OUT / REST (if any)
            if (selectedTechnique.holdOutSeconds > 0) {
                currentPhase = BreathingPhase.HOLD_OUT
                MindfulHapticHelper.triggerBreathingCue(context)
                val restSec = selectedTechnique.holdOutSeconds
                for (s in restSec downTo 1) {
                    secondsRemainingInPhase = s
                    delay(1000)
                }
            }

            totalCyclesCompleted++
            MindfulHapticHelper.triggerSubtleClick(context)
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(PastelCream)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))

            // Header Banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("calm_corner_header"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = PastelSurface),
                border = BorderStroke(1.dp, Color(0xFFEEE6DF)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(PastelSageLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SelfImprovement,
                            contentDescription = null,
                            tint = PastelSage,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Calm Corner",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 22.sp
                        ),
                        color = PastelTextPrimary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Take a peaceful moment to de-escalate stress through guided rhythmic breathing",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        ),
                        textAlign = TextAlign.Center,
                        color = PastelTextSecondary
                    )
                }
            }
        }

        // Technique Selector Chips
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "SELECT BREATHING PATTERN",
                    style = MaterialTheme.typography.labelSmall.copy(
                        letterSpacing = 1.2.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = PastelTextSecondary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BreathingTechniques.forEach { tech ->
                        val isSelected = selectedTechnique.id == tech.id
                        val chipBg = if (isSelected) PastelPeachLight else PastelSurface
                        val chipBorder = if (isSelected) PastelPeach else Color(0xFFE5DDD5)
                        val chipText = if (isSelected) PastelPeachDark else PastelTextSecondary

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(chipBg)
                                .border(1.dp, chipBorder, RoundedCornerShape(16.dp))
                                .clickable {
                                    MindfulHapticHelper.triggerSubtleClick(context)
                                    selectedTechnique = tech
                                    if (!isRunning) {
                                        secondsRemainingInPhase = tech.inhaleSeconds
                                    }
                                }
                                .padding(vertical = 10.dp, horizontal = 4.dp)
                                .testTag("technique_chip_${tech.id}"),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = tech.name.substringBefore(" "),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        fontSize = 11.sp
                                    ),
                                    color = chipText,
                                    maxLines = 1
                                )
                                Text(
                                    text = tech.subtitle,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 10.sp
                                    ),
                                    color = if (isSelected) PastelPeach else PastelTextSecondary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = selectedTechnique.description,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                    color = PastelTextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
        }

        // Guided Breathing Circle Card (Center Stage)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("breathing_circle_card"),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = PastelSurface),
                border = BorderStroke(1.dp, Color(0xFFEEE6DF)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 28.dp, horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Instruction Tag
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                when (currentPhase) {
                                    BreathingPhase.INHALE -> PastelPeachLight
                                    BreathingPhase.HOLD_IN -> PastelSageLight
                                    BreathingPhase.EXHALE -> PastelPeachLight
                                    BreathingPhase.HOLD_OUT -> PastelSageLight
                                }
                            )
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = if (isRunning) currentPhase.label.uppercase() else "READY TO BEGIN",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.5.sp
                            ),
                            color = when (currentPhase) {
                                BreathingPhase.INHALE, BreathingPhase.EXHALE -> PastelPeach
                                BreathingPhase.HOLD_IN, BreathingPhase.HOLD_OUT -> PastelSage
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Guided Expanding & Shrinking Circle
                    val activeScale = if (isRunning && (currentPhase == BreathingPhase.HOLD_IN || currentPhase == BreathingPhase.HOLD_OUT)) {
                        circleScale.value * pulseShimmer
                    } else {
                        circleScale.value
                    }

                    Box(
                        modifier = Modifier
                            .size(240.dp)
                            .testTag("breathing_animated_container"),
                        contentAlignment = Alignment.Center
                    ) {
                        // Outer ethereal glow ring
                        Box(
                            modifier = Modifier
                                .size(240.dp)
                                .scale(activeScale)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        listOf(
                                            PastelPeachLight.copy(alpha = 0.55f),
                                            PastelSageLight.copy(alpha = 0.25f),
                                            Color.Transparent
                                        )
                                    )
                                )
                        )

                        // Secondary soft boundary ring
                        Box(
                            modifier = Modifier
                                .size(190.dp)
                                .scale(activeScale)
                                .clip(CircleShape)
                                .border(
                                    width = 1.5.dp,
                                    color = if (currentPhase == BreathingPhase.HOLD_IN || currentPhase == BreathingPhase.HOLD_OUT) {
                                        PastelSage.copy(alpha = 0.4f)
                                    } else {
                                        PastelPeach.copy(alpha = 0.4f)
                                    },
                                    shape = CircleShape
                                )
                                .background(
                                    Brush.verticalGradient(
                                        listOf(
                                            PastelPeachLight.copy(alpha = 0.7f),
                                            PastelCream
                                        )
                                    )
                                )
                        )

                        // Core breathing disc with dynamic content
                        Box(
                            modifier = Modifier
                                .size(140.dp)
                                .scale(activeScale)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        listOf(
                                            PastelPeach,
                                            Color(0xFFD99079)
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                if (isRunning) {
                                    Text(
                                        text = "$secondsRemainingInPhase",
                                        style = MaterialTheme.typography.displayMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 42.sp
                                        ),
                                        color = Color.White
                                    )
                                    Text(
                                        text = "sec",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 11.sp
                                        ),
                                        color = Color.White.copy(alpha = 0.85f)
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Spa,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(38.dp)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Breathe",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Medium,
                                            fontSize = 14.sp
                                        ),
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Phase prompt description
                    Text(
                        text = if (isRunning) currentPhase.instruction else "Find a comfortable posture and press Begin to start",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 13.sp,
                            lineHeight = 19.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        textAlign = TextAlign.Center,
                        color = PastelTextPrimary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Cycle Counter
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = PastelSage,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Cycles completed: $totalCyclesCompleted",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp
                            ),
                            color = PastelTextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Controls (Play/Pause & Reset)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isRunning) {
                            OutlinedButton(
                                onClick = {
                                    MindfulHapticHelper.triggerSubtleClick(context)
                                    isRunning = false
                                },
                                shape = RoundedCornerShape(24.dp),
                                border = BorderStroke(1.dp, PastelPeach),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = PastelPeach
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("pause_breathing_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Pause,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Pause", fontWeight = FontWeight.SemiBold)
                            }
                        } else {
                            Button(
                                onClick = {
                                    MindfulHapticHelper.triggerCompletionSuccess(context)
                                    isRunning = true
                                },
                                shape = RoundedCornerShape(24.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = PastelPeach,
                                    contentColor = Color.White
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("start_breathing_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Begin Breathing", fontWeight = FontWeight.SemiBold)
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        IconButton(
                            onClick = {
                                MindfulHapticHelper.triggerSubtleClick(context)
                                isRunning = false
                                totalCyclesCompleted = 0
                                secondsRemainingInPhase = selectedTechnique.inhaleSeconds
                                currentPhase = BreathingPhase.INHALE
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(PastelSurfaceVariant)
                                .testTag("reset_breathing_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Reset Exercise",
                                tint = PastelTextPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }

        // Quick Stress De-escalation & Grounding Guide (Collapsible)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .clickable {
                        MindfulHapticHelper.triggerSubtleClick(context)
                        showGroundingGuide = !showGroundingGuide
                    }
                    .testTag("grounding_guide_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = PastelSurface),
                border = BorderStroke(1.dp, Color(0xFFEEE6DF)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(PastelSageLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Spa,
                                    contentDescription = null,
                                    tint = PastelSage,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Body De-escalation Checklist",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 15.sp
                                    ),
                                    color = PastelTextPrimary
                                )
                                Text(
                                    text = "3 simple physical resets for intense stress",
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                                    color = PastelTextSecondary
                                )
                            }
                        }

                        Icon(
                            imageVector = if (showGroundingGuide) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = if (showGroundingGuide) "Collapse" else "Expand",
                            tint = PastelPeach
                        )
                    }

                    AnimatedVisibility(
                        visible = showGroundingGuide,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            GroundingItem(
                                number = "1",
                                title = "Drop Your Shoulders",
                                description = "Inhale up to your ears, then let them fall completely on the exhale. Release the heavy weight you've been carrying."
                            )
                            GroundingItem(
                                number = "2",
                                title = "Unclench Your Jaw",
                                description = "Part your teeth slightly and rest your tongue gently behind your front teeth. Smooth out the tension around your eyes."
                            )
                            GroundingItem(
                                number = "3",
                                title = "Ground Your Feet",
                                description = "Press both soles flat into the floor. Feel the solid earth supporting you right now—safe, steady, and secure."
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun GroundingItem(
    number: String,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(26.dp)
                .clip(CircleShape)
                .background(PastelPeachLight),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = PastelPeach
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = PastelTextPrimary
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 17.sp),
                color = PastelTextSecondary
            )
        }
    }
}

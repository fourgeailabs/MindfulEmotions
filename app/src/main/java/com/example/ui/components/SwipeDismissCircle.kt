package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PastelPeach
import com.example.ui.theme.PastelPeachLight
import com.example.ui.theme.PastelSage
import com.example.ui.theme.PastelTextSecondary
import com.example.util.MindfulHapticHelper
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.math.sqrt

@Composable
fun SwipeDismissCircle(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current
    val density = LocalDensity.current

    val offset = remember { Animatable(Offset.Zero, Offset.VectorConverter) }
    var isDismissing by remember { mutableStateOf(false) }
    var passedThreshold by remember { mutableStateOf(false) }

    // Gentle breathing pulse animation for the halo
    val infiniteTransition = rememberInfiniteTransition(label = "halo_pulse")
    val haloPulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "halo_scale"
    )
    val haloAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "halo_alpha"
    )

    val dismissThresholdPx = with(density) { 95.dp.toPx() }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(170.dp),
            contentAlignment = Alignment.Center
        ) {
            // Outer breathing glowing halo
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .scale(haloPulse)
                    .alpha(haloAlpha)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(PastelPeach.copy(alpha = 0.5f), Color.Transparent)
                        )
                    )
            )

            // Outer guide border circle
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .border(
                        width = 1.5.dp,
                        color = PastelPeach.copy(alpha = 0.25f),
                        shape = CircleShape
                    )
            )

            // Draggable center circle
            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            offset.value.x.roundToInt(),
                            offset.value.y.roundToInt()
                        )
                    }
                    .size(92.dp)
                    .scale(if (isDismissing) 0.8f else 1f)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                PastelPeachLight,
                                Color(0xFFFFD9CC),
                                PastelSage.copy(alpha = 0.2f)
                            )
                        )
                    )
                    .border(
                        width = 2.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(PastelPeach, PastelSage)
                        ),
                        shape = CircleShape
                    )
                    .testTag("swipe_dismiss_circle")
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragEnd = {
                                val distance = sqrt(
                                    offset.value.x * offset.value.x + offset.value.y * offset.value.y
                                )
                                if (distance > dismissThresholdPx && !isDismissing) {
                                    isDismissing = true
                                    MindfulHapticHelper.triggerDismissSuccess(context)
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    coroutineScope.launch {
                                        // Animate out towards drag direction
                                        val factor = 2.5f
                                        offset.animateTo(
                                            Offset(offset.value.x * factor, offset.value.y * factor),
                                            animationSpec = tween(180)
                                        )
                                        onDismiss()
                                    }
                                } else {
                                    passedThreshold = false
                                    // Spring back to center
                                    coroutineScope.launch {
                                        offset.animateTo(
                                            Offset.Zero,
                                            animationSpec = spring(dampingRatio = 0.6f, stiffness = 400f)
                                        )
                                    }
                                }
                            },
                            onDragCancel = {
                                passedThreshold = false
                                coroutineScope.launch {
                                    offset.animateTo(Offset.Zero)
                                }
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                if (!isDismissing) {
                                    val nextX = offset.value.x + dragAmount.x
                                    val nextY = offset.value.y + dragAmount.y
                                    val dist = sqrt(nextX * nextX + nextY * nextY)
                                    if (dist >= dismissThresholdPx && !passedThreshold) {
                                        passedThreshold = true
                                        MindfulHapticHelper.triggerThresholdTick(context)
                                    } else if (dist < dismissThresholdPx && passedThreshold) {
                                        passedThreshold = false
                                    }
                                    coroutineScope.launch {
                                        offset.snapTo(Offset(nextX, nextY))
                                    }
                                }
                            }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Swipe away circle",
                        tint = PastelPeach,
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        text = "SWIPE",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.2.sp
                        ),
                        color = PastelPeach
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Swipe circle away to dismiss",
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 12.sp,
                letterSpacing = 0.3.sp
            ),
            color = PastelTextSecondary
        )
    }
}

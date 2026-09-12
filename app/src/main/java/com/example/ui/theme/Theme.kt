package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val MindfulLightColorScheme =
  lightColorScheme(
    primary = PastelPeach,
    onPrimary = PastelSurface,
    primaryContainer = PastelPeachLight,
    onPrimaryContainer = PastelPeachDark,
    secondary = PastelSage,
    onSecondary = PastelSurface,
    secondaryContainer = PastelSageLight,
    onSecondaryContainer = PastelSageDark,
    tertiary = PastelLavender,
    onTertiary = PastelSurface,
    tertiaryContainer = PastelLavenderLight,
    onTertiaryContainer = PastelLavenderDark,
    background = PastelCream,
    onBackground = PastelTextPrimary,
    surface = PastelSurface,
    onSurface = PastelTextPrimary,
    surfaceVariant = PastelSurfaceVariant,
    onSurfaceVariant = PastelTextSecondary,
    outline = PastelOutline,
    outlineVariant = PastelSurfaceVariant
  )

@Composable
fun MyApplicationTheme(
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = MindfulLightColorScheme,
    typography = Typography,
    content = content
  )
}


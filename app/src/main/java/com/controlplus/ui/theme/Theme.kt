package com.controlplus.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val BgPrimary = Color(0xFF060B16)
val CardBg = Color(0xFF0D1424)
val NeonGreen = Color(0xFF5CFF9D)
val ElectricBlue = Color(0xFF41B9FF)
val PurpleGoal = Color(0xFFA16CFF)
val AlertRed = Color(0xFFFF4D5F)
val WarningYellow = Color(0xFFFFD058)

private val ControlScheme = darkColorScheme(
    primary = NeonGreen,
    secondary = ElectricBlue,
    tertiary = PurpleGoal,
    background = BgPrimary,
    surface = CardBg,
    error = AlertRed,
    onPrimary = Color.Black,
    onBackground = Color(0xFFEAF0FF),
    onSurface = Color(0xFFEAF0FF)
)

@Composable
fun ControlPlusTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ControlScheme,
        content = content
    )
}

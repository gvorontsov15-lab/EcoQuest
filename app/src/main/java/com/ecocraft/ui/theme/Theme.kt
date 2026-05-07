package com.ecocraft.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ─── Palette ────────────────────────────────────────────────────────────────

val GreenDark      = Color(0xFF1B5E20)
val GreenMedium    = Color(0xFF2E7D32)
val GreenAccent    = Color(0xFF43A047)
val GreenLight     = Color(0xFF66BB6A)
val GreenPastel    = Color(0xFFC8E6C9)
val GreenSurface   = Color(0xFFE8F5E9)
val GreenBackground= Color(0xFFF0F7F0)
val GreenMint      = Color(0xFFF1F8E9)
val White          = Color(0xFFFFFFFF)
val TextPrimary    = Color(0xFF1B5E20)
val TextSecondary  = Color(0xFF558B2F)
val TextMuted      = Color(0xFF81C784)

private val EcoColorScheme = lightColorScheme(
    primary         = GreenMedium,
    onPrimary       = White,
    primaryContainer= GreenSurface,
    secondary       = GreenAccent,
    onSecondary     = White,
    background      = GreenBackground,
    surface         = White,
    onBackground    = TextPrimary,
    onSurface       = TextPrimary
)

@Composable
fun EcoQuestTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EcoColorScheme,
        content = content
    )
}

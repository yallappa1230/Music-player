package com.example.glassmusic.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Apple-inspired Liquid Glass Color Palette
val GlassDarkBg = Color(0xFF090C15)
val GlassDarkCard = Color(0x221A2234)
val GlassHighlight = Color(0x33FFFFFF)
val GlassBorder = Color(0x40FFFFFF)
val GlassBorderSubtle = Color(0x1FFFFFFF)

val GlassCyan = Color(0xFF38BDF8)
val GlassViolet = Color(0xFFA855F7)
val GlassRose = Color(0xFFFB7185)
val GlassAmber = Color(0xFFFBBF24)
val GlassEmerald = Color(0xFF34D399)

val GlassDarkTextPrimary = Color(0xFFF8FAFC)
val GlassDarkTextSecondary = Color(0xFF94A3B8)
val GlassDarkTextMuted = Color(0xFF64748B)

private val DarkColorScheme = darkColorScheme(
    primary = GlassCyan,
    onPrimary = Color.Black,
    primaryContainer = Color(0x3338BDF8),
    onPrimaryContainer = Color(0xFFBAE6FD),
    secondary = GlassViolet,
    onSecondary = Color.Black,
    background = GlassDarkBg,
    onBackground = GlassDarkTextPrimary,
    surface = Color(0x181E293B),
    onSurface = GlassDarkTextPrimary,
    surfaceVariant = Color(0x26334155),
    onSurfaceVariant = GlassDarkTextSecondary,
    outline = GlassBorder
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF0284C7),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0F2FE),
    secondary = Color(0xFF7C3AED),
    background = Color(0xFFF1F5F9),
    onBackground = Color(0xFF0F172A),
    surface = Color(0xCCFFFFFF),
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0x99E2E8F0),
    onSurfaceVariant = Color(0xFF475569),
    outline = Color(0x4D000000)
)

/**
 * Liquid Glass styling modifier applying frosted translucency and specular gradient borders.
 */
fun Modifier.glassPanel(
    cornerRadius: Dp = 20.dp,
    bgAlpha: Float = 0.14f,
    borderAlpha: Float = 0.28f,
    surfaceColor: Color = Color.White
): Modifier = this
    .clip(RoundedCornerShape(cornerRadius))
    .background(
        Brush.linearGradient(
            colors = listOf(
                surfaceColor.copy(alpha = bgAlpha * 1.3f),
                surfaceColor.copy(alpha = bgAlpha * 0.7f)
            ),
            start = Offset(0f, 0f),
            end = Offset(400f, 400f)
        )
    )
    .border(
        width = 1.dp,
        brush = Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = borderAlpha),
                Color.White.copy(alpha = borderAlpha * 0.2f),
                Color.White.copy(alpha = borderAlpha * 0.6f)
            ),
            start = Offset(0f, 0f),
            end = Offset(300f, 600f)
        ),
        shape = RoundedCornerShape(cornerRadius)
    )

/**
 * Creates dynamic ambient liquid mesh background refractions.
 */
fun Modifier.liquidAmbientGlow(
    primaryColor: Color = GlassCyan,
    secondaryColor: Color = GlassViolet
): Modifier = this.drawBehind {
    // Top-left refraction orb
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                primaryColor.copy(alpha = 0.28f),
                primaryColor.copy(alpha = 0.08f),
                Color.Transparent
            ),
            center = Offset(size.width * 0.15f, size.height * 0.15f),
            radius = size.width * 0.75f
        )
    )

    // Mid-right chromatic glow orb
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                secondaryColor.copy(alpha = 0.22f),
                secondaryColor.copy(alpha = 0.05f),
                Color.Transparent
            ),
            center = Offset(size.width * 0.85f, size.height * 0.45f),
            radius = size.width * 0.85f
        )
    )

    // Bottom-center ambient pool
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                GlassRose.copy(alpha = 0.15f),
                Color.Transparent
            ),
            center = Offset(size.width * 0.5f, size.height * 0.88f),
            radius = size.width * 0.9f
        )
    )
}

@Composable
fun GlassMusicTheme(
    darkTheme: Boolean = true, // Default to stunning Apple dark liquid glass
    glassStyle: GlassStyle = GlassStyle(),
    content: @Composable () -> Unit
) {
    GlassmorphicTheme(
        darkTheme = darkTheme,
        glassStyle = glassStyle,
        content = content
    )
}

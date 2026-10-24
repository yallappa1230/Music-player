package com.example.glassmusic.ui.theme

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Configuration parameters for the Glassmorphism visual engine.
 */
@Immutable
data class GlassStyle(
    val blurRadius: Dp = 20.dp,
    val surfaceAlpha: Float = 0.16f,
    val borderAlpha: Float = 0.32f,
    val specularAlpha: Float = 0.50f,
    val primaryAccent: Color = GlassCyan,
    val secondaryAccent: Color = GlassViolet,
    val isGlassBlurSupported: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
)

val LocalGlassStyle = staticCompositionLocalOf { GlassStyle() }

// Specialized Semi-Transparent M3 Color Schemes for Glassmorphism
private val GlassDarkColorScheme = darkColorScheme(
    primary = GlassCyan,
    onPrimary = Color.Black,
    primaryContainer = Color(0x3338BDF8),
    onPrimaryContainer = Color(0xFFBAE6FD),
    secondary = GlassViolet,
    onSecondary = Color.Black,
    background = GlassDarkBg,
    onBackground = GlassDarkTextPrimary,
    // Semi-transparent surface layers for glassmorphic depth
    surface = Color(0x1F1E293B),               // ~12% opacity frosted surface
    onSurface = GlassDarkTextPrimary,
    surfaceVariant = Color(0x2E334155),        // ~18% opacity card surface
    onSurfaceVariant = GlassDarkTextSecondary,
    surfaceContainer = Color(0x240F172A),      // ~14% opacity container
    surfaceContainerHigh = Color(0x381E293B),  // ~22% opacity high elevation
    surfaceContainerHighest = Color(0x48334155), // ~28% opacity highest elevation
    outline = GlassBorder,                     // Specular gradient border
    outlineVariant = GlassBorderSubtle
)

private val GlassLightColorScheme = lightColorScheme(
    primary = Color(0xFF0284C7),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0F2FE),
    secondary = Color(0xFF7C3AED),
    background = Color(0xFFF8FAFC),
    onBackground = Color(0xFF0F172A),
    surface = Color(0x80FFFFFF),               // 50% opacity translucent white
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0x66E2E8F0),        // 40% opacity card surface
    onSurfaceVariant = Color(0xFF475569),
    surfaceContainer = Color(0x50F1F5F9),
    surfaceContainerHigh = Color(0x75FFFFFF),
    surfaceContainerHighest = Color(0x90FFFFFF),
    outline = Color(0x40000000),
    outlineVariant = Color(0x1F000000)
)

/**
 * Custom Material3 Theme Wrapper that provides glassmorphism color schemes,
 * local glass styling, and blurred background modifiers for the main UI components.
 */
@Composable
fun GlassmorphicTheme(
    darkTheme: Boolean = true,
    glassStyle: GlassStyle = GlassStyle(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) GlassDarkColorScheme else GlassLightColorScheme

    CompositionLocalProvider(LocalGlassStyle provides glassStyle) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = MaterialTheme.typography,
            shapes = MaterialTheme.shapes,
            content = content
        )
    }
}

/**
 * Applies a real hardware blur modifier on Android 12+ (SDK 31+) and graceful fallback.
 */
fun Modifier.glassmorphicBlur(
    blurRadius: Dp = 20.dp
): Modifier {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        this.blur(radius = blurRadius)
    } else {
        this // Graceful fallback on pre-API 31 without crashing
    }
}

/**
 * Full glassmorphism modifier that combines semi-transparent surface gradient,
 * specular edge highlights, and rounded corner shape.
 */
fun Modifier.glassmorphicSurface(
    shape: Shape = RoundedCornerShape(20.dp),
    surfaceColor: Color = Color.White,
    bgAlpha: Float = 0.16f,
    borderAlpha: Float = 0.32f,
    borderWidth: Dp = 1.dp
): Modifier = this
    .clip(shape)
    .background(
        Brush.linearGradient(
            colors = listOf(
                surfaceColor.copy(alpha = bgAlpha * 1.35f),
                surfaceColor.copy(alpha = bgAlpha * 0.75f)
            ),
            start = Offset(0f, 0f),
            end = Offset(450f, 450f)
        )
    )
    .border(
        width = borderWidth,
        brush = Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = borderAlpha),
                Color.White.copy(alpha = borderAlpha * 0.25f),
                Color.White.copy(alpha = borderAlpha * 0.65f)
            ),
            start = Offset(0f, 0f),
            end = Offset(300f, 600f)
        ),
        shape = shape
    )

/**
 * Renders chromatic blurred background ambient orbs behind any component.
 */
fun Modifier.glassmorphicBackground(
    primaryColor: Color = GlassCyan,
    secondaryColor: Color = GlassViolet,
    tertiaryColor: Color = GlassRose
): Modifier = this.drawBehind {
    // Primary chromatic orb (top-left)
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                primaryColor.copy(alpha = 0.30f),
                primaryColor.copy(alpha = 0.08f),
                Color.Transparent
            ),
            center = Offset(size.width * 0.18f, size.height * 0.12f),
            radius = size.width * 0.80f
        )
    )

    // Secondary chromatic orb (mid-right)
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                secondaryColor.copy(alpha = 0.24f),
                secondaryColor.copy(alpha = 0.06f),
                Color.Transparent
            ),
            center = Offset(size.width * 0.88f, size.height * 0.45f),
            radius = size.width * 0.85f
        )
    )

    // Tertiary chromatic orb (bottom-center)
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                tertiaryColor.copy(alpha = 0.18f),
                Color.Transparent
            ),
            center = Offset(size.width * 0.50f, size.height * 0.85f),
            radius = size.width * 0.90f
        )
    )
}

/**
 * Drop-in Glassmorphic Card component for Material 3 layouts.
 */
@Composable
fun GlassmorphicCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(20.dp),
    surfaceColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    bgAlpha: Float = 0.18f,
    borderAlpha: Float = 0.32f,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val baseModifier = modifier.glassmorphicSurface(
        shape = shape,
        surfaceColor = surfaceColor,
        bgAlpha = bgAlpha,
        borderAlpha = borderAlpha
    )

    val clickableModifier = if (onClick != null) {
        baseModifier.clickable(onClick = onClick)
    } else {
        baseModifier
    }

    Box(
        modifier = clickableModifier,
        content = content
    )
}

/**
 * Drop-in Glassmorphic Surface for containers, floating sheets, and modals.
 */
@Composable
fun GlassmorphicSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(24.dp),
    surfaceColor: Color = MaterialTheme.colorScheme.surface,
    bgAlpha: Float = 0.20f,
    borderAlpha: Float = 0.36f,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier.glassmorphicSurface(
            shape = shape,
            surfaceColor = surfaceColor,
            bgAlpha = bgAlpha,
            borderAlpha = borderAlpha
        ),
        content = content
    )
}

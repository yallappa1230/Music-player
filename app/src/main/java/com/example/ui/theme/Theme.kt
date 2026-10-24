package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.example.glassmusic.ui.theme.GlassStyle
import com.example.glassmusic.ui.theme.GlassmorphicTheme

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    glassStyle: GlassStyle = GlassStyle(),
    content: @Composable () -> Unit
) {
    GlassmorphicTheme(
        darkTheme = darkTheme,
        glassStyle = glassStyle,
        content = content
    )
}

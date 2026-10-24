package com.example.glassmusic.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.MaterialTheme
import com.example.glassmusic.ui.theme.GlassCyan
import com.example.glassmusic.ui.theme.GlassDarkTextMuted
import com.example.glassmusic.ui.theme.GlassDarkTextPrimary
import com.example.glassmusic.ui.theme.glassmorphicBlur
import com.example.glassmusic.ui.theme.glassmorphicSurface
import com.example.glassmusic.ui.theme.glassPanel
import com.example.glassmusic.viewmodel.NavScreen

data class NavItem(
    val screen: NavScreen,
    val title: String,
    val icon: ImageVector,
    val tag: String
)

@Composable
fun GlassNavigationBar(
    currentScreen: NavScreen,
    onScreenSelected: (NavScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavItem(NavScreen.HOME, "Home", Icons.Filled.Home, "nav_home"),
        NavItem(NavScreen.BROWSE, "Browse", Icons.Filled.Explore, "nav_browse"),
        NavItem(NavScreen.SEARCH, "Search", Icons.Filled.Search, "nav_search"),
        NavItem(NavScreen.LIBRARY, "Library", Icons.Filled.CollectionsBookmark, "nav_library"),
        NavItem(NavScreen.SETTINGS, "Setup", Icons.Filled.Tune, "nav_settings")
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 14.dp, vertical = 6.dp)
            .glassmorphicBlur(blurRadius = 18.dp)
            .glassmorphicSurface(
                shape = RoundedCornerShape(28.dp),
                surfaceColor = MaterialTheme.colorScheme.surfaceVariant,
                bgAlpha = 0.24f,
                borderAlpha = 0.32f
            )
            .testTag("glass_navigation_bar")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp, horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = currentScreen == item.screen
                val tintColor = animateColorAsState(
                    targetValue = if (isSelected) GlassCyan else GlassDarkTextMuted,
                    animationSpec = tween(durationMillis = 200),
                    label = "NavIconTint"
                )

                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onScreenSelected(item.screen) }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .testTag(item.tag),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) Color(0x3338BDF8) else Color.Transparent)
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = tintColor.value,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = item.title,
                        color = if (isSelected) GlassDarkTextPrimary else GlassDarkTextMuted,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                    )
                }
            }
        }
    }
}

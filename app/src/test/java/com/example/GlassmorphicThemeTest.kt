package com.example

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import com.example.glassmusic.ui.screens.SearchCategoryFilter
import com.example.glassmusic.ui.theme.GlassStyle
import com.example.glassmusic.ui.theme.GlassmorphicTheme
import com.example.glassmusic.ui.theme.LocalGlassStyle
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class GlassmorphicThemeTest {

    @Test
    fun testGlassStyleDefaultValues() {
        val style = GlassStyle()
        assertEquals(0.16f, style.surfaceAlpha, 0.001f)
        assertEquals(0.32f, style.borderAlpha, 0.001f)
        assertEquals(0.50f, style.specularAlpha, 0.001f)
    }

    @Test
    fun testSemiTransparentSurfaceColors() {
        val customStyle = GlassStyle(surfaceAlpha = 0.20f)
        assertTrue("Surface alpha should be semi-transparent", customStyle.surfaceAlpha < 1.0f)
    }

    @Test
    fun testSearchCategoryFilters() {
        val filters = SearchCategoryFilter.values()
        assertEquals(4, filters.size)
        assertTrue(filters.contains(SearchCategoryFilter.ALL))
        assertTrue(filters.contains(SearchCategoryFilter.SONGS))
        assertTrue(filters.contains(SearchCategoryFilter.ALBUMS))
        assertTrue(filters.contains(SearchCategoryFilter.SPATIAL))
    }
}

package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.glassmusic.api.AppleMusicAuthStatus
import com.example.glassmusic.api.AppleMusicKitBridge
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class AppleMusicKitBridgeTest {

    @Test
    fun testDeveloperTokenConfiguration() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val bridge = AppleMusicKitBridge(context)

        // Initial state
        bridge.setDeveloperToken("")
        val initialDiag = bridge.getDiagnostics()
        assertEquals(AppleMusicAuthStatus.NO_DEVELOPER_TOKEN, initialDiag.status)
        assertFalse(initialDiag.hasDeveloperToken)

        // After setting token
        val dummyToken = "eyJhbGciOiJFUzI1NiIsImtpZCI6IlRFU1QifQ.payload.sig"
        bridge.setDeveloperToken(dummyToken)
        assertEquals(dummyToken, bridge.getDeveloperToken())

        val configuredDiag = bridge.getDiagnostics()
        assertTrue(configuredDiag.hasDeveloperToken)
    }

    @Test
    fun testStorefrontPersistence() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val bridge = AppleMusicKitBridge(context)

        bridge.setStorefront("jp")
        assertEquals("jp", bridge.getStorefront())

        bridge.setStorefront("us")
        assertEquals("us", bridge.getStorefront())
    }
}

package com.example.glassmusic.api

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

enum class AppleMusicAuthStatus {
    NO_DEVELOPER_TOKEN,
    DEVELOPER_TOKEN_READY,
    SDK_AAR_MISSING,
    SDK_AAR_LINKED,
    USER_AUTHORIZED,
    USER_AUTH_EXPIRED,
    RESTRICTED_REGION
}

data class AppleMusicDiagnostic(
    val hasDeveloperToken: Boolean,
    val isSdkPresent: Boolean,
    val hasUserToken: Boolean,
    val storefront: String,
    val status: AppleMusicAuthStatus,
    val statusDescription: String,
    val nextActionAdvice: String
)

class AppleMusicKitBridge(private val context: Context) {

    private val tag = "AppleMusicKitBridge"
    private val prefs: SharedPreferences =
        context.getSharedPreferences("glass_music_apple_prefs", Context.MODE_PRIVATE)

    companion object {
        const val PREF_DEV_TOKEN = "apple_developer_token"
        const val PREF_USER_TOKEN = "apple_user_token"
        const val PREF_STOREFRONT = "apple_storefront"

        // Official Apple MusicKit class names
        const val CLASS_AUTH_MANAGER = "com.apple.android.sdk.authentication.AuthenticationManager"
        const val CLASS_MEDIA_CONTROLLER = "com.apple.android.music.playback.controller.MediaPlayerController"
    }

    /**
     * Inspects classpath to see if Apple's official proprietary MusicKit AAR
     * has been provided in app/libs.
     */
    fun isAppleMusicKitSdkPresent(): Boolean {
        return try {
            Class.forName(CLASS_AUTH_MANAGER)
            true
        } catch (_: ClassNotFoundException) {
            false
        }
    }

    fun getDeveloperToken(): String {
        val saved = prefs.getString(PREF_DEV_TOKEN, "") ?: ""
        if (saved.isNotBlank()) return saved

        // Fallback to BuildConfig if configured in .env
        return try {
            val buildConfigClass = Class.forName("com.example.BuildConfig")
            val field = buildConfigClass.getField("APPLE_MUSIC_DEVELOPER_TOKEN")
            (field.get(null) as? String) ?: ""
        } catch (_: Exception) {
            ""
        }
    }

    fun setDeveloperToken(token: String) {
        prefs.edit().putString(PREF_DEV_TOKEN, token.trim()).apply()
    }

    fun getUserToken(): String {
        return prefs.getString(PREF_USER_TOKEN, "") ?: ""
    }

    fun setUserToken(token: String) {
        prefs.edit().putString(PREF_USER_TOKEN, token.trim()).apply()
    }

    fun getStorefront(): String {
        return prefs.getString(PREF_STOREFRONT, "us") ?: "us"
    }

    fun setStorefront(storefront: String) {
        prefs.edit().putString(PREF_STOREFRONT, storefront.trim().lowercase()).apply()
    }

    fun getDiagnostics(): AppleMusicDiagnostic {
        val devToken = getDeveloperToken()
        val userToken = getUserToken()
        val sdkPresent = isAppleMusicKitSdkPresent()
        val storefront = getStorefront()

        val (status, desc, advice) = when {
            devToken.isBlank() -> Triple(
                AppleMusicAuthStatus.NO_DEVELOPER_TOKEN,
                "No Apple Music Developer Token provided.",
                "Generate an ES256 JWT using your Apple Developer MusicKit private key (.p8), or explore catalog using the built-in preview engine."
            )
            !sdkPresent && userToken.isBlank() -> Triple(
                AppleMusicAuthStatus.DEVELOPER_TOKEN_READY,
                "Developer Token is configured! Apple Music Catalog & 30-sec previews enabled.",
                "For full subscriber DRM playback, place Apple's musickit-release.aar into app/libs and request user token."
            )
            !sdkPresent && userToken.isNotBlank() -> Triple(
                AppleMusicAuthStatus.SDK_AAR_MISSING,
                "User Token present, but official MusicKit SDK (.aar) is not linked.",
                "Download musickit-release.aar from Apple Developer portal and place it in app/libs/."
            )
            sdkPresent && userToken.isBlank() -> Triple(
                AppleMusicAuthStatus.SDK_AAR_LINKED,
                "Apple MusicKit SDK is linked! Ready for Apple Music user sign-in.",
                "Tap 'Authorize Apple Music' to launch the Apple Music subscriber login flow."
            )
            else -> Triple(
                AppleMusicAuthStatus.USER_AUTHORIZED,
                "Full Apple Music Subscriber access active!",
                "Full-length DRM catalog playback is enabled."
            )
        }

        return AppleMusicDiagnostic(
            hasDeveloperToken = devToken.isNotBlank(),
            isSdkPresent = sdkPresent,
            hasUserToken = userToken.isNotBlank(),
            storefront = storefront,
            status = status,
            statusDescription = desc,
            nextActionAdvice = advice
        )
    }
}

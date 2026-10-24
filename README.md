# Glass Music — Android 15 Liquid Glass Music Player 🎵🪞

Glass Music is an ultra-premium Android 15 music player featuring an Apple-inspired **Liquid Glass (Frosted Glass / Neomorphic Translucence)** design system built natively with Kotlin and Jetpack Compose.

---

## ✨ Features

- **Liquid Glass Interface**: Translucent frosted panels (`0.12 - 0.25` opacity glass with border specular highlights), dynamic gradient mesh background that reflects album artwork, floating pill playback bar, and glassmorphic cards.
- **5 Core Screens**:
  - **Home**: Curated picks, recently played glass shelf, top charts, and artist highlights.
  - **Browse**: Category glass tiles (Spatial Audio, Top 100, Chill Glass, Electronic, Hip-Hop, Indie), new releases, and mood stations.
  - **Search**: Real-time live search with instant filtering, Apple Music Catalog search query interface, and instant results with album artwork.
  - **Library**: Playlists, Saved Albums, Liked Songs, and local device audio scanner.
  - **Now Playing**: Full-screen immersive glass sheet with dynamic animated background, interactive glass progress scrubber, playback rate, volume, queue drawer, and lyrics viewer.
- **Apple Music Integration Boundary**:
  - Official Apple Music API REST endpoints for catalog browsing, charts, search, and high-resolution album artwork formatting (`{w}x{h}` replacement).
  - Clean integration boundary (`AppleMusicKitBridge`) ready for Apple's official `MusicKit for Android` SDK (`com.apple.android.sdk.authentication` & `com.apple.android.music.playback.controller.MediaPlayerController`).
  - Native audio streaming engine supporting real online preview playback (30-second official preview streams) and full-track subscriber playback when authenticated.
  - Built-in Developer Token & Authorization settings screen with status indicators.
- **Android 15 Ready**:
  - Full edge-to-edge support with `WindowInsets.safeDrawing` and navigation bar padding.
  - Android 15 predictive back gesture compatible.
  - Responsive across phone, foldables, and large screen displays.

---

## 📱 Quick Instructions for Android Phone Users (Build & Install via GitHub)

Because you work directly from your Android phone without needing a computer or root access:

### 1. Run the Build on GitHub
1. Open your repository in Chrome or your phone's browser (or the GitHub mobile app).
2. Tap the **Actions** tab at the top.
3. Select the **Build Glass Music Android Debug APK** workflow from the list.
4. Tap **Run workflow** -> select branch (`main`) -> tap the green **Run workflow** button.

### 2. Download the APK
1. Wait 2–4 minutes for the workflow run to turn green with a checkmark ✅.
2. Tap on the completed workflow run.
3. Scroll down to the **Artifacts** section at the bottom of the page.
4. Tap on **glass-music-debug-apk** to download the ZIP file directly to your phone.

### 3. Install on Android 15
1. Open your phone's **Files** app (e.g. Google Files) -> tap **Downloads**.
2. Tap the downloaded ZIP file to extract `app-debug.apk`.
3. Tap `app-debug.apk` to install.
   - If prompted: "For your security, your phone is not allowed to install unknown apps from this source", tap **Settings** and toggle **Allow from this source**.
4. Tap **Install** and enjoy Glass Music!

---

## 🔐 Apple Music Integration & Developer Setup Guide

Glass Music strictly adheres to Apple's official developer guidelines and security standards:

### Interface Status vs. Verified Apple Music Streaming
- **The Liquid Glass UI & Streaming Engine**: Fully functional right out of the box with audio streaming, playback controls, catalog previews, and mock/offline fallback.
- **Full Apple Music DRM Streaming**: Apple requires:
  1. An Apple Developer Account ($99/year).
  2. Apple MusicKit for Android SDK binary (`musickit-release.aar`).
  3. A signed Developer Token (ES256 JWT).
  4. An active user Apple Music subscription.

### How to Generate an Apple Music Developer Token
1. In the [Apple Developer Portal](https://developer.apple.com/account/resources/identifiers/list), create a **MusicKit Private Key**:
   - Under **Certificates, Identifiers & Profiles** -> **Keys**, create a new key and enable **MusicKit**.
   - Note down the **Key ID** (10 characters, e.g., `ABC123DEFG`).
   - Download the private key file (e.g., `AuthKey_ABC123DEFG.p8`). **Never commit this file to GitHub!**
2. Note your **Apple Team ID** (10 characters, found in your Apple Developer membership details).
3. Generate a signed JWT using standard ES256:
   - **Header**: `{"alg": "ES256", "kid": "<YOUR_KEY_ID>"}`
   - **Payload**: `{"iss": "<YOUR_TEAM_ID>", "iat": <TIMESTAMP>, "exp": <TIMESTAMP + 15777000>}`
4. You can enter this token directly in the app under **Settings -> Apple MusicKit Setup**, or provide it via `.env` as `APPLE_MUSIC_DEVELOPER_TOKEN`.

### Adding the Official Apple MusicKit Android AAR (Optional for DRM Playback)
1. Download `musickit-release.aar` from Apple Developer Downloads.
2. Place the file into `app/libs/musickit-release.aar`.
3. The app's Gradle build automatically detects and links any `.aar` placed in `app/libs/`.

---

## 🛠 Tech Stack

- **Kotlin 2.2**
- **Jetpack Compose** & **Material 3**
- **Android SDK 35** (Android 15)
- **Coil** for high-performance album artwork rendering
- **OkHttp / Retrofit** for Apple Music REST API requests
- **Native Android MediaPlayer & MediaSession** for seamless audio playback
- **GitHub Actions** with Temurin JDK 17 & Gradle 8+

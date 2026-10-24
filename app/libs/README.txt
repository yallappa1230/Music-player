Apple MusicKit for Android AAR Integration Directory
===================================================

To link Apple's official proprietary MusicKit binary SDK:
1. Log in to your Apple Developer account at https://developer.apple.com/
2. Go to MusicKit downloads and download the latest "MusicKit for Android" package.
3. Extract `musickit-release.aar` (or `musickit.aar`).
4. Place the `.aar` file into this directory: `app/libs/musickit-release.aar`
5. The build system in `app/build.gradle.kts` automatically includes all `*.aar` files in `app/libs/`.

Glass Music automatically detects if the Apple MusicKit SDK classes (`com.apple.android.sdk.authentication.AuthenticationManager`)
are present at runtime via the `AppleMusicKitBridge`. If present, full native subscription playback is enabled;
otherwise, Glass Music operates through the official Apple Music REST API and preview playback engine,
guaranteeing a clean compile and zero crashes while providing complete diagnostic setup guidance.

package com.example.glassmusic.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.glassmusic.api.AppleMusicDiagnostic
import com.example.glassmusic.ui.theme.GlassCyan
import com.example.glassmusic.ui.theme.GlassDarkTextMuted
import com.example.glassmusic.ui.theme.GlassDarkTextPrimary
import com.example.glassmusic.ui.theme.GlassDarkTextSecondary
import com.example.glassmusic.ui.theme.GlassEmerald
import com.example.glassmusic.ui.theme.GlassRose
import com.example.glassmusic.ui.theme.GlassViolet
import com.example.glassmusic.ui.theme.glassPanel

@Composable
fun AppleMusicSettingsScreen(
    diagnostics: AppleMusicDiagnostic,
    onSaveDeveloperToken: (String) -> Unit,
    onSaveUserToken: (String) -> Unit,
    onSaveStorefront: (String) -> Unit,
    onRefreshDiagnostics: () -> Unit,
    modifier: Modifier = Modifier
) {
    var devTokenInput by remember { mutableStateOf("") }
    var userTokenInput by remember { mutableStateOf("") }
    var storefrontInput by remember { mutableStateOf(diagnostics.storefront) }
    var saveMessage by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("apple_music_settings_screen"),
        contentPadding = PaddingValues(bottom = 120.dp, top = 16.dp)
    ) {
        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                Text(
                    text = "Apple MusicKit Setup",
                    color = GlassDarkTextPrimary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Official Integration Boundary & Diagnostics",
                    color = GlassDarkTextMuted,
                    fontSize = 13.sp
                )
            }
        }

        // Live Diagnostic Overview Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
                    .glassPanel(cornerRadius = 24.dp, bgAlpha = 0.20f, borderAlpha = 0.38f)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                (if (diagnostics.hasDeveloperToken) GlassCyan else GlassRose).copy(alpha = 0.15f),
                                GlassViolet.copy(alpha = 0.10f)
                            )
                        )
                    )
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "INTEGRATION STATUS",
                            color = if (diagnostics.hasDeveloperToken) GlassCyan else GlassRose,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x33FFFFFF))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Storefront: ${diagnostics.storefront.uppercase()}",
                                color = GlassDarkTextPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = diagnostics.statusDescription,
                        color = GlassDarkTextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = diagnostics.nextActionAdvice,
                        color = GlassDarkTextSecondary,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Checklist
                    StatusIndicatorRow(
                        title = "Developer Token (JWT ES256)",
                        isActive = diagnostics.hasDeveloperToken,
                        activeLabel = "Configured",
                        inactiveLabel = "Missing"
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    StatusIndicatorRow(
                        title = "Official MusicKit SDK AAR",
                        isActive = diagnostics.isSdkPresent,
                        activeLabel = "Linked in app/libs",
                        inactiveLabel = "Not in app/libs"
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    StatusIndicatorRow(
                        title = "Apple Music User Token",
                        isActive = diagnostics.hasUserToken,
                        activeLabel = "Authorized",
                        inactiveLabel = "Not Authorized"
                    )
                }
            }
        }

        // Developer Token Input Field
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .glassPanel(cornerRadius = 20.dp, bgAlpha = 0.14f, borderAlpha = 0.28f)
                    .padding(18.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Key,
                            contentDescription = null,
                            tint = GlassCyan,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Provide Developer Token (JWT)",
                            color = GlassDarkTextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Paste your ES256 Developer Token here to test live catalog search from your phone. Tokens are stored securely on-device and never committed to GitHub.",
                        color = GlassDarkTextMuted,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = devTokenInput,
                        onValueChange = { devTokenInput = it },
                        placeholder = {
                            Text(
                                text = "eyJhbGciOiJFUzI1NiIsImtpZCI6...",
                                color = GlassDarkTextMuted,
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0x18000000),
                            unfocusedContainerColor = Color(0x18000000),
                            focusedBorderColor = GlassCyan,
                            unfocusedBorderColor = Color(0x40FFFFFF),
                            focusedTextColor = GlassDarkTextPrimary,
                            unfocusedTextColor = GlassDarkTextPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("developer_token_input")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                if (devTokenInput.isNotBlank()) {
                                    onSaveDeveloperToken(devTokenInput)
                                    devTokenInput = ""
                                    saveMessage = "Developer Token saved successfully!"
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GlassCyan,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("save_token_button")
                        ) {
                            Icon(Icons.Filled.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Save Token", fontWeight = FontWeight.SemiBold)
                        }

                        // Storefront input
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Storefront: ", color = GlassDarkTextMuted, fontSize = 12.sp)
                            OutlinedTextField(
                                value = storefrontInput,
                                onValueChange = {
                                    if (it.length <= 2) {
                                        storefrontInput = it
                                        onSaveStorefront(it)
                                    }
                                },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color(0x18000000),
                                    unfocusedContainerColor = Color(0x18000000),
                                    focusedTextColor = GlassDarkTextPrimary,
                                    unfocusedTextColor = GlassDarkTextPrimary
                                ),
                                modifier = Modifier.width(68.dp)
                            )
                        }
                    }

                    if (saveMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = saveMessage ?: "",
                            color = GlassEmerald,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Setup Guide & Official Documentation
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
                    .glassPanel(cornerRadius = 20.dp, bgAlpha = 0.12f, borderAlpha = 0.22f)
                    .padding(18.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = null,
                            tint = GlassViolet,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "How to Generate an Apple Developer Token",
                            color = GlassDarkTextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "1. Sign in to developer.apple.com with an active Apple Developer Program account.\n\n" +
                                "2. Go to Certificates, Identifiers & Profiles -> Keys, add a new key and check 'MusicKit'.\n\n" +
                                "3. Download your private key (.p8 file) and copy the 10-character Key ID.\n\n" +
                                "4. Use standard ES256 to sign a JWT with your Team ID as issuer ('iss') and Key ID as key identifier ('kid').\n\n" +
                                "5. To enable DRM full-track playback, download 'musickit-release.aar' from Apple and place it into the app/libs/ folder of this project.",
                        color = GlassDarkTextSecondary,
                        fontSize = 13.sp,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
fun StatusIndicatorRow(
    title: String,
    isActive: Boolean,
    activeLabel: String,
    inactiveLabel: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(if (isActive) GlassEmerald else GlassRose)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                color = GlassDarkTextPrimary,
                fontSize = 13.sp
            )
        }
        Text(
            text = if (isActive) activeLabel else inactiveLabel,
            color = if (isActive) GlassEmerald else GlassDarkTextMuted,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

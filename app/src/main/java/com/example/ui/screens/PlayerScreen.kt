package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.webkit.RenderProcessGoneDetail
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.example.model.Movie
import com.example.model.StreamingServer
import com.example.model.VideoQuality
import com.example.ui.theme.*
import com.example.viewmodel.CineViewModel
import kotlinx.coroutines.delay

@Composable
fun PlayerScreen(
    movie: Movie,
    viewModel: CineViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val selectedServer by viewModel.selectedServer.collectAsState()
    val selectedQuality by viewModel.selectedQuality.collectAsState()
    val activeStreamUrl by viewModel.activeStreamUrl.collectAsState()

    var isPlaying by remember { mutableStateOf(true) }
    var currentPositionSeconds by remember { mutableFloatStateOf(142f) }
    val totalDurationSeconds = 7200f // 2 hours
    var showControls by remember { mutableStateOf(true) }
    var showWebEngine by remember { mutableStateOf(false) }
    var activeAudioLanguage by remember { mutableStateOf(movie.languages.firstOrNull() ?: "Hindi") }
    var areSubtitlesEnabled by remember { mutableStateOf(true) }

    // Auto-advance playback timer when playing
    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            delay(1000)
            if (currentPositionSeconds < totalDurationSeconds) {
                currentPositionSeconds += 1f
            }
        }
    }

    BackHandler {
        viewModel.navigateBack()
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.Black,
        topBar = {
            // SMART RETURN TRICK: FLOATING TOP BAR WITH "🔙 Back to CineZenix"
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("smart_return_top_bar"),
                color = Color(0xF20B0C10),
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Crucial Smart Return Button: "🔙 Back to CineZenix"
                        Button(
                            onClick = { viewModel.navigateBack() },
                            colors = ButtonDefaults.buttonColors(containerColor = NetflixRed),
                            shape = RoundedCornerShape(20.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("back_to_cinezenix_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back to CineZenix",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Back to CineZenix",
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp
                            )
                        }

                        // Server & Quality indicator badges
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(CineSurfaceHighlight)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = selectedServer.displayName,
                                    color = LanguageGreen,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(NetflixRed)
                                    .padding(horizontal = 7.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = selectedQuality.label,
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }

                            // Toggle Web engine mode / Native player mode
                            IconButton(
                                onClick = {
                                    showWebEngine = !showWebEngine
                                    Toast.makeText(
                                        context,
                                        if (showWebEngine) "Switched to Embedded Webview" else "Switched to Cinematic Player",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = if (showWebEngine) Icons.Default.SmartDisplay else Icons.Default.Language,
                                    contentDescription = "Switch Engine",
                                    tint = if (showWebEngine) NetflixRed else Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            // Launch in external app/browser action
                            IconButton(
                                onClick = {
                                    if (activeStreamUrl.isNotEmpty()) {
                                        try {
                                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(activeStreamUrl))
                                            context.startActivity(intent)
                                        } catch (e: Exception) {
                                            Toast.makeText(context, "Opening stream URL: $activeStreamUrl", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.OpenInNew,
                                    contentDescription = "Open in External App",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    // Movie title & language info
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, start = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Now Streaming: ",
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                        Text(
                            text = movie.title,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = " • Audio: $activeAudioLanguage",
                            color = LanguageGreen,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.Black)
        ) {
            if (showWebEngine) {
                // Crash-proof, safely-configured In-App WebView Wrapper
                SafeWebViewWrapper(
                    streamUrl = activeStreamUrl,
                    server = selectedServer,
                    quality = selectedQuality,
                    movieTitle = movie.title
                )
            } else {
                // High-performance Cinematic Native Video Player Canvas
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { showControls = !showControls }
                ) {
                    // Movie backdrop poster acting as video frame
                    AsyncImage(
                        model = movie.backdropUrl.ifEmpty { movie.posterUrl },
                        contentDescription = movie.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Cinematic dark overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0x99000000),
                                        Color(0x22000000),
                                        Color(0xDD000000)
                                    )
                                )
                            )
                    )

                    // Subtitles Overlay Preview
                    if (areSubtitlesEnabled && isPlaying) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 120.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xBB000000))
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "♪ [Audio playing in $activeAudioLanguage - ${selectedQuality.label}] ♪",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // Interactive Controls Overlay
                    AnimatedVisibility(
                        visible = showControls,
                        enter = fadeIn(),
                        exit = fadeOut(),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            // Center Big Play/Pause Button
                            IconButton(
                                onClick = { isPlaying = !isPlaying },
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .size(72.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xCC0B0C10))
                                    .border(2.dp, NetflixRed, CircleShape)
                                    .testTag("player_play_pause_button")
                            ) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = if (isPlaying) "Pause" else "Play",
                                    tint = Color.White,
                                    modifier = Modifier.size(38.dp)
                                )
                            }

                            // Quick Rewind & Fast Forward
                            Row(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .fillMaxWidth()
                                    .padding(horizontal = 40.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = {
                                        currentPositionSeconds = (currentPositionSeconds - 10f).coerceAtLeast(0f)
                                    },
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(CircleShape)
                                        .background(Color(0x88000000))
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Replay10,
                                        contentDescription = "Rewind 10s",
                                        tint = Color.White
                                    )
                                }

                                Spacer(modifier = Modifier.weight(1f))

                                IconButton(
                                    onClick = {
                                        currentPositionSeconds = (currentPositionSeconds + 10f).coerceAtMost(totalDurationSeconds)
                                    },
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(CircleShape)
                                        .background(Color(0x88000000))
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Forward10,
                                        contentDescription = "Forward 10s",
                                        tint = Color.White
                                    )
                                }
                            }

                            // Top Info status inside player
                            Row(
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color(0x9900E676))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "● LIVE STREAM",
                                        color = CineBackground,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                }

                                Text(
                                    text = "Buffer: 100% • ${selectedQuality.label}",
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }

            // Bottom In-App Control Bar (Controls, Scrubber, Server & Quality switch)
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(12.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xF014171F),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderDark)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    // Scrubber Bar & Timestamps
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val currentMinutes = (currentPositionSeconds / 60).toInt()
                        val currentSecs = (currentPositionSeconds % 60).toInt()
                        val totalMinutes = (totalDurationSeconds / 60).toInt()
                        val totalSecs = (totalDurationSeconds % 60).toInt()

                        Text(
                            text = String.format("%02d:%02d", currentMinutes, currentSecs),
                            color = LanguageGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Slider(
                            value = currentPositionSeconds,
                            onValueChange = { currentPositionSeconds = it },
                            valueRange = 0f..totalDurationSeconds,
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 8.dp),
                            colors = SliderDefaults.colors(
                                thumbColor = NetflixRed,
                                activeTrackColor = NetflixRed,
                                inactiveTrackColor = BorderDark
                            )
                        )

                        Text(
                            text = String.format("%02d:%02d", totalMinutes, totalSecs),
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Audio Track Selector row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "AUDIO TRACK:",
                            color = TextMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            movie.languages.take(3).forEach { lang ->
                                val isLangSelected = activeAudioLanguage == lang
                                Surface(
                                    modifier = Modifier.clickable { activeAudioLanguage = lang },
                                    shape = RoundedCornerShape(4.dp),
                                    color = if (isLangSelected) LanguageGreenBadge else CineSurfaceVariant
                                ) {
                                    Text(
                                        text = lang,
                                        color = if (isLangSelected) CineBackground else TextSecondary,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            // Subtitles toggle button
                            Surface(
                                modifier = Modifier.clickable { areSubtitlesEnabled = !areSubtitlesEnabled },
                                shape = RoundedCornerShape(4.dp),
                                color = if (areSubtitlesEnabled) NetflixRed else CineSurfaceVariant
                            ) {
                                Text(
                                    text = "CC",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Server Toggle Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Switch to Server 1
                        Button(
                            onClick = { viewModel.selectServer(StreamingServer.DISKWALA) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedServer == StreamingServer.DISKWALA) NetflixRed else CineSurface
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(vertical = 6.dp)
                        ) {
                            Text(
                                "Server 1 (Diskwala)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Switch to Server 2
                        Button(
                            onClick = { viewModel.selectServer(StreamingServer.PLAYERJET) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedServer == StreamingServer.PLAYERJET) NetflixRed else CineSurface
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(vertical = 6.dp)
                        ) {
                            Text(
                                "Server 2 (PlayerJet)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Quality Selector Pills
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        VideoQuality.values().forEach { q ->
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { viewModel.selectQuality(q) },
                                shape = RoundedCornerShape(6.dp),
                                color = if (selectedQuality == q) GoldRating else CineSurfaceVariant
                            ) {
                                Text(
                                    text = q.label,
                                    color = if (selectedQuality == q) CineBackground else Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .padding(vertical = 5.dp)
                                        .wrapContentWidth(Alignment.CenterHorizontally)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Quick App Download helper row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Stream engine:",
                            color = TextMuted,
                            fontSize = 10.sp
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(
                                text = "Install Diskwala ↗",
                                color = NetflixRed,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable {
                                    try {
                                        val intent = Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse("https://play.google.com/store/apps/details?id=com.diskwalaapp")
                                        ).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }
                                        context.startActivity(intent)
                                    } catch (e: Exception) {
                                        // Ignore
                                    }
                                }
                            )

                            Text(
                                text = "Install PlayerJet ↗",
                                color = BlueServer,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable {
                                    try {
                                        val intent = Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse("https://play.google.com/store/apps/details?id=com.rs.playerjet")
                                        ).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }
                                        context.startActivity(intent)
                                    } catch (e: Exception) {
                                        // Ignore
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Robust, crash-proof WebView wrapper that disables aggressive hardware rasterization
 * and catches onRenderProcessGone to prevent host app crashes.
 */
@Composable
fun SafeWebViewWrapper(
    streamUrl: String,
    server: StreamingServer,
    quality: VideoQuality,
    movieTitle: String,
    modifier: Modifier = Modifier
) {
    var isRenderDead by remember { mutableStateOf(false) }

    if (isRenderDead) {
        // Graceful fallback if emulator terminates renderer
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.SmartDisplay,
                    contentDescription = null,
                    tint = NetflixRed,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Streaming Engine Recovered",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Text(
                    text = "Endpoint: $streamUrl",
                    color = TextMuted,
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { isRenderDead = false },
                    colors = ButtonDefaults.buttonColors(containerColor = NetflixRed)
                ) {
                    Text("Reload Stream")
                }
            }
        }
        return
    }

    AndroidView(
        modifier = modifier
            .fillMaxSize()
            .testTag("in_app_player_webview"),
        factory = { ctx ->
            WebView(ctx).apply {
                layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                // Use software layer in virtualized environments to avoid GPU renderer crashes
                setLayerType(View.LAYER_TYPE_SOFTWARE, null)

                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    loadWithOverviewMode = true
                    useWideViewPort = true
                    cacheMode = WebSettings.LOAD_NO_CACHE
                }

                webViewClient = object : WebViewClient() {
                    // CRITICAL: Overriding onRenderProcessGone prevents chromium termination crashes!
                    override fun onRenderProcessGone(
                        view: WebView?,
                        detail: RenderProcessGoneDetail?
                    ): Boolean {
                        isRenderDead = true
                        return true // Returning true informs the OS that the app handled the event safely
                    }
                }
                webChromeClient = WebChromeClient()

                val html = """
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <style>
                            body { margin:0; padding:0; background:#000; color:#fff; display:flex; flex-direction:column; justify-content:center; align-items:center; height:100vh; font-family:sans-serif; }
                            .box { border:1px solid #333; border-radius:12px; padding:24px; text-align:center; background:#111; max-width:85%; }
                            .badge { color:#00e676; font-weight:bold; margin-bottom:8px; font-size:12px; }
                            .title { font-size:16px; font-weight:bold; margin-bottom:4px; color:#fff; }
                            .url { font-size:11px; color:#888; word-break:break-all; }
                        </style>
                    </head>
                    <body>
                        <div class="box">
                            <div class="badge">● STREAM CONNECTED [${quality.label}]</div>
                            <div class="title">${movieTitle}</div>
                            <p style="color:#e50914; font-size:12px; font-weight:bold;">${server.displayName}: ${server.providerName}</p>
                            <div class="url">${streamUrl}</div>
                        </div>
                    </body>
                    </html>
                """.trimIndent()
                loadDataWithBaseURL("https://cinezenix.stream", html, "text/html", "UTF-8", null)
            }
        }
    )
}

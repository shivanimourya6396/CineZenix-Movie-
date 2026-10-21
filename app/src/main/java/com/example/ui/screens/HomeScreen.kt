package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterListOff
import androidx.compose.material3.*
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.DownloadForOffline
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Movie
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.CineViewModel
import com.example.viewmodel.Screen

@Composable
fun HomeScreen(
    viewModel: CineViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()
    val filteredMovies by viewModel.filteredMovies.collectAsState()
    val trendingMovies by viewModel.trendingMovies.collectAsState()
    val notifications by viewModel.notifications.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = CineBackground,
        topBar = {
            CineHeader(
                searchQuery = searchQuery,
                onSearchChanged = { viewModel.setSearchQuery(it) },
                notificationCount = notifications.size,
                onOpenNotifications = { viewModel.navigateTo(Screen.NOTIFICATIONS) }
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp),
            contentPadding = PaddingValues(bottom = 32.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .testTag("movies_grid")
        ) {
            // Trending Auto-Slider Banner (Span full width)
            item(span = { GridItemSpan(maxLineSpan) }) {
                TrendingBannerSlider(
                    trendingMovies = trendingMovies,
                    onMovieClick = { viewModel.openMovieDetail(it) },
                    onPlayClick = { viewModel.playMovie(it) }
                )
            }

            // Language Selection System (Horizontal scrollable audio filter)
            item(span = { GridItemSpan(maxLineSpan) }) {
                LanguageFilterBar(
                    selectedLanguage = selectedLanguage,
                    onLanguageSelected = { viewModel.setLanguage(it) },
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Category Horizontal Scroll (South Dubbed, Bollywood, Hollywood, Web Series)
            item(span = { GridItemSpan(maxLineSpan) }) {
                CategoryFilterBar(
                    selectedCategory = selectedCategory,
                    onCategorySelected = { viewModel.setCategory(it) },
                    modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                )
            }

            // Required Players / Download Apps Quick Banner
            item(span = { GridItemSpan(maxLineSpan) }) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .testTag("home_required_players_banner"),
                    shape = RoundedCornerShape(12.dp),
                    color = CineSurfaceVariant,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x33E50914))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.DownloadForOffline,
                                contentDescription = null,
                                tint = NetflixRed,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Required Streaming Players",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Install Diskwala & PlayerJet from Play Store",
                                    color = TextMuted,
                                    fontSize = 10.5.sp
                                )
                            }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            // Diskwala Play Store Button
                            Surface(
                                modifier = Modifier
                                    .clickable {
                                        try {
                                            val intent = Intent(
                                                Intent.ACTION_VIEW,
                                                Uri.parse("https://play.google.com/store/apps/details?id=com.diskwalaapp")
                                            ).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }
                                            context.startActivity(intent)
                                        } catch (e: Exception) {
                                            // Handle gracefully
                                        }
                                    }
                                    .testTag("home_diskwala_btn"),
                                shape = RoundedCornerShape(6.dp),
                                color = NetflixRed
                            ) {
                                Text(
                                    text = "Diskwala",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                                )
                            }

                            // PlayerJet Play Store Button
                            Surface(
                                modifier = Modifier
                                    .clickable {
                                        try {
                                            val intent = Intent(
                                                Intent.ACTION_VIEW,
                                                Uri.parse("https://play.google.com/store/apps/details?id=com.rs.playerjet")
                                            ).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }
                                            context.startActivity(intent)
                                        } catch (e: Exception) {
                                            // Handle gracefully
                                        }
                                    }
                                    .testTag("home_playerjet_btn"),
                                shape = RoundedCornerShape(6.dp),
                                color = BlueServer
                            ) {
                                Text(
                                    text = "PlayerJet",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Section Title & Counter
            item(span = { GridItemSpan(maxLineSpan) }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (selectedCategory == "All") "EXPLORE CATALOG" else selectedCategory.uppercase(),
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 15.sp,
                            letterSpacing = 1.sp
                        )
                        if (selectedLanguage != "All") {
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                shape = MaterialTheme.shapes.extraSmall,
                                color = Color(0x3300E676)
                            ) {
                                Text(
                                    text = selectedLanguage,
                                    color = LanguageGreen,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Text(
                        text = "${filteredMovies.size} titles",
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                }
            }

            // Empty State if no matches
            if (filteredMovies.isEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterListOff,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No movies found",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Try clearing language filters or search terms",
                            color = TextMuted,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                viewModel.setSearchQuery("")
                                viewModel.setLanguage("All")
                                viewModel.setCategory("All")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NetflixRed)
                        ) {
                            Text("Reset All Filters")
                        }
                    }
                }
            } else {
                // Movie Grid Items
                items(filteredMovies, key = { it.id }) { movie ->
                    Box(modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)) {
                        MovieItemCard(
                            movie = movie,
                            onClick = { viewModel.openMovieDetail(movie) },
                            onPlayClick = { viewModel.playMovie(movie) }
                        )
                    }
                }
            }
        }
    }
}

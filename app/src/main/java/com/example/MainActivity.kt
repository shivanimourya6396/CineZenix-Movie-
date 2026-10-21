package com.example

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.MovieRepository
import com.example.ui.screens.*
import com.example.ui.theme.CineBackground
import com.example.ui.theme.CineZenixTheme
import com.example.viewmodel.CineViewModel
import com.example.viewmodel.Screen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        try {
            com.google.firebase.FirebaseApp.initializeApp(applicationContext)
        } catch (e: Exception) {
            android.util.Log.e("Firebase", "FirebaseApp init: ${e.message}")
        }

        val repository = MovieRepository(applicationContext)

        // Handle initial notification intent if launched from deep link
        handleNotificationIntent(intent, repository)

        setContent {
            val cineViewModel = remember { CineViewModel(repository) }

            CineZenixTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(CineBackground)
                ) {
                    CineZenixApp(cineViewModel)
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        val repository = MovieRepository(applicationContext)
        handleNotificationIntent(intent, repository)
    }

    private fun handleNotificationIntent(intent: Intent?, repository: MovieRepository) {
        val deepLinkMovieId = intent?.getStringExtra("movie_id")
        if (!deepLinkMovieId.isNullOrEmpty()) {
            repository.selectDeepLinkMovie(deepLinkMovieId)
        }
    }
}

@Composable
fun CineZenixApp(viewModel: CineViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val selectedMovie by viewModel.selectedMovie.collectAsState()
    val allMovies by viewModel.allMovies.collectAsState()

    // Screen Routing with Backstack handled inside ViewModel and Composables
    when (currentScreen) {
        Screen.HOME -> {
            HomeScreen(viewModel = viewModel)
        }
        Screen.DETAIL -> {
            val movie = selectedMovie ?: allMovies.firstOrNull()
            if (movie != null) {
                MovieDetailScreen(movie = movie, viewModel = viewModel)
            } else {
                HomeScreen(viewModel = viewModel)
            }
        }
        Screen.PLAYER -> {
            val movie = selectedMovie ?: allMovies.firstOrNull()
            if (movie != null) {
                PlayerScreen(movie = movie, viewModel = viewModel)
            } else {
                HomeScreen(viewModel = viewModel)
            }
        }
        Screen.NOTIFICATIONS -> {
            NotificationsScreen(viewModel = viewModel)
        }
    }
}

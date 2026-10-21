package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.MovieRepository
import com.example.model.Movie
import com.example.model.PushNotificationItem
import com.example.model.StreamingServer
import com.example.model.VideoQuality
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class Screen {
    HOME,
    DETAIL,
    PLAYER,
    NOTIFICATIONS
}

class CineViewModel(private val repository: MovieRepository) : ViewModel() {

    val allMovies: StateFlow<List<Movie>> = repository.movies
    val notifications: StateFlow<List<PushNotificationItem>> = repository.notifications

    private val _currentScreen = MutableStateFlow(Screen.HOME)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _screenBackStack = mutableListOf<Screen>()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _selectedLanguage = MutableStateFlow("All")
    val selectedLanguage: StateFlow<String> = _selectedLanguage.asStateFlow()

    private val _selectedMovie = MutableStateFlow<Movie?>(null)
    val selectedMovie: StateFlow<Movie?> = _selectedMovie.asStateFlow()

    private val _selectedServer = MutableStateFlow(StreamingServer.DISKWALA)
    val selectedServer: StateFlow<StreamingServer> = _selectedServer.asStateFlow()

    private val _selectedQuality = MutableStateFlow(VideoQuality.P1080)
    val selectedQuality: StateFlow<VideoQuality> = _selectedQuality.asStateFlow()

    // Filtered movies matching Search, Category, and Language
    val filteredMovies: StateFlow<List<Movie>> = combine(
        allMovies,
        _searchQuery,
        _selectedCategory,
        _selectedLanguage
    ) { movies, query, category, language ->
        movies.filter { movie ->
            val matchesQuery = query.isBlank() ||
                    movie.title.contains(query, ignoreCase = true) ||
                    movie.description.contains(query, ignoreCase = true) ||
                    movie.category.contains(query, ignoreCase = true) ||
                    movie.languages.any { it.contains(query, ignoreCase = true) }

            val matchesCategory = category == "All" || movie.category.equals(category, ignoreCase = true)

            val matchesLanguage = language == "All" || movie.languages.any {
                it.equals(language, ignoreCase = true)
            }

            matchesQuery && matchesCategory && matchesLanguage
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val trendingMovies: StateFlow<List<Movie>> = combine(allMovies) { moviesArray ->
        val list = moviesArray[0].filter { it.isTrending }
        if (list.isNotEmpty()) list else moviesArray[0].take(4)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeStreamUrl: StateFlow<String> = combine(
        _selectedMovie,
        _selectedServer,
        _selectedQuality
    ) { movie, server, quality ->
        movie?.getStreamUrl(server, quality) ?: ""
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    fun navigateTo(screen: Screen) {
        if (_currentScreen.value != screen) {
            _screenBackStack.add(_currentScreen.value)
            _currentScreen.value = screen
        }
    }

    fun navigateBack(): Boolean {
        if (_screenBackStack.isNotEmpty()) {
            val previous = _screenBackStack.removeAt(_screenBackStack.size - 1)
            _currentScreen.value = previous
            return true
        }
        return false
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setCategory(category: String) {
        _selectedCategory.value = category
    }

    fun setLanguage(language: String) {
        _selectedLanguage.value = language
    }

    fun openMovieDetail(movie: Movie) {
        _selectedMovie.value = movie
        navigateTo(Screen.DETAIL)
    }

    fun openMovieDetailById(movieId: String) {
        val found = allMovies.value.firstOrNull { it.id == movieId }
        if (found != null) {
            _selectedMovie.value = found
            navigateTo(Screen.DETAIL)
        }
    }

    fun selectServer(server: StreamingServer) {
        _selectedServer.value = server
    }

    fun selectQuality(quality: VideoQuality) {
        _selectedQuality.value = quality
    }

    fun playMovie(movie: Movie? = null) {
        if (movie != null) {
            _selectedMovie.value = movie
        }
        navigateTo(Screen.PLAYER)
    }

    fun refreshCatalog() {
        repository.startFirestoreSync()
    }

    fun clearNotifications() {
        viewModelScope.launch {
            repository.clearNotifications()
        }
    }
}

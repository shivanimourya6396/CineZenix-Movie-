package com.example.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.model.Movie
import com.example.model.PushNotificationItem
import com.example.model.StreamingLinks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

class MovieRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("cinezenix_prefs", Context.MODE_PRIVATE)

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies.asStateFlow()

    private val _notifications = MutableStateFlow<List<PushNotificationItem>>(emptyList())
    val notifications: StateFlow<List<PushNotificationItem>> = _notifications.asStateFlow()

    private val _pendingDeepLinkMovieId = MutableStateFlow<String?>(null)
    val pendingDeepLinkMovieId: StateFlow<String?> = _pendingDeepLinkMovieId.asStateFlow()

    private var firestoreListener: ListenerRegistration? = null

    init {
        loadCachedMovies()
        loadNotifications()
        startFirestoreSync()
    }

    private fun loadCachedMovies() {
        val jsonString = prefs.getString(KEY_MOVIES, null)
        if (jsonString.isNullOrEmpty()) {
            _movies.value = SampleData.initialMovies
            saveMoviesToPrefs(_movies.value)
        } else {
            try {
                val list = deserializeMovies(jsonString)
                _movies.value = if (list.isNotEmpty()) list else SampleData.initialMovies
            } catch (e: Exception) {
                _movies.value = SampleData.initialMovies
            }
        }
    }

    /**
     * Real-time Firebase Firestore observer.
     * Whenever movies are added, updated, or removed in Firestore,
     * this callback immediately updates the catalog in the user app.
     */
    fun startFirestoreSync() {
        try {
            val firestore = FirebaseFirestore.getInstance()
            firestoreListener?.remove()
            firestoreListener = firestore.collection("movies")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.w("FirestoreSync", "Firestore listen error: ${error.message}")
                        return@addSnapshotListener
                    }
                    if (snapshot != null && !snapshot.isEmpty) {
                        val firestoreList = snapshot.documents.mapNotNull { doc ->
                            try {
                                val data = doc.data ?: return@mapNotNull null
                                val languagesRaw = data["languages"] as? List<*>
                                val languages = languagesRaw?.mapNotNull { it?.toString() }
                                    ?: listOf("Hindi", "English")

                                val linksMap = data["streamingLinks"] as? Map<*, *>
                                val links = StreamingLinks(
                                    diskwala480p = linksMap?.get("diskwala480p")?.toString() ?: "",
                                    diskwala720p = linksMap?.get("diskwala720p")?.toString() ?: "",
                                    diskwala1080p = linksMap?.get("diskwala1080p")?.toString() ?: "",
                                    diskwala4k = linksMap?.get("diskwala4k")?.toString() ?: "",
                                    playerjet480p = linksMap?.get("playerjet480p")?.toString() ?: "",
                                    playerjet720p = linksMap?.get("playerjet720p")?.toString() ?: "",
                                    playerjet1080p = linksMap?.get("playerjet1080p")?.toString() ?: "",
                                    playerjet4k = linksMap?.get("playerjet4k")?.toString() ?: ""
                                )

                                Movie(
                                    id = doc.id,
                                    title = data["title"]?.toString() ?: "Untitled",
                                    category = data["category"]?.toString() ?: "Bollywood",
                                    languages = languages,
                                    rating = (data["rating"] as? Number)?.toDouble() ?: 7.5,
                                    releaseYear = (data["releaseYear"] as? Number)?.toInt() ?: 2024,
                                    duration = data["duration"]?.toString() ?: "2h 15m",
                                    posterUrl = data["posterUrl"]?.toString() ?: "",
                                    backdropUrl = data["backdropUrl"]?.toString() ?: "",
                                    description = data["description"]?.toString() ?: "",
                                    isTrending = data["isTrending"] as? Boolean ?: false,
                                    streamingLinks = links
                                )
                            } catch (e: Exception) {
                                Log.e("FirestoreSync", "Error parsing movie ${doc.id}: ${e.message}")
                                null
                            }
                        }

                        if (firestoreList.isNotEmpty()) {
                            _movies.value = firestoreList
                            saveMoviesToPrefs(firestoreList)
                            Log.d("FirestoreSync", "Loaded ${firestoreList.size} movies from Firebase Firestore")
                        }
                    }
                }
        } catch (e: Exception) {
            Log.e("FirestoreSync", "Firebase Firestore unavailable or not initialized: ${e.message}")
        }
    }

    private fun loadNotifications() {
        val jsonString = prefs.getString(KEY_NOTIFICATIONS, null)
        if (jsonString.isNullOrEmpty()) {
            val welcomeNotification = PushNotificationItem(
                id = UUID.randomUUID().toString(),
                movieId = "movie-2",
                title = "🔥 New Release: Pushpa 2: The Rule",
                message = "Available in Hindi, Telugu, Tamil & Indonesian. Stream in 4K UHD now!",
                posterUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?auto=format&fit=crop&w=600&q=80",
                languages = listOf("Hindi", "Telugu", "Tamil", "Indonesian")
            )
            _notifications.value = listOf(welcomeNotification)
            saveNotificationsToPrefs(_notifications.value)
        } else {
            try {
                _notifications.value = deserializeNotifications(jsonString)
            } catch (e: Exception) {
                _notifications.value = emptyList()
            }
        }
    }

    fun selectDeepLinkMovie(movieId: String?) {
        _pendingDeepLinkMovieId.value = movieId
    }

    fun clearNotifications() {
        _notifications.value = emptyList()
        saveNotificationsToPrefs(emptyList())
    }

    private fun saveMoviesToPrefs(movies: List<Movie>) {
        val jsonArray = JSONArray()
        movies.forEach { movie ->
            val obj = JSONObject().apply {
                put("id", movie.id)
                put("title", movie.title)
                put("category", movie.category)
                put("languages", JSONArray(movie.languages))
                put("rating", movie.rating)
                put("releaseYear", movie.releaseYear)
                put("duration", movie.duration)
                put("posterUrl", movie.posterUrl)
                put("backdropUrl", movie.backdropUrl)
                put("description", movie.description)
                put("isTrending", movie.isTrending)
                val linksObj = JSONObject().apply {
                    put("diskwala480p", movie.streamingLinks.diskwala480p)
                    put("diskwala720p", movie.streamingLinks.diskwala720p)
                    put("diskwala1080p", movie.streamingLinks.diskwala1080p)
                    put("diskwala4k", movie.streamingLinks.diskwala4k)
                    put("playerjet480p", movie.streamingLinks.playerjet480p)
                    put("playerjet720p", movie.streamingLinks.playerjet720p)
                    put("playerjet1080p", movie.streamingLinks.playerjet1080p)
                    put("playerjet4k", movie.streamingLinks.playerjet4k)
                }
                put("streamingLinks", linksObj)
            }
            jsonArray.put(obj)
        }
        prefs.edit().putString(KEY_MOVIES, jsonArray.toString()).apply()
    }

    private fun deserializeMovies(jsonString: String): List<Movie> {
        val jsonArray = JSONArray(jsonString)
        val list = mutableListOf<Movie>()
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            val langArray = obj.getJSONArray("languages")
            val languages = mutableListOf<String>()
            for (j in 0 until langArray.length()) {
                languages.add(langArray.getString(j))
            }
            val linksObj = obj.optJSONObject("streamingLinks")
            val links = if (linksObj != null) {
                StreamingLinks(
                    diskwala480p = linksObj.optString("diskwala480p", ""),
                    diskwala720p = linksObj.optString("diskwala720p", ""),
                    diskwala1080p = linksObj.optString("diskwala1080p", ""),
                    diskwala4k = linksObj.optString("diskwala4k", ""),
                    playerjet480p = linksObj.optString("playerjet480p", ""),
                    playerjet720p = linksObj.optString("playerjet720p", ""),
                    playerjet1080p = linksObj.optString("playerjet1080p", ""),
                    playerjet4k = linksObj.optString("playerjet4k", "")
                )
            } else {
                StreamingLinks()
            }
            list.add(
                Movie(
                    id = obj.getString("id"),
                    title = obj.getString("title"),
                    category = obj.getString("category"),
                    languages = languages,
                    rating = obj.optDouble("rating", 7.5),
                    releaseYear = obj.optInt("releaseYear", 2023),
                    duration = obj.optString("duration", "2h 15m"),
                    posterUrl = obj.optString("posterUrl", ""),
                    backdropUrl = obj.optString("backdropUrl", ""),
                    description = obj.optString("description", ""),
                    isTrending = obj.optBoolean("isTrending", false),
                    streamingLinks = links
                )
            )
        }
        return list
    }

    private fun saveNotificationsToPrefs(items: List<PushNotificationItem>) {
        val jsonArray = JSONArray()
        items.forEach { item ->
            val obj = JSONObject().apply {
                put("id", item.id)
                put("movieId", item.movieId)
                put("title", item.title)
                put("message", item.message)
                put("posterUrl", item.posterUrl)
                put("languages", JSONArray(item.languages))
                put("timestamp", item.timestamp)
            }
            jsonArray.put(obj)
        }
        prefs.edit().putString(KEY_NOTIFICATIONS, jsonArray.toString()).apply()
    }

    private fun deserializeNotifications(jsonString: String): List<PushNotificationItem> {
        val jsonArray = JSONArray(jsonString)
        val list = mutableListOf<PushNotificationItem>()
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            val langArray = obj.optJSONArray("languages")
            val languages = mutableListOf<String>()
            if (langArray != null) {
                for (j in 0 until langArray.length()) {
                    languages.add(langArray.getString(j))
                }
            }
            list.add(
                PushNotificationItem(
                    id = obj.getString("id"),
                    movieId = obj.getString("movieId"),
                    title = obj.getString("title"),
                    message = obj.getString("message"),
                    posterUrl = obj.optString("posterUrl", ""),
                    languages = languages,
                    timestamp = obj.optLong("timestamp", System.currentTimeMillis())
                )
            )
        }
        return list
    }

    companion object {
        private const val KEY_MOVIES = "cinezenix_movies_data"
        private const val KEY_NOTIFICATIONS = "cinezenix_notifications_data"
    }
}

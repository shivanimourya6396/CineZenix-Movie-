package com.example.model

data class StreamingLinks(
    val diskwala480p: String = "",
    val diskwala720p: String = "",
    val diskwala1080p: String = "",
    val diskwala4k: String = "",
    val playerjet480p: String = "",
    val playerjet720p: String = "",
    val playerjet1080p: String = "",
    val playerjet4k: String = ""
)

data class Movie(
    val id: String,
    val title: String,
    val category: String, // South Dubbed, Bollywood, Hollywood, Web Series
    val languages: List<String>, // Hindi, English, Tamil, Telugu, Indonesian, Dual Audio
    val rating: Double,
    val releaseYear: Int,
    val duration: String,
    val posterUrl: String,
    val backdropUrl: String,
    val description: String,
    val isTrending: Boolean = false,
    val streamingLinks: StreamingLinks = StreamingLinks()
) {
    fun getStreamUrl(server: StreamingServer, quality: VideoQuality): String {
        return when (server) {
            StreamingServer.DISKWALA -> when (quality) {
                VideoQuality.P480 -> streamingLinks.diskwala480p
                VideoQuality.P720 -> streamingLinks.diskwala720p
                VideoQuality.P1080 -> streamingLinks.diskwala1080p
                VideoQuality.UHD4K -> streamingLinks.diskwala4k
            }
            StreamingServer.PLAYERJET -> when (quality) {
                VideoQuality.P480 -> streamingLinks.playerjet480p
                VideoQuality.P720 -> streamingLinks.playerjet720p
                VideoQuality.P1080 -> streamingLinks.playerjet1080p
                VideoQuality.UHD4K -> streamingLinks.playerjet4k
            }
        }
    }
}

enum class StreamingServer(val displayName: String, val providerName: String) {
    DISKWALA("Server 1", "Diskwala App"),
    PLAYERJET("Server 2", "PlayerJet / Streaam.net")
}

enum class VideoQuality(val label: String, val defaultSize: String) {
    P480("480p", "450 MB"),
    P720("720p", "1.2 GB"),
    P1080("1080p", "2.4 GB"),
    UHD4K("4K UHD", "6.8 GB")
}

data class PushNotificationItem(
    val id: String,
    val movieId: String,
    val title: String,
    val message: String,
    val posterUrl: String,
    val languages: List<String>,
    val timestamp: Long = System.currentTimeMillis()
)

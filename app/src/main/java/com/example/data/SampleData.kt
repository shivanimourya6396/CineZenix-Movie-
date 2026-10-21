package com.example.data

import com.example.model.Movie
import com.example.model.StreamingLinks

object SampleData {
    val initialMovies: List<Movie> = listOf(
        Movie(
            id = "movie-1",
            title = "KGF: Chapter 2",
            category = "South Dubbed",
            languages = listOf("Hindi", "Kannada", "Tamil", "Telugu", "Dual Audio"),
            rating = 8.3,
            releaseYear = 2022,
            duration = "2h 48m",
            posterUrl = "https://images.unsplash.com/photo-1536440136628-849c177e76a1?auto=format&fit=crop&w=600&q=80",
            backdropUrl = "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?auto=format&fit=crop&w=1200&q=80",
            description = "In the blood-soaked Kolar Gold Fields, Rocky's name strikes fear into his foes. While his allies look up to him, the government sees him as a danger to law and order.",
            isTrending = true,
            streamingLinks = StreamingLinks(
                diskwala480p = "https://www.diskwala.com/app/v/kgf2-480p",
                diskwala720p = "https://www.diskwala.com/app/v/kgf2-720p",
                diskwala1080p = "https://www.diskwala.com/app/v/kgf2-1080p",
                diskwala4k = "https://www.diskwala.com/app/v/kgf2-4k-uhd",
                playerjet480p = "https://streaam.net/T/kgf2_480",
                playerjet720p = "https://streaam.net/T/kgf2_720",
                playerjet1080p = "https://streaam.net/T/kgf2_1080",
                playerjet4k = "https://streaam.net/T/kgf2_4k"
            )
        ),
        Movie(
            id = "movie-2",
            title = "Pushpa 2: The Rule",
            category = "South Dubbed",
            languages = listOf("Hindi", "Telugu", "Tamil", "Indonesian", "Dual Audio"),
            rating = 8.6,
            releaseYear = 2024,
            duration = "3h 05m",
            posterUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?auto=format&fit=crop&w=600&q=80",
            backdropUrl = "https://images.unsplash.com/photo-1517604931442-7e0c8ed2963c?auto=format&fit=crop&w=1200&q=80",
            description = "Pushpa Raj expands his red sandalwood smuggling syndicate while waging an all-out war against the relentless SP Bhanwar Singh Shekhawat.",
            isTrending = true,
            streamingLinks = StreamingLinks(
                diskwala480p = "https://www.diskwala.com/app/v/pushpa2-480p",
                diskwala720p = "https://www.diskwala.com/app/v/pushpa2-720p",
                diskwala1080p = "https://www.diskwala.com/app/v/pushpa2-1080p",
                diskwala4k = "https://www.diskwala.com/app/v/pushpa2-4k-uhd",
                playerjet480p = "https://streaam.net/T/pushpa2_480",
                playerjet720p = "https://streaam.net/T/pushpa2_720",
                playerjet1080p = "https://streaam.net/T/pushpa2_1080",
                playerjet4k = "https://streaam.net/T/pushpa2_4k"
            )
        ),
        Movie(
            id = "movie-3",
            title = "Jawan",
            category = "Bollywood",
            languages = listOf("Hindi", "Tamil", "Telugu", "Dual Audio"),
            rating = 7.9,
            releaseYear = 2023,
            duration = "2h 49m",
            posterUrl = "https://images.unsplash.com/photo-1574375927938-d5a98e8ffe85?auto=format&fit=crop&w=600&q=80",
            backdropUrl = "https://images.unsplash.com/photo-1478720568477-152d9b164e26?auto=format&fit=crop&w=1200&q=80",
            description = "A man is driven by a personal vendetta to rectify the evils in society, while keeping a promise made years ago. He comes up against a monstrous outlaw with no fear.",
            isTrending = true,
            streamingLinks = StreamingLinks(
                diskwala480p = "https://www.diskwala.com/app/v/jawan-480p",
                diskwala720p = "https://www.diskwala.com/app/v/jawan-720p",
                diskwala1080p = "https://www.diskwala.com/app/v/jawan-1080p",
                diskwala4k = "https://www.diskwala.com/app/v/jawan-4k",
                playerjet480p = "https://streaam.net/T/jawan_480",
                playerjet720p = "https://streaam.net/T/jawan_720",
                playerjet1080p = "https://streaam.net/T/jawan_1080",
                playerjet4k = "https://streaam.net/T/jawan_4k"
            )
        ),
        Movie(
            id = "movie-4",
            title = "Dune: Part Two",
            category = "Hollywood",
            languages = listOf("English", "Hindi", "Indonesian", "Dual Audio"),
            rating = 8.5,
            releaseYear = 2024,
            duration = "2h 46m",
            posterUrl = "https://images.unsplash.com/photo-1440404653325-ab127d49abc1?auto=format&fit=crop&w=600&q=80",
            backdropUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?auto=format&fit=crop&w=1200&q=80",
            description = "Paul Atreides unites with Chani and the Fremen while seeking revenge against the conspirators who destroyed his family, facing a choice between love and the universe's fate.",
            isTrending = true,
            streamingLinks = StreamingLinks(
                diskwala480p = "https://www.diskwala.com/app/v/dune2-480p",
                diskwala720p = "https://www.diskwala.com/app/v/dune2-720p",
                diskwala1080p = "https://www.diskwala.com/app/v/dune2-1080p",
                diskwala4k = "https://www.diskwala.com/app/v/dune2-4k-uhd",
                playerjet480p = "https://streaam.net/T/dune2_480",
                playerjet720p = "https://streaam.net/T/dune2_720",
                playerjet1080p = "https://streaam.net/T/dune2_1080",
                playerjet4k = "https://streaam.net/T/dune2_4k"
            )
        ),
        Movie(
            id = "movie-5",
            title = "Mirzapur (Season 3)",
            category = "Web Series",
            languages = listOf("Hindi", "English", "Tamil", "Dual Audio"),
            rating = 8.4,
            releaseYear = 2024,
            duration = "10 Episodes",
            posterUrl = "https://images.unsplash.com/photo-1594909122845-11baa439b7bf?auto=format&fit=crop&w=600&q=80",
            backdropUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?auto=format&fit=crop&w=1200&q=80",
            description = "With Munna gone and Kaleen Bhaiya incapacitated, Guddu Pandit and Golu stake their claim to the throne of Purvanchal amidst rising rivals and bloody betrayals.",
            isTrending = true,
            streamingLinks = StreamingLinks(
                diskwala480p = "https://www.diskwala.com/app/v/mirzapur-s3-480p",
                diskwala720p = "https://www.diskwala.com/app/v/mirzapur-s3-720p",
                diskwala1080p = "https://www.diskwala.com/app/v/mirzapur-s3-1080p",
                diskwala4k = "https://www.diskwala.com/app/v/mirzapur-s3-4k",
                playerjet480p = "https://streaam.net/T/mirzapur_s3_480",
                playerjet720p = "https://streaam.net/T/mirzapur_s3_720",
                playerjet1080p = "https://streaam.net/T/mirzapur_s3_1080",
                playerjet4k = "https://streaam.net/T/mirzapur_s3_4k"
            )
        ),
        Movie(
            id = "movie-6",
            title = "RRR (Rise Roar Revolt)",
            category = "South Dubbed",
            languages = listOf("Hindi", "Telugu", "Tamil", "English", "Indonesian", "Dual Audio"),
            rating = 8.8,
            releaseYear = 2022,
            duration = "3h 07m",
            posterUrl = "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?auto=format&fit=crop&w=600&q=80",
            backdropUrl = "https://images.unsplash.com/photo-1536440136628-849c177e76a1?auto=format&fit=crop&w=1200&q=80",
            description = "A tale of two legendary revolutionaries and their journey away from home before they began fighting for their country in the 1920s.",
            isTrending = false,
            streamingLinks = StreamingLinks(
                diskwala480p = "https://www.diskwala.com/app/v/rrr-480p",
                diskwala720p = "https://www.diskwala.com/app/v/rrr-720p",
                diskwala1080p = "https://www.diskwala.com/app/v/rrr-1080p",
                diskwala4k = "https://www.diskwala.com/app/v/rrr-4k",
                playerjet480p = "https://streaam.net/T/rrr_480",
                playerjet720p = "https://streaam.net/T/rrr_720",
                playerjet1080p = "https://streaam.net/T/rrr_1080",
                playerjet4k = "https://streaam.net/T/rrr_4k"
            )
        ),
        Movie(
            id = "movie-7",
            title = "Animal",
            category = "Bollywood",
            languages = listOf("Hindi", "Telugu", "Tamil", "Dual Audio"),
            rating = 7.1,
            releaseYear = 2023,
            duration = "3h 24m",
            posterUrl = "https://images.unsplash.com/photo-1478720568477-152d9b164e26?auto=format&fit=crop&w=600&q=80",
            backdropUrl = "https://images.unsplash.com/photo-1574375927938-d5a98e8ffe85?auto=format&fit=crop&w=1200&q=80",
            description = "A son's obsessive love for his absent father sparks a dark descent into underworld blood feuds, leading to ruthless vengeance and destruction.",
            isTrending = false,
            streamingLinks = StreamingLinks(
                diskwala480p = "https://www.diskwala.com/app/v/animal-480p",
                diskwala720p = "https://www.diskwala.com/app/v/animal-720p",
                diskwala1080p = "https://www.diskwala.com/app/v/animal-1080p",
                diskwala4k = "https://www.diskwala.com/app/v/animal-4k",
                playerjet480p = "https://streaam.net/T/animal_480",
                playerjet720p = "https://streaam.net/T/animal_720",
                playerjet1080p = "https://streaam.net/T/animal_1080",
                playerjet4k = "https://streaam.net/T/animal_4k"
            )
        ),
        Movie(
            id = "movie-8",
            title = "Deadpool & Wolverine",
            category = "Hollywood",
            languages = listOf("English", "Hindi", "Indonesian", "Dual Audio"),
            rating = 8.1,
            releaseYear = 2024,
            duration = "2h 08m",
            posterUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?auto=format&fit=crop&w=600&q=80",
            backdropUrl = "https://images.unsplash.com/photo-1440404653325-ab127d49abc1?auto=format&fit=crop&w=1200&q=80",
            description = "Wolverine is recovering from his injuries when he crosses paths with the loudmouth Wade Wilson. They team up to defeat a common enemy threatening their multiverse.",
            isTrending = false,
            streamingLinks = StreamingLinks(
                diskwala480p = "https://www.diskwala.com/app/v/dp-wolv-480p",
                diskwala720p = "https://www.diskwala.com/app/v/dp-wolv-720p",
                diskwala1080p = "https://www.diskwala.com/app/v/dp-wolv-1080p",
                diskwala4k = "https://www.diskwala.com/app/v/dp-wolv-4k",
                playerjet480p = "https://streaam.net/T/dp_wolv_480",
                playerjet720p = "https://streaam.net/T/dp_wolv_720",
                playerjet1080p = "https://streaam.net/T/dp_wolv_1080",
                playerjet4k = "https://streaam.net/T/dp_wolv_4k"
            )
        ),
        Movie(
            id = "movie-9",
            title = "Farzi (Season 1)",
            category = "Web Series",
            languages = listOf("Hindi", "English", "Tamil", "Telugu", "Dual Audio"),
            rating = 8.4,
            releaseYear = 2023,
            duration = "8 Episodes",
            posterUrl = "https://images.unsplash.com/photo-1517604931442-7e0c8ed2963c?auto=format&fit=crop&w=600&q=80",
            backdropUrl = "https://images.unsplash.com/photo-1594909122845-11baa439b7bf?auto=format&fit=crop&w=1200&q=80",
            description = "An artist gets pulled into the murky high stakes of a con job when he designs the ultimate counterfeit note. A fiery, unorthodox task force officer wants to rid the nation of counterfeiting.",
            isTrending = false,
            streamingLinks = StreamingLinks(
                diskwala480p = "https://www.diskwala.com/app/v/farzi-480p",
                diskwala720p = "https://www.diskwala.com/app/v/farzi-720p",
                diskwala1080p = "https://www.diskwala.com/app/v/farzi-1080p",
                diskwala4k = "https://www.diskwala.com/app/v/farzi-4k",
                playerjet480p = "https://streaam.net/T/farzi_480",
                playerjet720p = "https://streaam.net/T/farzi_720",
                playerjet1080p = "https://streaam.net/T/farzi_1080",
                playerjet4k = "https://streaam.net/T/farzi_4k"
            )
        )
    )

    val supportedLanguages = listOf(
        "All",
        "Hindi",
        "English",
        "Tamil",
        "Telugu",
        "Indonesian",
        "Dual Audio"
    )

    val categories = listOf(
        "All",
        "South Dubbed",
        "Bollywood",
        "Hollywood",
        "Web Series"
    )
}

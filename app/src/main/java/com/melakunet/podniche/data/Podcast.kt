package com.melakunet.podniche.data

/**
 * Data classes for the iTunes Search API response.
 * Includes the total count and the list of podcast results.
 */
data class PodcastResponse(
    val resultCount: Int,
    val results: List<Podcast>
)

/**
 * Represents a single podcast search result.
 * All fields are nullable to safely handle missing data from the API.
 */
data class Podcast(
    val collectionName: String?, // The name of the podcast
    val artistName: String?,     // The creator or artist of the podcast
    val artworkUrl100: String?,  // URL for the 100x100 artwork image
    val feedUrl: String?,        // URL for the podcast's RSS feed
    val trackId: Long?,          // Unique ID for the podcast entry
    val trackCount: Int?,        // Total number of episodes available
    val primaryGenreName: String?, // Main genre classification
    val releaseDate: String?     // The latest release date string
)

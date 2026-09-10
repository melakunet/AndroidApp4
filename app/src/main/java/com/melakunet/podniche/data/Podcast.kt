package com.melakunet.podniche.data

/**
 * Data classes for the iTunes Search API response.
 */
data class PodcastResponse(
    val resultCount: Int,
    val results: List<Podcast>
)

/**
 * Represents a single podcast search result.
 */
data class Podcast(
    val collectionName: String?,
    val artistName: String?,
    val artworkUrl100: String?,
    val feedUrl: String?,
    val trackId: Long?,
    val trackCount: Int?,
    val primaryGenreName: String?,
    val releaseDate: String?
)

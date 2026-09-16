package com.melakunet.podniche.data

/**
 * Data class representing a single podcast episode.
 */
data class Episode(
    val guid: String?,
    val title: String?,
    val description: String?,
    val mediaUrl: String?,
    val mediaType: String?, // e.g., audio/mpeg, video/mp4
    val pubDate: String?,
    val duration: String?
)

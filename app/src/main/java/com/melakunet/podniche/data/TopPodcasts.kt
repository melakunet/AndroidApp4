package com.melakunet.podniche.data

import com.google.gson.annotations.SerializedName

/**
 * Root response for the Apple Top Podcasts RSS JSON feed.
 * Wraps the main chart feed content.
 */
data class TopPodcastsResponse(
    val feed: ChartFeed?
)

/**
 * Represents the feed content in the RSS JSON.
 * Contains a list of podcast entries.
 */
data class ChartFeed(
    val entry: List<ChartEntry>?
)

/**
 * Represents a single podcast entry in the chart feed.
 * Uses SerializedName for the specific "im:" prefixed fields from Apple's JSON.
 */
data class ChartEntry(
    @SerializedName("im:name") val name: LabelWrapper?,
    @SerializedName("im:artist") val artist: LabelWrapper?,
    @SerializedName("im:image") val images: List<ImageWrapper>?,
    val category: CategoryWrapper?
) {
    /**
     * Maps this [ChartEntry] to the existing [Podcast] data class.
     * Extracts labels and finds the largest image from the list.
     */
    fun toPodcast(): Podcast {
        // We pick the last image in the array as it is usually the largest one available
        val artworkUrl = images?.lastOrNull()?.label
        return Podcast(
            collectionName = name?.label,
            artistName = artist?.label,
            artworkUrl100 = artworkUrl,
            feedUrl = null,
            trackId = null,
            trackCount = null,
            primaryGenreName = category?.attributes?.label,
            releaseDate = null
        )
    }
}

/**
 * Wrapper for simple text labels in the JSON.
 * Used for fields that have a "label" key.
 */
data class LabelWrapper(
    val label: String?
)

/**
 * Wrapper for image data in the JSON.
 * Includes the image label and optional height attribute.
 */
data class ImageWrapper(
    val label: String?,
    val attributes: ImageAttributes?
)

/**
 * Attributes for image data, specifically height.
 */
data class ImageAttributes(
    val height: String?
)

/**
 * Wrapper for category data in the JSON.
 * Holds the attributes where the genre label is stored.
 */
data class CategoryWrapper(
    val attributes: LabelWrapper?
)

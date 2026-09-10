package com.melakunet.podniche.data

import retrofit2.http.GET
import retrofit2.http.Url
import retrofit2.http.Query

/**
 * Interface for the iTunes Search API.
 * Uses Retrofit annotations to define endpoint calls.
 */
interface ITunesApi {
    /**
     * Searches for podcasts by a term.
     * Hits the "search" endpoint and specifies media as podcast.
     *
     * @param term The search query string.
     * @param media The media type, defaults to "podcast".
     * @return A [PodcastResponse] object.
     */
    @GET("search")
    suspend fun searchPodcasts(
        @Query("term") term: String,
        @Query("media") media: String = "podcast"
    ): PodcastResponse

    /**
     * Gets the top podcasts from a provided RSS feed URL.
     * This uses a dynamic URL to fetch chart results.
     *
     * @param url The full URL of the Apple RSS JSON feed.
     * @return A [TopPodcastsResponse] object.
     */
    @GET
    suspend fun getTopPodcasts(
        @Url url: String
    ): TopPodcastsResponse
}

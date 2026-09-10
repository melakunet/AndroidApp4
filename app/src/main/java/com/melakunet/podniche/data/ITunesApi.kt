package com.melakunet.podniche.data

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface for the iTunes Search API.
 */
interface ITunesApi {
    /**
     * Searches for podcasts by a term.
     *
     * @param term The search query.
     * @param media The media type, defaults to "podcast".
     * @return A [PodcastResponse] containing the search results.
     */
    @GET("search")
    suspend fun searchPodcasts(
        @Query("term") term: String,
        @Query("media") media: String = "podcast"
    ): PodcastResponse
}

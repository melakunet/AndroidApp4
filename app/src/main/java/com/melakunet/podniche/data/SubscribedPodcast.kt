package com.melakunet.podniche.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing a subscribed podcast.
 * Stores metadata and the subscription timestamp.
 */
@Entity(tableName = "subscribed_podcasts")
data class SubscribedPodcast(
    @PrimaryKey val trackId: Long,
    val collectionName: String?,
    val artistName: String?,
    val artworkUrl100: String?,
    val feedUrl: String?,
    val lastEpisodeGuid: String?, // The guid of the newest episode seen
    val subscribedAt: Long // Timestamp of when the user subscribed
)

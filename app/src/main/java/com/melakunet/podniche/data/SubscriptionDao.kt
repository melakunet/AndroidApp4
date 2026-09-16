package com.melakunet.podniche.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for local podcast subscriptions.
 * Provides methods to manage stored podcasts in the Room database.
 */
@Dao
interface SubscriptionDao {

    /**
     * Inserts a new subscribed podcast. Replaces if trackId already exists.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(podcast: SubscribedPodcast)

    /**
     * Deletes a subscribed podcast.
     */
    @Delete
    suspend fun delete(podcast: SubscribedPodcast)

    /**
     * Checks if a podcast is currently subscribed by its trackId.
     */
    @Query("SELECT EXISTS(SELECT 1 FROM subscribed_podcasts WHERE trackId = :trackId)")
    suspend fun isSubscribed(trackId: Long): Boolean

    /**
     * Deletes a subscribed podcast by its trackId.
     */
    @Query("DELETE FROM subscribed_podcasts WHERE trackId = :trackId")
    suspend fun deleteByTrackId(trackId: Long)

    /**
     * Returns all subscribed podcasts ordered by subscription time descending.
     */
    @Query("SELECT * FROM subscribed_podcasts ORDER BY subscribedAt DESC")
    fun getAll(): Flow<List<SubscribedPodcast>>
}

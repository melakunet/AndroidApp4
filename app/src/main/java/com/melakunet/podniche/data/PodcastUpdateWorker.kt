package com.melakunet.podniche.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Background worker that checks for new episodes of subscribed podcasts.
 */
class PodcastUpdateWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val database = AppDatabase.getInstance(applicationContext)
        val dao = database.subscriptionDao()
        val parser = PodcastRssParser()
        val notificationHelper = NotificationHelper(applicationContext)
        notificationHelper.createNotificationChannel()

        val subscriptions = dao.getAllSync()
        var hasError = false

        for (subscription in subscriptions) {
            val feedUrl = subscription.feedUrl ?: continue
            try {
                val episodes = parser.fetchAndParse(feedUrl)
                if (episodes.isNotEmpty()) {
                    val newestEpisode = episodes.first()
                    val newestGuid = newestEpisode.guid

                    if (newestGuid != null && newestGuid != subscription.lastEpisodeGuid) {
                        // Notify user of new episode
                        notificationHelper.showNewEpisodeNotification(
                            subscription.collectionName ?: "Podcast Update",
                            newestEpisode.title ?: "New episode available",
                            subscription.trackId.toInt()
                        )
                        // Save the new guid
                        dao.updateLastEpisodeGuid(subscription.trackId, newestGuid)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                hasError = true
            }
        }

        if (hasError) Result.retry() else Result.success()
    }
}

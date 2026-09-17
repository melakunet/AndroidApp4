package com.melakunet.podniche.data

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

/**
 * Scheduler for the background podcast update worker.
 */
object PodcastUpdateScheduler {

    private const val WORK_NAME = "podcast_update_work"

    /**
     * Schedules the periodic update worker every 1 hour.
     * Requires an active network connection.
     */
    fun scheduleUpdateWorker(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val updateRequest = PeriodicWorkRequestBuilder<PodcastUpdateWorker>(1, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            updateRequest
        )
    }
}

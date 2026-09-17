package com.melakunet.podniche.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.melakunet.podniche.R

/**
 * Helper class for creating and showing notifications.
 */
class NotificationHelper(private val context: Context) {

    companion object {
        private const val CHANNEL_ID = "new_episodes_channel"
        private const val CHANNEL_NAME = "New episodes"
    }

    /**
     * Creates the notification channel for Android O and above.
     */
    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Shows a notification for a new episode.
     *
     * @param podcastName The name of the podcast.
     * @param episodeTitle The title of the new episode.
     * @param notificationId Unique ID for the notification.
     */
    fun showNewEpisodeNotification(podcastName: String, episodeTitle: String, notificationId: Int) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_save)
            .setContentTitle(podcastName)
            .setContentText(episodeTitle)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            try {
                notify(notificationId, builder.build())
            } catch (e: SecurityException) {
                // Handle missing permission gracefully
                e.printStackTrace()
            }
        }
    }
}

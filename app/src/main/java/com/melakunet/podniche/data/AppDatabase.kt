package com.melakunet.podniche.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Main Room database for the application.
 * Manages the [SubscribedPodcast] entity and provides the [SubscriptionDao].
 */
@Database(entities = [SubscribedPodcast::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Exposes the DAO for subscription operations.
     */
    abstract fun subscriptionDao(): SubscriptionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Returns the singleton instance of the database.
         */
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "podniche_db"
                )
                    .fallbackToDestructiveMigration(true) // Acceptable as subscriptions can be re-added
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

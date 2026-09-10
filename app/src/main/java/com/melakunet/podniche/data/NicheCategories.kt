package com.melakunet.podniche.data

import android.content.Context
import com.melakunet.podniche.R

/**
 * Data class representing a niche category in the app.
 * Used for both Apple chart genres and keyword-based searches.
 *
 * @property label The display name string for the chip.
 * @property genreId The Apple genre ID for chart lookup, if available.
 * @property searchTerm The keyword to use for search if no genreId exists.
 */
data class NicheCategory(
    val label: String,
    val genreId: Int?,
    val searchTerm: String?
)

/**
 * Provides the list of predefined niche categories for the app.
 *
 * @param context Required to access string resources for localized labels.
 * @return A static list of [NicheCategory] objects.
 */
fun getNicheCategories(context: Context): List<NicheCategory> {
    // Motivation genre
    val motivation = NicheCategory(context.getString(R.string.cat_motivation), 1500, null)
    // Books genre
    val books = NicheCategory(context.getString(R.string.cat_books), 1482, null)
    // Politics genre
    val politics = NicheCategory(context.getString(R.string.cat_politics), 1527, null)
    // News genre
    val news = NicheCategory(context.getString(R.string.cat_news), 1489, null)
    // Stocks genre
    val stocks = NicheCategory(context.getString(R.string.cat_stocks), 1412, null)
    // Crypto doesn't have a genre ID, so we use a search term
    val crypto = NicheCategory(context.getString(R.string.cat_crypto), null, "cryptocurrency")

    return listOf(motivation, books, politics, news, stocks, crypto)
}
/*
 * Note: genreId categories use Apple's chart feed API.
 * The 'Crypto' category uses a keyword search because there is no specific Apple genre ID for it.
 */

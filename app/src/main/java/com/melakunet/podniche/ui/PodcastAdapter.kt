package com.melakunet.podniche.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.melakunet.podniche.R
import com.melakunet.podniche.data.Podcast

/**
 * Adapter for displaying a list of podcasts in a RecyclerView.
 * Manages the data list and the visibility of ranks.
 */
class PodcastAdapter(private var podcasts: List<Podcast> = emptyList()) :
    RecyclerView.Adapter<PodcastAdapter.PodcastViewHolder>() {

    // Toggle to show or hide the #Rank indicator
    private var showRanks: Boolean = false

    /**
     * ViewHolder for holding podcast item views.
     * References the rank, artwork, title, artist, and details views.
     */
    class PodcastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val rankTextView: TextView = itemView.findViewById(R.id.podcast_rank)
        private val artworkImageView: ImageView = itemView.findViewById(R.id.podcast_artwork)
        private val titleTextView: TextView = itemView.findViewById(R.id.podcast_title)
        private val artistTextView: TextView = itemView.findViewById(R.id.podcast_artist)
        private val detailsTextView: TextView = itemView.findViewById(R.id.podcast_details)

        /**
         * Binds a [Podcast] object to the UI components.
         * Handles rank visibility and uses Glide to load the artwork.
         */
        fun bind(podcast: Podcast, showRank: Boolean, position: Int) {
            // Show rank if we are in chart mode
            if (showRank) {
                rankTextView.visibility = View.VISIBLE
                rankTextView.text = itemView.context.getString(R.string.podcast_rank, position + 1)
            } else {
                rankTextView.visibility = View.GONE
            }

            // Set basic text data
            titleTextView.text = podcast.collectionName ?: ""
            artistTextView.text = podcast.artistName ?: ""
            
            // Format the details line with genre and episode count
            val context = itemView.context
            val genre = podcast.primaryGenreName ?: ""
            val episodeCount = podcast.trackCount ?: 0
            detailsTextView.text = context.getString(R.string.podcast_details, genre, episodeCount)

            // Load the artwork image from URL
            Glide.with(context)
                .load(podcast.artworkUrl100)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .into(artworkImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PodcastViewHolder {
        // Inflate the item layout
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_podcast, parent, false)
        return PodcastViewHolder(view)
    }

    override fun onBindViewHolder(holder: PodcastViewHolder, position: Int) {
        // Bind the data to the view holder
        holder.bind(podcasts[position], showRanks, position)
    }

    override fun getItemCount(): Int = podcasts.size

    /**
     * Updates the list of podcasts and refreshes the adapter.
     * Clears old data and sets the new list and rank mode.
     *
     * @param newItems The new list of podcasts to display.
     * @param showRanks Whether to show the rank number for each item.
     */
    fun updateList(newItems: List<Podcast>, showRanks: Boolean) {
        this.podcasts = newItems
        this.showRanks = showRanks
        // Tell the adapter that the data set has changed to refresh the UI
        notifyDataSetChanged()
    }
}

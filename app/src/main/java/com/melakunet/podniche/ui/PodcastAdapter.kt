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
 */
class PodcastAdapter(private var podcasts: List<Podcast> = emptyList()) :
    RecyclerView.Adapter<PodcastAdapter.PodcastViewHolder>() {

    /**
     * ViewHolder for holding podcast item views.
     */
    class PodcastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val artworkImageView: ImageView = itemView.findViewById(R.id.podcast_artwork)
        private val titleTextView: TextView = itemView.findViewById(R.id.podcast_title)
        private val artistTextView: TextView = itemView.findViewById(R.id.podcast_artist)
        private val detailsTextView: TextView = itemView.findViewById(R.id.podcast_details)

        /**
         * Binds a [Podcast] object to the UI components.
         */
        fun bind(podcast: Podcast) {
            titleTextView.text = podcast.collectionName ?: ""
            artistTextView.text = podcast.artistName ?: ""
            
            val context = itemView.context
            val genre = podcast.primaryGenreName ?: ""
            val episodeCount = podcast.trackCount ?: 0
            detailsTextView.text = context.getString(R.string.podcast_details, genre, episodeCount)

            Glide.with(context)
                .load(podcast.artworkUrl100)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .into(artworkImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PodcastViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_podcast, parent, false)
        return PodcastViewHolder(view)
    }

    override fun onBindViewHolder(holder: PodcastViewHolder, position: Int) {
        holder.bind(podcasts[position])
    }

    override fun getItemCount(): Int = podcasts.size

    /**
     * Updates the list of podcasts and refreshes the adapter.
     */
    fun updateList(newItems: List<Podcast>) {
        podcasts = newItems
        notifyDataSetChanged()
    }
}

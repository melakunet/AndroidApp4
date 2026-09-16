package com.melakunet.podniche.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.melakunet.podniche.R
import com.melakunet.podniche.data.SubscribedPodcast

/**
 * Adapter for displaying the list of subscribed podcasts.
 */
class SubscriptionAdapter(
    private val onPodcastClick: (SubscribedPodcast) -> Unit
) : RecyclerView.Adapter<SubscriptionAdapter.SubscriptionViewHolder>() {

    private var podcasts: List<SubscribedPodcast> = emptyList()

    /**
     * Updates the list of subscribed podcasts.
     */
    fun submitList(newList: List<SubscribedPodcast>) {
        podcasts = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriptionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_subscription, parent, false)
        return SubscriptionViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubscriptionViewHolder, position: Int) {
        holder.bind(podcasts[position])
    }

    override fun getItemCount(): Int = podcasts.size

    inner class SubscriptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val artwork: ImageView = itemView.findViewById(R.id.subscriptionArtwork)
        private val title: TextView = itemView.findViewById(R.id.subscriptionTitle)
        private val artist: TextView = itemView.findViewById(R.id.subscriptionArtist)

        fun bind(podcast: SubscribedPodcast) {
            title.text = podcast.collectionName
            artist.text = podcast.artistName
            Glide.with(itemView.context)
                .load(podcast.artworkUrl100)
                .into(artwork)

            itemView.setOnClickListener { onPodcastClick(podcast) }
        }
    }
}

package com.melakunet.podniche.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.melakunet.podniche.R
import com.melakunet.podniche.data.Episode

/**
 * Adapter for displaying a list of podcast episodes.
 */
class EpisodeAdapter(
    private val onEpisodeClick: (Episode) -> Unit
) : RecyclerView.Adapter<EpisodeAdapter.EpisodeViewHolder>() {

    private var episodes: List<Episode> = emptyList()
    private var playingEpisodeGuid: String? = null

    /**
     * Updates the list of episodes.
     */
    fun submitList(newEpisodes: List<Episode>) {
        episodes = newEpisodes
        notifyDataSetChanged()
    }

    /**
     * Sets the currently playing episode and refreshes the list.
     */
    fun setPlayingEpisode(guid: String?) {
        playingEpisodeGuid = guid
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_episode, parent, false)
        return EpisodeViewHolder(view)
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        holder.bind(episodes[position])
    }

    override fun getItemCount(): Int = episodes.size

    inner class EpisodeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.episodeTitle)
        private val pubDate: TextView = itemView.findViewById(R.id.episodePubDate)
        private val duration: TextView = itemView.findViewById(R.id.episodeDuration)
        private val mediaTypeBadge: TextView = itemView.findViewById(R.id.mediaTypeBadge)
        private val card: View = itemView.findViewById(R.id.episodeCard)

        fun bind(episode: Episode) {
            title.text = episode.title
            pubDate.text = episode.pubDate
            duration.text = episode.duration

            val context = itemView.context
            if (episode.mediaType?.contains("video") == true) {
                mediaTypeBadge.text = context.getString(R.string.video)
                mediaTypeBadge.setBackgroundColor(ContextCompat.getColor(context, R.color.videoBadge))
            } else {
                mediaTypeBadge.text = context.getString(R.string.audio)
                mediaTypeBadge.setBackgroundColor(ContextCompat.getColor(context, R.color.audioBadge))
            }

            if (episode.guid == playingEpisodeGuid) {
                card.setBackgroundColor(ContextCompat.getColor(context, R.color.playingHighlight))
            } else {
                card.setBackgroundColor(ContextCompat.getColor(context, R.color.cardBackground))
            }

            itemView.setOnClickListener { onEpisodeClick(episode) }
        }
    }
}

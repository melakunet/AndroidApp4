package com.melakunet.podniche.ui

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.melakunet.podniche.R
import com.melakunet.podniche.data.Episode
import com.melakunet.podniche.data.ITunesApi
import com.melakunet.podniche.data.PodcastRssParser
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Activity that displays details for a specific podcast, its episodes, and provides playback.
 */
class PodcastDetailActivity : AppCompatActivity() {

    private lateinit var player: ExoPlayer
    private lateinit var playerView: PlayerView
    private lateinit var episodeAdapter: EpisodeAdapter
    private val rssParser = PodcastRssParser()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_podcast_detail)

        val collectionName = intent.getStringExtra("collectionName")
        val artistName = intent.getStringExtra("artistName")
        val artworkUrl = intent.getStringExtra("artworkUrl100")
        var feedUrl = intent.getStringExtra("feedUrl")
        val trackId = intent.getLongExtra("trackId", -1L)

        setupUI(collectionName, artistName, artworkUrl)
        setupPlayer()
        setupRecyclerView()

        if (feedUrl.isNullOrEmpty() && trackId != -1L) {
            resolveFeedUrl(trackId)
        } else if (!feedUrl.isNullOrEmpty()) {
            loadEpisodes(feedUrl)
        } else {
            Toast.makeText(this, getString(R.string.error_message), Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupUI(title: String?, artist: String?, artworkUrl: String?) {
        findViewById<TextView>(R.id.detailTitle).text = title
        findViewById<TextView>(R.id.detailArtist).text = artist
        val artworkView = findViewById<ImageView>(R.id.detailArtwork)
        Glide.with(this).load(artworkUrl).into(artworkView)
    }

    private fun setupPlayer() {
        player = ExoPlayer.Builder(this).build()
        playerView = findViewById(R.id.playerView)
        playerView.player = player
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.episodesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        episodeAdapter = EpisodeAdapter { episode ->
            playEpisode(episode)
        }
        recyclerView.adapter = episodeAdapter
    }

    private fun resolveFeedUrl(trackId: Long) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(ITunesApi::class.java)

        lifecycleScope.launch {
            try {
                val response = api.lookupPodcast(trackId)
                val feedUrl = response.results.firstOrNull()?.feedUrl
                if (!feedUrl.isNullOrEmpty()) {
                    loadEpisodes(feedUrl)
                } else {
                    Toast.makeText(this@PodcastDetailActivity, getString(R.string.error_message), Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@PodcastDetailActivity, getString(R.string.error_message), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadEpisodes(url: String) {
        lifecycleScope.launch {
            try {
                val episodes = rssParser.fetchAndParse(url)
                episodeAdapter.submitList(episodes)
            } catch (e: Exception) {
                Toast.makeText(this@PodcastDetailActivity, getString(R.string.error_message), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun playEpisode(episode: Episode) {
        val mediaUrl = episode.mediaUrl ?: return
        val mediaItem = MediaItem.fromUri(mediaUrl)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()

        findViewById<TextView>(R.id.detailDescription).text = episode.description
        episodeAdapter.setPlayingEpisode(episode.guid)
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }
}

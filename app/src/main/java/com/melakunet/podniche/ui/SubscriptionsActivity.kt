package com.melakunet.podniche.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.melakunet.podniche.R
import com.melakunet.podniche.data.AppDatabase
import kotlinx.coroutines.launch

/**
 * Activity that displays a list of all podcasts the user has subscribed to.
 */
class SubscriptionsActivity : AppCompatActivity() {

    private lateinit var adapter: SubscriptionAdapter
    private lateinit var emptyStateText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscriptions)

        emptyStateText = findViewById(R.id.emptyStateText)
        setupRecyclerView()
        observeSubscriptions()
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.subscriptionsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = SubscriptionAdapter { podcast ->
            val intent = Intent(this, PodcastDetailActivity::class.java).apply {
                putExtra("collectionName", podcast.collectionName)
                putExtra("artistName", podcast.artistName)
                putExtra("artworkUrl100", podcast.artworkUrl100)
                putExtra("feedUrl", podcast.feedUrl)
                putExtra("trackId", podcast.trackId)
            }
            startActivity(intent)
        }
        recyclerView.adapter = adapter
    }

    private fun observeSubscriptions() {
        val dao = AppDatabase.getInstance(this).subscriptionDao()
        lifecycleScope.launch {
            dao.getAll().collect { subscriptions ->
                if (subscriptions.isEmpty()) {
                    emptyStateText.visibility = View.VISIBLE
                    adapter.submitList(emptyList())
                } else {
                    emptyStateText.visibility = View.GONE
                    adapter.submitList(subscriptions)
                }
            }
        }
    }
}

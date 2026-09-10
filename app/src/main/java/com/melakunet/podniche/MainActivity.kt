package com.melakunet.podniche

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.ChipGroup
import com.melakunet.podniche.data.ITunesApi
import com.melakunet.podniche.data.NicheCategory
import com.melakunet.podniche.data.getNicheCategories
import com.melakunet.podniche.ui.PodcastAdapter
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Main activity that displays the podcast search UI and niche category charts.
 * This class handles search input, chip selection, and API coordination.
 */
class MainActivity : AppCompatActivity() {

    // The adapter for the podcast list
    private lateinit var adapter: PodcastAdapter
    // The Retrofit API service
    private lateinit var api: ITunesApi
    // The list of predefined niche categories
    private lateinit var categories: List<NicheCategory>
    // The Material ChipGroup for category selection
    private lateinit var chipGroup: ChipGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Enable edge-to-edge support
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Handle edge-to-edge insets for the main layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize networking and UI
        setupRetrofit()
        setupUI()
    }

    /**
     * Initializes the Retrofit instance for API calls.
     * Uses the iTunes base URL and a Gson converter.
     */
    private fun setupRetrofit() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(ITunesApi::class.java)
    }

    /**
     * Sets up UI components and click listeners.
     * Initializes the RecyclerView, search button, and chip group.
     */
    private fun setupUI() {
        // Find views and set adapter
        val recyclerView = findViewById<RecyclerView>(R.id.podcast_recycler_view)
        adapter = PodcastAdapter()
        recyclerView.adapter = adapter

        val searchEditText = findViewById<EditText>(R.id.search_edit_text)
        val searchButton = findViewById<Button>(R.id.search_button)
        chipGroup = findViewById(R.id.category_chip_group)
        categories = getNicheCategories(this)

        // Handle manual search button clicks
        searchButton.setOnClickListener {
            val query = searchEditText.text.toString().trim()
            if (query.isNotEmpty()) {
                hideKeyboard()
                // Clear any chip selection when doing a manual search
                chipGroup.clearCheck()
                searchPodcasts(query)
            }
        }

        // Handle category chip selection changes
        chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                val index = group.indexOfChild(findViewById(checkedIds.first()))
                if (index in categories.indices) {
                    val category = categories[index]
                    // If category has a genreId, load the top chart
                    if (category.genreId != null) {
                        loadTopPodcasts(category.genreId)
                    } else if (category.searchTerm != null) {
                        // If no genreId, use the search term instead
                        searchPodcasts(category.searchTerm)
                    }
                }
            }
        }
    }

    /**
     * Searches for podcasts using the iTunes API.
     * Launches a coroutine and updates the adapter with results.
     *
     * @param query The search term entered by the user.
     */
    private fun searchPodcasts(query: String) {
        lifecycleScope.launch {
            try {
                // Call the search API
                val response = api.searchPodcasts(query)
                // Update the adapter, ranks are hidden for searches
                adapter.updateList(response.results, showRanks = false)
            } catch (e: Exception) {
                // Log and show error toast if call fails
                Log.e("MainActivity", "Search failed", e)
                Toast.makeText(this@MainActivity, R.string.error_message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Loads the top podcasts for a given genre ID.
     * Uses the RSS feed endpoint to fetch ranked results.
     *
     * @param genreId The Apple genre ID for the chart.
     */
    private fun loadTopPodcasts(genreId: Int) {
        // Build the RSS feed URL for the specific genre
        val url = "https://itunes.apple.com/us/rss/toppodcasts/limit=25/genre=$genreId/json"
        lifecycleScope.launch {
            try {
                // Call the chart API using the full URL
                val response = api.getTopPodcasts(url)
                // Map chart entries to the Podcast model
                val podcasts = response.feed?.entry?.map { it.toPodcast() } ?: emptyList()
                // Update the adapter and enable rank display
                adapter.updateList(podcasts, showRanks = true)
            } catch (e: Exception) {
                // Log and show error toast if call fails
                Log.e("MainActivity", "Chart loading failed", e)
                Toast.makeText(this@MainActivity, R.string.error_message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Hides the software keyboard from the current focus.
     */
    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        currentFocus?.let {
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
}

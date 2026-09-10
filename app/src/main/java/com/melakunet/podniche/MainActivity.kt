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
import com.melakunet.podniche.data.ITunesApi
import com.melakunet.podniche.ui.PodcastAdapter
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Main activity that displays the podcast search UI.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var adapter: PodcastAdapter
    private lateinit var api: ITunesApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Handle edge-to-edge insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRetrofit()
        setupUI()
    }

    /**
     * Initializes the Retrofit instance for API calls.
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
     */
    private fun setupUI() {
        val recyclerView = findViewById<RecyclerView>(R.id.podcast_recycler_view)
        adapter = PodcastAdapter()
        recyclerView.adapter = adapter

        val searchEditText = findViewById<EditText>(R.id.search_edit_text)
        val searchButton = findViewById<Button>(R.id.search_button)

        searchButton.setOnClickListener {
            val query = searchEditText.text.toString().trim()
            if (query.isNotEmpty()) {
                hideKeyboard()
                searchPodcasts(query)
            }
        }
    }

    /**
     * Searches for podcasts using the iTunes API.
     *
     * @param query The search term entered by the user.
     */
    private fun searchPodcasts(query: String) {
        lifecycleScope.launch {
            try {
                val response = api.searchPodcasts(query)
                adapter.updateList(response.results)
            } catch (e: Exception) {
                Log.e("MainActivity", "Search failed", e)
                Toast.makeText(this@MainActivity, R.string.error_message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Hides the software keyboard.
     */
    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        currentFocus?.let {
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
}

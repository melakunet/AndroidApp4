# PodNiche (SuperPodcast) — MWD3B Assignment 7

### What the app does
PodNiche is a podcast discovery app that allows users to search for podcasts via the iTunes Search API. It also features niche category chips (Motivation, Books, Politics, News, Stocks, Crypto) that load Apple's official Top Podcasts charts for those specific genres, displaying them with popularity ranks (#1, #2, etc.). For the Crypto category, the app falls back to a keyword-based search since there is no official Apple genre for it.

### Features list
- **Networking**: Uses Retrofit + Gson to communicate with the iTunes Search API and the Apple Top Charts RSS JSON feed.
- **UI Components**: Implements a RecyclerView with a custom adapter for displaying results.
- **Image Loading**: Uses Glide for efficient artwork loading and caching.
- **Concurrency**: Manages asynchronous API calls using Kotlin coroutines and `lifecycleScope`.
- **Error Handling**: Provides user feedback via Toasts and logs exceptions for debugging.
- **Material Design**: Uses Material Chips for category selection and a horizontal scroll view.

### How to run
1. Open the project in Android Studio.
2. Sync the project with Gradle files.
3. Run the application on an Android device or emulator that has active internet access.

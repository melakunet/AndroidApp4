package com.melakunet.podniche.data

import android.util.Xml
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParser
import java.net.URL

/**
 * Parser for podcast RSS feeds.
 */
class PodcastRssParser {

    /**
     * Fetches and parses a podcast RSS feed from the given URL.
     */
    suspend fun fetchAndParse(url: String): List<Episode> = withContext(Dispatchers.IO) {
        val episodes = mutableListOf<Episode>()
        try {
            val inputStream = URL(url).openStream()
            val parser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)

            var eventType = parser.eventType
            var currentEpisode: MutableEpisode? = null

            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagName = parser.name
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        if (tagName == "item") {
                            currentEpisode = MutableEpisode()
                        } else if (currentEpisode != null) {
                            when (tagName) {
                                "guid" -> currentEpisode.guid = parser.nextText()
                                "title" -> currentEpisode.title = parser.nextText()
                                "description" -> currentEpisode.description = parser.nextText()
                                "pubDate" -> currentEpisode.pubDate = parser.nextText()
                                "enclosure" -> {
                                    currentEpisode.mediaUrl = parser.getAttributeValue(null, "url")
                                    currentEpisode.mediaType = parser.getAttributeValue(null, "type")
                                }
                                "itunes:duration" -> currentEpisode.duration = parser.nextText()
                            }
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (tagName == "item" && currentEpisode != null) {
                            episodes.add(currentEpisode.toEpisode())
                            currentEpisode = null
                        }
                    }
                }
                eventType = parser.next()
            }
            inputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        episodes
    }

    private class MutableEpisode(
        var guid: String? = null,
        var title: String? = null,
        var description: String? = null,
        var mediaUrl: String? = null,
        var mediaType: String? = null,
        var pubDate: String? = null,
        var duration: String? = null
    ) {
        fun toEpisode() = Episode(guid, title, description, mediaUrl, mediaType, pubDate, duration)
    }
}

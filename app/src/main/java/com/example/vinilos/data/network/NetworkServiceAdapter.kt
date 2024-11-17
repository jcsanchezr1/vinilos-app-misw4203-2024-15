package com.example.vinilos.data.network

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.vinilos.data.models.Album
import com.example.vinilos.data.models.Artist
import com.example.vinilos.data.models.Collector
import com.example.vinilos.data.models.Comment
import com.example.vinilos.data.models.Track
import org.json.JSONArray
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class NetworkServiceAdapter private constructor(context: Context) {

    companion object {
        const val BASE_URL = "https://api-backvynils-misw4203-600c0ea84373.herokuapp.com/"
        @Volatile
        private var instance: NetworkServiceAdapter? = null

        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: NetworkServiceAdapter(context).also { instance = it }
            }
    }

    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    private suspend fun getRequest(path: String): String = suspendCoroutine { cont ->
        requestQueue.add(
            StringRequest(Request.Method.GET, BASE_URL + path,
                { response ->
                    cont.resume(response)
                },
                { error ->
                    cont.resumeWithException(error)
                })
        )
    }

    private suspend fun postRequest(path: String, jsonBody: JSONObject): String =
        suspendCoroutine { cont ->
            requestQueue.add(
                object : StringRequest(
                    Method.POST, BASE_URL + path,
                    { response ->
                        cont.resume(response)
                    },
                    { error ->
                        cont.resumeWithException(error)
                    }
                ) {
                    override fun getBodyContentType(): String {
                        return "application/json; charset=utf-8"
                    }

                    override fun getBody(): ByteArray {
                        return jsonBody.toString().toByteArray()
                    }
                }
            )
        }

    suspend fun getAlbums(): List<Album> {
        val response = getRequest("albums")
        val resp = JSONArray(response)
        val list = mutableListOf<Album>()

        for (i in 0 until resp.length()) {
            val item = resp.getJSONObject(i)
            list.add(parseAlbum(item))
        }
        return list
    }

    suspend fun getMusicians(): List<Artist> {
        val response = getRequest("musicians") // Fetch the data using getRequest
        return parseMusicians(response) // Parse the response using a helper function
    }

    suspend fun getBands(): List<Artist> {
        val response = getRequest("bands")
        val resp = JSONArray(response)
        val list = mutableListOf<Artist>()

        for (i in 0 until resp.length()) {
            val item = resp.getJSONObject(i)
            list.add(
                Artist(
                    id = item.getInt("id"),
                    name = item.getString("name"),
                    image = item.getString("image"),
                    description = item.getString("description"),
                    creationDate = item.getString("creationDate"),
                    albums = emptyList(),
                    type = Artist.ArtistType.BAND
                )
            )
        }
        return list.sortedBy { it.name }
    }

    suspend fun postComment(albumId: Int, comment: Comment): Comment {
        val path = "albums/$albumId/comments"

        // Create the JSON request body
        val requestBody = JSONObject().apply {
            put("description", comment.description)
            put("rating", comment.rating)
            put("collector", comment.collector)
        }

        val response = postRequest(path, requestBody)
        val jsonResponse = JSONObject(response)

        return Comment(
            id = jsonResponse.getInt("id"),
            description = jsonResponse.getString("description"),
            rating = jsonResponse.getInt("rating"),
            collector = 100
        )
    }

    suspend fun getCollectors(): List<Collector> {
        val response = getRequest("collectors") // Reuse the getRequest function
        return parseCollectors(response) // Parse the response using a helper function
    }

    private fun parseMusicians(response: String): List<Artist> {
        val jsonArray = JSONArray(response)
        val musicians = mutableListOf<Artist>()

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            musicians.add(
                Artist(
                    id = jsonObject.getInt("id"),
                    name = jsonObject.getString("name"),
                    image = jsonObject.getString("image"),
                    description = jsonObject.getString("description"),
                    creationDate = jsonObject.getString("birthDate"),
                    albums = emptyList(),
                    type = Artist.ArtistType.MUSICIAN
                )
            )
        }

        return musicians.sortedBy { it.name }
    }


    private fun parseCollectors(response: String): List<Collector> {
        val jsonArray = JSONArray(response)
        val collectors = mutableListOf<Collector>()

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            collectors.add(
                Collector(
                    id = jsonObject.getInt("id"),
                    name = jsonObject.getString("name"),
                    telephone = jsonObject.getString("telephone"),
                    email = jsonObject.getString("email"),
                    comments = emptyList(),
                    favoritePerformers = emptyList(),
                    collectorAlbums = emptyList(),
                )
            )
        }

        return collectors
    }


    private fun parseAlbum(item: JSONObject): Album {
        val tracksArray = item.getJSONArray("tracks")
        val tracks = mutableListOf<Track>()
        for (j in 0 until tracksArray.length()) {
            val trackItem = tracksArray.getJSONObject(j)
            tracks.add(
                Track(
                    id = trackItem.getInt("id"),
                    name = trackItem.getString("name"),
                    duration = trackItem.getString("duration")
                )
            )
        }

        val performersArray = item.getJSONArray("performers")
        val performers = mutableListOf<Artist>()
        for (j in 0 until performersArray.length()) {
            val performerItem = performersArray.getJSONObject(j)
            performers.add(
                Artist(
                    id = performerItem.getInt("id"),
                    name = performerItem.getString("name"),
                    image = performerItem.getString("image"),
                    description = performerItem.getString("description"),
                    creationDate = "",
                    albums = emptyList(),
                    type = Artist.ArtistType.MUSICIAN
                )
            )
        }

        val commentsArray = item.getJSONArray("comments")
        val comments = mutableListOf<Comment>()
        for (j in 0 until commentsArray.length()) {
            val commentItem = commentsArray.getJSONObject(j)
            comments.add(
                Comment(
                    id = commentItem.getInt("id"),
                    description = commentItem.getString("description"),
                    rating = commentItem.getInt("rating"),
                    collector = 100
                )
            )
        }

        return Album(
            id = item.getInt("id"),
            name = item.getString("name"),
            cover = item.getString("cover"),
            description = item.getString("description"),
            releaseDate = item.getString("releaseDate"),
            genre = item.getString("genre"),
            recordLabel = item.getString("recordLabel"),
            tracks = tracks,
            performers = performers,
            comments = comments
        )
    }
}

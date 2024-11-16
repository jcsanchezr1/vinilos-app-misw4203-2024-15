package com.example.vinilos.data.network

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.vinilos.data.models.Album
import com.example.vinilos.data.models.Artist
import com.example.vinilos.data.models.Collector
import com.example.vinilos.data.models.Comment
import com.example.vinilos.data.models.Track
import org.json.JSONArray
import org.json.JSONObject

class NetworkServiceAdapter constructor(context: Context) {
    companion object {
        const val BASE_URL = "https://api-backvynils-misw4203-600c0ea84373.herokuapp.com/"
        var instance: NetworkServiceAdapter? = null
        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: NetworkServiceAdapter(context).also {
                    instance = it
                }
            }
    }

    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    fun getBands(onComplete: (resp: List<Artist>) -> Unit, onError: (error: VolleyError) -> Unit) {
        requestQueue.add(
            getRequest("bands",
                { response ->
                    val resp = JSONArray(response)
                    val list = mutableListOf<Artist>()
                    for (i in 0 until resp.length()) {
                        val item = resp.getJSONObject(i)
                        list.add(
                            i,
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
                    val sortedList = list.sortedBy { it.name }
                    onComplete(sortedList)
                },
                {
                    onError(it)
                })
        )
    }

    fun getMusicians(
        onComplete: (resp: List<Artist>) -> Unit,
        onError: (error: VolleyError) -> Unit
    ) {
        requestQueue.add(
            getRequest("musicians",
                { response ->
                    val resp = JSONArray(response)
                    val list = mutableListOf<Artist>()
                    for (i in 0 until resp.length()) {
                        val item = resp.getJSONObject(i)
                        list.add(
                            i,
                            Artist(
                                id = item.getInt("id"),
                                name = item.getString("name"),
                                image = item.getString("image"),
                                description = item.getString("description"),
                                creationDate = item.getString("birthDate"),
                                albums = emptyList(),
                                type = Artist.ArtistType.MUSICIAN
                            )
                        )
                    }
                    val sortedList = list.sortedBy { it.name }
                    onComplete(sortedList)
                },
                {
                    onError(it)
                })
        )
    }

    fun getAlbums(
        onComplete: (resp: List<Album>) -> Unit,
        onError: (error: VolleyError) -> Unit
    ) {
        requestQueue.add(
            getRequest("albums",
                { response ->
                    val resp = JSONArray(response)
                    val list = mutableListOf<Album>()
                    for (i in 0 until resp.length()) {
                        val item = resp.getJSONObject(i)

                        // Parse the tracks list
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

                        // Parse the performers list
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

                        // Parse the comments list
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

                        list.add(
                            i,
                            Album(
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
                        )
                    }
                    onComplete(list)
                },
                {
                    onError(it)
                })
        )
    }

    fun getCollectors(onComplete: (resp: List<Collector>) -> Unit, onError: (error: VolleyError) -> Unit) {
        requestQueue.add(
            getRequest("collectors",
                { response ->
                    val resp = JSONArray(response)
                    val list = mutableListOf<Collector>()
                    for (i in 0 until resp.length()) {
                        val item = resp.getJSONObject(i)
                        list.add(
                            i,
                            Collector(
                                id = item.getInt("id"),
                                name = item.getString("name"),
                                telephone = item.getString("telephone"),
                                email = item.getString("email"),
                                comments = emptyList(),
                                favoritePerformers = emptyList(),
                                collectorAlbums = emptyList(),
                            )
                        )
                    }
                    val sortedList = list.sortedBy { it.name }
                    onComplete(sortedList)
                },
                {
                    onError(it)
                })
        )
    }



    fun postComment(
        albumId: Int,
        comment: Comment,
        onComplete: (Comment) -> Unit,
        onError: (error: VolleyError) -> Unit
    ) {
        val url = "${BASE_URL}albums/$albumId/comments"

        val requestBody = JSONObject()
        requestBody.put("description", comment.description)
        requestBody.put("rating", comment.rating)
        requestBody.put("collector", comment.collector)

        val postRequest = object : StringRequest(
            Method.POST, url,
            { response ->
                val responseObject = JSONObject(response)
                val createdComment = Comment(
                    id = responseObject.getInt("id"),
                    description = responseObject.getString("description"),
                    rating = responseObject.getInt("rating"),
                    collector = 100,
                )
                onComplete(createdComment)
            },
            { error ->
                onError(error)
            }
        ) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toString().toByteArray()
            }
        }

        requestQueue.add(postRequest)
    }

    private fun getRequest(
        path: String,
        responseListener: Response.Listener<String>,
        errorListener: Response.ErrorListener
    ): StringRequest {
        return StringRequest(Request.Method.GET, BASE_URL + path, responseListener, errorListener)
    }

}
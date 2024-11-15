package com.example.vinilos.ui.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vinilos.common.Constant
import com.example.vinilos.databinding.ActivityAlbumDetailBinding
import com.example.vinilos.ui.viewmodels.AlbumViewModel
import com.example.vinilos.ui.views.adapters.PerformerAdapter
import com.example.vinilos.ui.views.adapters.TrackAdapter

class AlbumDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlbumDetailBinding
    private lateinit var albumViewModel: AlbumViewModel
    private lateinit var performerAdapter: PerformerAdapter
    private lateinit var trackAdapter: TrackAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout
        binding = ActivityAlbumDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the ViewModel
        albumViewModel = ViewModelProvider(
            this,
            AlbumViewModel.Factory(application)
        )[AlbumViewModel::class.java]

        // Initialize Adapters
        performerAdapter = PerformerAdapter()
        trackAdapter = TrackAdapter()

        // Set up RecyclerView for Performers
        binding.rvArtistList.apply {
            layoutManager = LinearLayoutManager(this@AlbumDetailActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = performerAdapter
        }

        // Set up RecyclerView for Tracks
        binding.rvTracksList.apply {
            layoutManager = LinearLayoutManager(this@AlbumDetailActivity)
            adapter = trackAdapter
        }

        // Load and observe album data
        val albumId = intent.getIntExtra(Constant.ALBUM_ID, -1)
        observeAlbumData(albumId)
    }

    private fun observeAlbumData(albumId: Int) {
        albumViewModel.getAlbumById(albumId).observe(this) { album ->
            if (album != null) {
                // Bind album details to the layout
                binding.album = album

                // Update nested RecyclerViews
                performerAdapter.setPerformers(album.performers)
                trackAdapter.setTracks(album.tracks)
            }
        }
    }
}

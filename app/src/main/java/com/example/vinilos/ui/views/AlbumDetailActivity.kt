package com.example.vinilos.ui.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.vinilos.R
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
        binding = ActivityAlbumDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        albumViewModel = ViewModelProvider(
            this,
            AlbumViewModel.Factory(application)
        )[AlbumViewModel::class.java]

        binding.viewModel = albumViewModel

        performerAdapter = PerformerAdapter()
        trackAdapter = TrackAdapter()

        binding.rvArtistList.apply {
            layoutManager = LinearLayoutManager(this@AlbumDetailActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = performerAdapter
        }

        binding.rvTracksList.apply {
            layoutManager = LinearLayoutManager(this@AlbumDetailActivity)
            adapter = trackAdapter
        }

        val albumId = intent.getIntExtra(Constant.ALBUM_ID, -1)
        observeAlbumData(albumId)
    }

    private fun observeAlbumData(albumId: Int) {
        albumViewModel.getAlbumById(albumId).observe(this) { album ->
            if (album != null) {

                binding.album = album

                performerAdapter.setPerformers(album.performers)
                trackAdapter.setTracks(album.tracks)

                Glide.with(binding.root.context)
                    .load(album.cover)
                    .placeholder(R.drawable.album_placeholder)
                    .error(R.drawable.album_placeholder)
                    .into(binding.ivAlbumDetailCover)
            }
        }
    }
}

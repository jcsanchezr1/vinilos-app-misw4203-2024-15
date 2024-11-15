package com.example.vinilos.ui.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vinilos.databinding.ActivityAlbumDetailBinding
import com.example.vinilos.ui.viewmodels.AlbumViewModel
import com.example.vinilos.ui.views.adapters.AlbumDetailAdapter

class AlbumDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlbumDetailBinding
    private lateinit var albumViewModel: AlbumViewModel
    private lateinit var albumDetailAdapter: AlbumDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlbumDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        albumViewModel = ViewModelProvider(
            this,
            AlbumViewModel.Factory(application)
        )[AlbumViewModel::class.java]

        albumDetailAdapter = AlbumDetailAdapter()

        binding.rvArtistList.apply {
            layoutManager = LinearLayoutManager(this@AlbumDetailActivity)
            adapter = albumDetailAdapter
        }
        binding.rvTracksList.apply {
            layoutManager = LinearLayoutManager(this@AlbumDetailActivity)
            adapter = albumDetailAdapter
        }

        val albumId = intent.getIntExtra("ALBUM_ID", -1)
        observeAlbumData(albumId)

    }

    private fun observeAlbumData(albumId: Int) {
        albumViewModel.getAlbumById(albumId).observe(this) { album ->
            if (album != null) {
                albumDetailAdapter.albums = listOf(album) // Only a single album
            }
        }
    }
}

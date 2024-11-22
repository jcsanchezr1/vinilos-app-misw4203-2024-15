package com.example.vinilos.ui.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.vinilos.R
import com.example.vinilos.common.Constant
import com.example.vinilos.databinding.ActivityArtistDetailBinding
import com.example.vinilos.ui.adapters.AlbumAdapter
import com.example.vinilos.ui.viewmodels.ArtistViewModel

class ArtistDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArtistDetailBinding
    private lateinit var artistViewModel: ArtistViewModel
    private lateinit var albumAdapter: AlbumAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArtistDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        albumAdapter = AlbumAdapter()

        artistViewModel = ViewModelProvider(
            this,
            ArtistViewModel.Factory(application)
        )[ArtistViewModel::class.java]

        binding.viewModel = artistViewModel

        val artistId = intent.getIntExtra(Constant.ARTIST_ID, -1)
        val artistTypeString = intent.getStringExtra(Constant.ARTIST_TYPE)
        val artistType = when (artistTypeString) {
            "MUSICIAN" -> ArtistViewModel.ArtistType.MUSICIAN
            "BAND" -> ArtistViewModel.ArtistType.BAND
            else -> throw IllegalArgumentException("Invalid artist type")
        }

        observeArtistData(artistId, artistType)

        binding.rvAlbumsList.apply {
            layoutManager = LinearLayoutManager(this@ArtistDetailActivity)
            adapter = albumAdapter
        }

        binding.btnArtistDetailBack.setOnClickListener {
            finish()
        }
    }

    private fun observeArtistData(artistId: Int, artistType: ArtistViewModel.ArtistType) {
        artistViewModel.getArtistById(artistId, artistType).observe(this) { artist ->
            if (artist != null) {
                binding.artist = artist

                albumAdapter.submitList(artist.albums)

                Glide.with(binding.root.context)
                    .load(artist.image)
                    .placeholder(R.drawable.album_placeholder)
                    .error(R.drawable.album_placeholder)
                    .into(binding.ivArtistDetailCover)

                binding.tvAlbumDetailDescription.text = artist.description
                binding.tvDate.text = artistViewModel.formatDate(artist.creationDate)
            }
        }
    }
}

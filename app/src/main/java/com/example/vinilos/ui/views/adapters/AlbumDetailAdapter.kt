package com.example.vinilos.ui.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vinilos.R
import com.example.vinilos.data.models.Album
import com.example.vinilos.databinding.ActivityAlbumDetailBinding
import com.example.vinilos.ui.views.adapters.PerformerAdapter
import com.example.vinilos.ui.views.adapters.TrackAdapter

class AlbumDetailAdapter : RecyclerView.Adapter<AlbumDetailAdapter.AlbumDetailViewHolder>() {

    private var albums: List<Album> = emptyList()

    fun setAlbums(newAlbums: List<Album>) {
        albums = newAlbums
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumDetailViewHolder {
        val binding = ActivityAlbumDetailBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AlbumDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumDetailViewHolder, position: Int) {
        holder.bind(albums[position])
    }

    override fun getItemCount(): Int = albums.size

    inner class AlbumDetailViewHolder(private val binding: ActivityAlbumDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val performerAdapter = PerformerAdapter()
        private val trackAdapter = TrackAdapter()

        init {
            // Set up the Performers RecyclerView
            binding.rvArtistList.apply {
                layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
                adapter = performerAdapter
                setHasFixedSize(true)
            }

            // Set up the Tracks RecyclerView
            binding.rvTracksList.apply {
                layoutManager = LinearLayoutManager(binding.root.context)
                adapter = trackAdapter
                setHasFixedSize(true)
            }
        }

        fun bind(album: Album) {
            // Bind album-level data
            binding.album = album

            // Load album cover
            Glide.with(binding.root.context)
                .load(album.cover)
                .placeholder(R.drawable.album_placeholder)
                .error(R.drawable.album_placeholder)
                .into(binding.ivAlbumDetailCover)

            // Update nested adapters
            performerAdapter.setPerformers(album.performers)
            trackAdapter.setTracks(album.tracks)
        }
    }
}

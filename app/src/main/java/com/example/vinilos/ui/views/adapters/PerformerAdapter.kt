package com.example.vinilos.ui.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vinilos.R
import com.example.vinilos.data.models.Artist
import com.example.vinilos.databinding.ArtistAlbumDetailItemBinding

class PerformerAdapter : RecyclerView.Adapter<PerformerAdapter.PerformerViewHolder>() {

    private var performers: List<Artist> = emptyList()

    fun setPerformers(newPerformers: List<Artist>) {
        performers = newPerformers
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PerformerViewHolder {
        val binding = ArtistAlbumDetailItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PerformerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PerformerViewHolder, position: Int) {
        holder.bind(performers[position])
    }

    override fun getItemCount(): Int = performers.size

    class PerformerViewHolder(private val binding: ArtistAlbumDetailItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(artist: Artist) {
            binding.artist = artist
            Glide.with(binding.root.context)
                .load(artist.image)
                .placeholder(R.drawable.album_placeholder)
                .error(R.drawable.album_placeholder)
                .into(binding.ivArtistImage)
        }
    }
}

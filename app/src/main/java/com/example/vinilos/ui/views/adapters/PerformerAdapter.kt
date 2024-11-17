package com.example.vinilos.ui.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vinilos.R
import com.example.vinilos.data.models.Artist
import com.example.vinilos.databinding.ArtistAlbumDetailItemBinding

class PerformerAdapter : ListAdapter<Artist, PerformerAdapter.PerformerViewHolder>(PerformerDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PerformerViewHolder {
        val binding = ArtistAlbumDetailItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PerformerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PerformerViewHolder, position: Int) {
        val performer = getItem(position)
        holder.bind(performer)
    }

    class PerformerViewHolder(private val viewDataBinding: ArtistAlbumDetailItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        fun bind(artist: Artist) {
            viewDataBinding.artist = artist
            Glide.with(viewDataBinding.root.context)
                .load(artist.image)
                .placeholder(R.drawable.album_placeholder)
                .error(R.drawable.album_placeholder)
                .into(viewDataBinding.ivArtistImage)
        }
    }

    companion object {
        val PerformerDiffCallback = object : DiffUtil.ItemCallback<Artist>() {
            override fun areItemsTheSame(oldItem: Artist, newItem: Artist): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Artist, newItem: Artist): Boolean {
                return oldItem == newItem
            }
        }
    }
}

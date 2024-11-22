package com.example.vinilos.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vinilos.R
import com.example.vinilos.data.models.Artist
import com.example.vinilos.databinding.ArtistItemBinding

class ArtistAdapter(private val onClick: (Artist) -> Unit) :
    ListAdapter<Artist, ArtistAdapter.ArtistViewHolder>(ArtistDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val withDataBinding: ArtistItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            ArtistViewHolder.LAYOUT,
            parent,
            false
        )
        return ArtistViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val artist = getItem(position)
        holder.viewDataBinding.artist = artist

        Glide.with(holder.itemView.context)
            .load(artist.image)
            .placeholder(R.drawable.album_placeholder)
            .error(R.drawable.album_placeholder)
            .into(holder.viewDataBinding.ivArtistImage)

        holder.itemView.setOnClickListener {
            onClick(artist)
        }
    }

    class ArtistViewHolder(val viewDataBinding: ArtistItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.artist_item
        }
    }

    companion object {
        val ArtistDiffCallback = object : DiffUtil.ItemCallback<Artist>() {
            override fun areItemsTheSame(oldItem: Artist, newItem: Artist): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Artist, newItem: Artist): Boolean {
                return oldItem == newItem
            }
        }
    }
}

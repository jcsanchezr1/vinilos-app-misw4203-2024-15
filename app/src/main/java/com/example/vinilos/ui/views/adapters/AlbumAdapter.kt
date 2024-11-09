package com.example.vinilos.ui.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vinilos.R
import com.example.vinilos.databinding.AlbumItemBinding
import com.example.vinilos.data.models.Album

class AlbumAdapter : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    var albums: List<Album> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onItemClickListener: ((Album) -> Unit)? = null


    // Inflates the item layout and returns a ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val withDataBinding: AlbumItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            AlbumViewHolder.LAYOUT,
            parent,
            false
        )
        return AlbumViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val album = albums[position]
        holder.viewDataBinding.album = album

        Glide.with(holder.itemView.context)
            .load(album.cover)
            .placeholder(R.drawable.album_placeholder)
            .error(R.drawable.album_placeholder)
            .into(holder.viewDataBinding.ivAlbumCover)

        // Set click listener
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(album)
        }
    }

    override fun getItemCount(): Int {
        return albums.size
    }

    class AlbumViewHolder(val viewDataBinding: AlbumItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.album_item
        }
    }

}
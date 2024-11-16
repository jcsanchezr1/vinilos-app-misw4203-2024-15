package com.example.vinilos.ui.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vinilos.R
import com.example.vinilos.data.models.Album
import com.example.vinilos.databinding.AlbumItemBinding

class AlbumAdapter : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    var albums: List<Album> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

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

        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it1 -> it1(album.id) }
        }
    }

    private var onItemClickListener: ((Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
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
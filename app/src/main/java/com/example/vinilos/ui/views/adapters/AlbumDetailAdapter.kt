// AlbumDetailAdapter.kt
package com.example.vinilos.ui.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vinilos.R
import com.example.vinilos.data.models.Album
import com.example.vinilos.data.models.Comment
import com.example.vinilos.databinding.ActivityAlbumDetailBinding
import com.example.vinilos.databinding.PerformerItemBinding
import com.example.vinilos.databinding.TrackItemBinding
import com.example.vinilos.databinding.CommentItemBinding

class AlbumDetailAdapter : RecyclerView.Adapter<AlbumDetailAdapter.AlbumDetailViewHolder>() {

    private var albums: List<Album> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumDetailViewHolder {
        val withDataBinding: ActivityAlbumDetailBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            AlbumDetailViewHolder.LAYOUT,
            parent,
            false
        )
        return AlbumDetailViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: AlbumDetailViewHolder, position: Int) {
        val album = albums[position]
        holder.bind(album)
    }

    override fun getItemCount(): Int = albums.size

    inner class AlbumDetailViewHolder(val viewDataBinding: ActivityAlbumDetailBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {

        fun bind(album: Album) {
            viewDataBinding.album = album

            Glide.with(itemView.context)
                .load(album.cover)
                .placeholder(R.drawable.album_placeholder)
                .error(R.drawable.album_placeholder)
                .into(viewDataBinding.ivAlbumCover)

            // Set up Performers RecyclerView directly
            viewDataBinding.recyclerViewPerformers.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = object : RecyclerView.Adapter<PerformerViewHolder>() {
                    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PerformerViewHolder {
                        val binding = PerformerItemBinding.inflate(
                            LayoutInflater.from(parent.context), parent, false
                        )
                        return PerformerViewHolder(binding)
                    }

                    override fun onBindViewHolder(holder: PerformerViewHolder, position: Int) {
                        holder.bind(album.performers[position])
                    }

                    override fun getItemCount(): Int = album.performers.size
                }
            }

            // Set up Tracks RecyclerView directly
            viewDataBinding.recyclerViewTracks.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = object : RecyclerView.Adapter<TrackViewHolder>() {
                    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
                        val binding = TrackItemBinding.inflate(
                            LayoutInflater.from(parent.context), parent, false
                        )
                        return TrackViewHolder(binding)
                    }

                    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
                        holder.bind(album.tracks[position])
                    }

                    override fun getItemCount(): Int = album.tracks.size
                }
            }

            // Set up Comments RecyclerView directly
            viewDataBinding.recyclerViewComments.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = object : RecyclerView.Adapter<CommentViewHolder>() {
                    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
                        val binding = CommentItemBinding.inflate(
                            LayoutInflater.from(parent.context), parent, false
                        )
                        return CommentViewHolder(binding)
                    }

                    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
                        holder.bind(album.comments[position])
                    }

                    override fun getItemCount(): Int = album.comments.size
                }
            }
        }

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.activity_album_detail
        }
    }

    inner class PerformerViewHolder(private val binding: PerformerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(performer: Performer) {
            binding.performer = performer
        }
    }

    inner class TrackViewHolder(private val binding: TrackItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(track: Track) {
            binding.track = track
        }
    }

    inner class CommentViewHolder(private val binding: CommentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: Comment) {
            binding.comment = comment
        }
    }
}

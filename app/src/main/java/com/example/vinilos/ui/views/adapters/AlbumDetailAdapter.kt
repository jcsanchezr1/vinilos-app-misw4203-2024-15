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
import com.example.vinilos.data.models.Artist
import com.example.vinilos.data.models.Track
import com.example.vinilos.databinding.ActivityAlbumDetailBinding
import com.example.vinilos.databinding.ArtistItemBinding
import com.example.vinilos.databinding.TrackAlbumDetailItemBinding

class AlbumDetailAdapter : RecyclerView.Adapter<AlbumDetailAdapter.AlbumDetailViewHolder>() {

    var albums: List<Album> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    /*override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumDetailViewHolder {
        val withDataBinding: ActivityAlbumDetailBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            AlbumDetailViewHolder.LAYOUT,
            parent,
            false
        )
        return AlbumDetailViewHolder(withDataBinding)
    }*/

    override fun onBindViewHolder(holder: AlbumDetailViewHolder, position: Int) {
        val album = albums[position]
        holder.bind(album)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumDetailViewHolder {
        TODO("Not yet implemented")
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
                .into(viewDataBinding.ivAlbumDetailCover)

            // Set up Performers RecyclerView directly
            viewDataBinding.rvArtistList.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = object : RecyclerView.Adapter<PerformerViewHolder>() {
                    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PerformerViewHolder {
                        val binding = ArtistItemBinding.inflate(
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

            viewDataBinding.rvTracksList.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = object : RecyclerView.Adapter<TrackViewHolder>() {
                    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
                        val binding = TrackAlbumDetailItemBinding.inflate(
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
            /*viewDataBinding.recyclerViewComments.apply {
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
            }*/
        }


    }

    class PerformerViewHolder(val viewDataBinding: ArtistItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        fun bind(artist: Artist) {
            viewDataBinding.artist = artist
        }

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.artist_album_detail_item
        }
    }

    class TrackViewHolder(val viewDataBinding: TrackAlbumDetailItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        fun bind(track: Track) {
            viewDataBinding.track = track
        }
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.track_album_detail_item
        }
    }

    /*inner class CommentViewHolder(private val binding: Comment) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: Comment) {
            binding.comment = comment
        }
    }*/
}

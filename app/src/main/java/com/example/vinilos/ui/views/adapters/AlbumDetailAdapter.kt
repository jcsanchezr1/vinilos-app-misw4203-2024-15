// AlbumDetailAdapter.kt
package com.example.vinilos.ui.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vinilos.R
import com.example.vinilos.data.models.Album
import com.example.vinilos.data.models.Artist
import com.example.vinilos.data.models.Track
import com.example.vinilos.databinding.ActivityAlbumDetailBinding
import com.example.vinilos.databinding.ArtistAlbumDetailItemBinding
import com.example.vinilos.databinding.TrackAlbumDetailItemBinding

class AlbumDetailAdapter : RecyclerView.Adapter<AlbumDetailAdapter.AlbumDetailViewHolder>() {

    companion object {
        @LayoutRes
        private val LAYOUT = R.layout.activity_album_detail
        private val LAYOUT_ARTIST = R.layout.artist_album_detail_item
        private val LAYOUT_TRACK = R.layout.track_album_detail_item
    // Define LAYOUT as a top-level property of the adapter
    }

    var albums: List<Album> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumDetailViewHolder {
        val withDataBinding: ActivityAlbumDetailBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            LAYOUT, // Use the LAYOUT constant
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

    inner class AlbumDetailViewHolder(private val viewDataBinding: ActivityAlbumDetailBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {

        fun bind(album: Album) {
            viewDataBinding.album = album

            Glide.with(itemView.context)
                .load(album.cover)
                .placeholder(R.drawable.album_placeholder)
                .error(R.drawable.album_placeholder)
                .into(viewDataBinding.ivAlbumDetailCover)

            // Set up Performers RecyclerView
            viewDataBinding.rvArtistList.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = object : RecyclerView.Adapter<PerformerViewHolder>() {
                    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PerformerViewHolder {
                        val withDataBinding: ArtistAlbumDetailItemBinding = DataBindingUtil.inflate(
                            LayoutInflater.from(parent.context),
                            LAYOUT_ARTIST, // Use the LAYOUT constant
                            parent,
                            false
                        )
                        return PerformerViewHolder(withDataBinding)
                    }

                    override fun onBindViewHolder(holder: PerformerViewHolder, position: Int) {
                        holder.bind(album.performers[position])
                    }

                    override fun getItemCount(): Int = album.performers.size
                }
            }

            // Set up Tracks RecyclerView
            viewDataBinding.rvTracksList.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = object : RecyclerView.Adapter<TrackViewHolder>() {
                    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
                        val withDataBinding: TrackAlbumDetailItemBinding = DataBindingUtil.inflate(
                            LayoutInflater.from(parent.context),
                            LAYOUT_TRACK, // Use the LAYOUT constant
                            parent,
                            false
                        )
                        return TrackViewHolder(withDataBinding)
                    }

                    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
                        holder.bind(album.tracks[position])
                    }

                    override fun getItemCount(): Int = album.tracks.size
                }
            }
        }
    }

    class PerformerViewHolder(private val viewDataBinding: ArtistAlbumDetailItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        fun bind(artist: Artist) {
            viewDataBinding.artist = artist
        }
    }

    class TrackViewHolder(private val viewDataBinding: TrackAlbumDetailItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        fun bind(track: Track) {
            viewDataBinding.track = track
        }
    }
}




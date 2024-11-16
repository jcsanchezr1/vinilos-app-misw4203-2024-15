package com.example.vinilos.ui.views

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.vinilos.R
import com.example.vinilos.common.Constant
import com.example.vinilos.databinding.ActivityAlbumDetailBinding
import com.example.vinilos.ui.viewmodels.AlbumViewModel
import com.example.vinilos.ui.views.adapters.CommentAdapter
import com.example.vinilos.ui.views.adapters.PerformerAdapter
import com.example.vinilos.ui.views.adapters.TrackAdapter

class AlbumDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlbumDetailBinding
    private lateinit var albumViewModel: AlbumViewModel
    private lateinit var performerAdapter: PerformerAdapter
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var commentAdapter: CommentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlbumDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        albumViewModel = ViewModelProvider(
            this,
            AlbumViewModel.Factory(application)
        )[AlbumViewModel::class.java]

        binding.viewModel = albumViewModel

        performerAdapter = PerformerAdapter()
        trackAdapter = TrackAdapter()
        commentAdapter = CommentAdapter()

        binding.rvArtistList.apply {
            layoutManager = LinearLayoutManager(this@AlbumDetailActivity)
            adapter = performerAdapter
        }

        binding.rvTracksList.apply {
            layoutManager = LinearLayoutManager(this@AlbumDetailActivity)
            adapter = trackAdapter
        }

        binding.rvCommentsList.apply {
            layoutManager = LinearLayoutManager(this@AlbumDetailActivity)
            adapter = commentAdapter
        }

        albumViewModel.isTracksEmpty.observe(this) { isEmpty ->
            binding.rvTracksList.visibility = if (isEmpty) View.GONE else View.VISIBLE
        }

        val albumId = intent.getIntExtra(Constant.ALBUM_ID, -1)
        observeAlbumData(albumId)

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.rating_options,
            R.layout.score_item
        ).apply {
            setDropDownViewResource(R.layout.score_item_dropdown)
        }

        binding.spinnerRating.adapter = adapter

        val editText = binding.editTextParagraph
        val charCounter = binding.charCounter

        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val currentLength = s?.length ?: 0
                val maxLength = 500
                charCounter.text = getString(R.string.char_counter_format, currentLength, maxLength)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.btnAlbumDetailBack.setOnClickListener {
            finish()
        }
    }

    private fun observeAlbumData(albumId: Int) {
        albumViewModel.getAlbumById(albumId).observe(this) { album ->
            if (album != null) {

                binding.album = album

                performerAdapter.setPerformers(album.performers)
                trackAdapter.setTracks(album.tracks)
                commentAdapter.setComments(album.comments)

                Glide.with(binding.root.context)
                    .load(album.cover)
                    .placeholder(R.drawable.album_placeholder)
                    .error(R.drawable.album_placeholder)
                    .into(binding.ivAlbumDetailCover)

                albumViewModel.setAlbum(album)
            }
        }
    }
}

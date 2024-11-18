package com.example.vinilos.ui.views

import android.app.Dialog
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.vinilos.R
import com.example.vinilos.common.Constant
import com.example.vinilos.databinding.ActivityAlbumDetailBinding
import com.example.vinilos.databinding.CustomDialogBinding
import com.example.vinilos.ui.viewmodels.AlbumViewModel
import com.example.vinilos.ui.adapters.CommentAdapter
import com.example.vinilos.ui.adapters.PerformerAdapter
import com.example.vinilos.ui.adapters.TrackAdapter
import kotlinx.coroutines.launch

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

        val userType = intent.getStringExtra(Constant.USER_TYPE)

        if (userType == getString(R.string.type_collector)) {
            binding.publishButton.visibility = View.VISIBLE
            binding.labelRating.visibility = View.VISIBLE
            binding.spinnerRating.visibility = View.VISIBLE
            binding.flComment.visibility = View.VISIBLE
        } else {
            binding.publishButton.visibility = View.GONE
            binding.labelRating.visibility = View.GONE
            binding.spinnerRating.visibility = View.GONE
            binding.flComment.visibility = View.GONE
        }

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

        binding.publishButton.setOnClickListener {
            val commentText = binding.editTextParagraph.text.toString().trim()
            val rating = binding.spinnerRating.selectedItem.toString().toIntOrNull()

            if (commentText.isEmpty()) {
                showCustomDialog(
                    message = getString(R.string.comment_error),
                    isSuccess = false
                )
            } else if (rating == null) {
                showCustomDialog(
                    message = getString(R.string.score_error),
                    isSuccess = false
                )
            } else {
                lifecycleScope.launch {
                    try {
                        albumViewModel.postComment(albumId, commentText, rating)

                        // Clear the input fields after successful submission
                        binding.editTextParagraph.text.clear()
                        binding.spinnerRating.setSelection(0)

                        showCustomDialog(
                            message = getString(R.string.success_comment),
                            isSuccess = true
                        )
                    } catch (e: Exception) {
                        showCustomDialog(
                            message = getString(R.string.comment_error),
                            isSuccess = false
                        )
                    }
                }
            }
        }

    }

    private fun observeAlbumData(albumId: Int) {
        albumViewModel.getAlbumById(albumId).observe(this) { album ->
            if (album != null) {
                binding.album = album

                performerAdapter.submitList(album.performers)
                trackAdapter.submitList(album.tracks)

                albumViewModel.loadComments(albumId)

                Glide.with(binding.root.context)
                    .load(album.cover)
                    .placeholder(R.drawable.album_placeholder)
                    .error(R.drawable.album_placeholder)
                    .into(binding.ivAlbumDetailCover)

                albumViewModel.setAlbum(album)
            }
        }
        albumViewModel.comments.observe(this) { comments ->
            commentAdapter.submitList(comments.reversed())
            binding.rvCommentsList.visibility = if (comments.isEmpty()) View.GONE else View.VISIBLE
        }
    }

    private fun showCustomDialog(message: String, isSuccess: Boolean) {
        val dialog = Dialog(this)
        val binding = CustomDialogBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)

        binding.dialogMessage.text = message

        if (isSuccess){
            binding.dialogTitle.text = getString(R.string.success_title)
        } else {
            binding.dialogTitle.text = getString(R.string.error_title)
        }

        val backgroundColor = if (isSuccess) {
            ContextCompat.getColor(this, R.color.success)
        } else {
            ContextCompat.getColor(this, R.color.error)
        }

        binding.root.setBackgroundColor(backgroundColor)

        val shapeDrawable = ContextCompat.getDrawable(this, R.drawable.alert_dialog_background) as GradientDrawable
        shapeDrawable.setColor(backgroundColor)
        dialog.window?.setBackgroundDrawable(shapeDrawable)

        binding.dialogButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

}

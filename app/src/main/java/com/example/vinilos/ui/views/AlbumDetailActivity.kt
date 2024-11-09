package com.example.vinilos.ui.views

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.vinilos.R
import com.example.vinilos.common.Constant
import com.example.vinilos.common.UserType
import com.example.vinilos.databinding.ActivityAlbumDetailBinding
import com.example.vinilos.ui.viewmodels.AlbumViewModel

class AlbumDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlbumDetailBinding
    private lateinit var albumViewModel: AlbumViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlbumDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        albumViewModel = ViewModelProvider(
            this,
            AlbumViewModel.Factory(this.application)
        )[AlbumViewModel::class.java]

        val albumId = intent.getIntExtra(Constant.ALBUM_ID, -1)

        albumViewModel.getAlbumById(albumId).observe(this) { album ->
            if (album != null) {
                binding.tvAlbumDetailTitle.text = album.name
                binding.tvAlbumDetailDescription.text = album.description
                Glide.with(this)
                    .load(album.cover)
                    .placeholder(R.drawable.album_placeholder)
                    .error(R.drawable.album_placeholder)
                    .into(binding.ivAlbumDetailCover)
            }
        }

        binding.btnAlbumDetailBack.setOnClickListener {
            val userTypeValue = intent.getStringExtra(Constant.USER_TYPE) ?: UserType.VISITOR.type
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(Constant.USER_TYPE, userTypeValue)
            startActivity(intent)
        }
    }
}
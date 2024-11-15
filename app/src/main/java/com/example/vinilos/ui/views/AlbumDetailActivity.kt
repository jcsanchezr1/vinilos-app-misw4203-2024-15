package com.example.vinilos.ui.views

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.vinilos.R
import com.example.vinilos.common.Constant
import com.example.vinilos.databinding.ActivityAlbumDetailBinding
import com.example.vinilos.ui.viewmodels.AlbumViewModel

class AlbumDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlbumDetailBinding
    private lateinit var albumViewModel: AlbumViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlbumDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val spinnerRating = findViewById<Spinner>(R.id.spinnerRating)
        val ratings = listOf("Selecciona una opción", "1", "2", "3", "4", "5")

        val adapter = object : ArrayAdapter<String>(
            this,
            R.layout.score_item,
            ratings
        ) {
            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                val textView = view as TextView
                textView.setTextColor(
                    ContextCompat.getColor(
                        context,
                        if (position == 0) R.color.hint_text else R.color.color_primary
                    )
                )
                return view
            }
        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRating.adapter = adapter
        spinnerRating.setSelection(0)

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

        binding.btnAlbumDetailBack?.setOnClickListener {
            finish()
        }
    }
}
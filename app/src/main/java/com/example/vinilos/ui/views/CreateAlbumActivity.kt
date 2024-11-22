package com.example.vinilos.ui.views

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.vinilos.data.models.Album
import com.example.vinilos.ui.viewmodels.AlbumViewModel

class CreateAlbumActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateAlbumBinding
    private lateinit var albumViewModel: AlbumViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAlbumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        albumViewModel = ViewModelProvider(
            this,
            AlbumViewModel.Factory(application)
        )[AlbumViewModel::class.java]

        binding.btnSubmitAlbum.setOnClickListener {
            val name = binding.etAlbumName.text.toString().trim()
            val cover = binding.etAlbumCover.text.toString().trim()
            val releaseDate = binding.etAlbumReleaseDate.text.toString().trim()
            val description = binding.etAlbumDescription.text.toString().trim()
            val genre = binding.etAlbumGenre.text.toString().trim()
            val recordLabel = binding.etAlbumRecordLabel.text.toString().trim()

            if (name.isNotEmpty() && cover.isNotEmpty() && releaseDate.isNotEmpty()
                && description.isNotEmpty() && genre.isNotEmpty() && recordLabel.isNotEmpty()
            ) {
                val newAlbum = Album(
                    id = 0,
                    name = name,
                    cover = cover,
                    releaseDate = releaseDate,
                    description = description,
                    genre = genre,
                    recordLabel = recordLabel,
                    tracks = emptyList(),
                    performers = emptyList(),
                    comments = emptyList()
                )
                albumViewModel.createAlbum(newAlbum)
                finish()
            } else {
                Toast.makeText(this, "Todos los campos son requeridos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

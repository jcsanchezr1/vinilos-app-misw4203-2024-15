package com.example.vinilos.ui.views

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.vinilos.data.models.Album
import com.example.vinilos.databinding.AlbumDetailFragmentBinding

class AlbumDetailFragment : Fragment() {

    private var _binding: AlbumDetailFragmentBinding? = null
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AlbumDetailFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        val album: Album? = arguments?.getParcelable("album", Album::class.java)

        album?.let {
            binding.album = it

            Glide.with(this)
                .load(it.cover)
                .into(binding.ivAlbumDetailCover)
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
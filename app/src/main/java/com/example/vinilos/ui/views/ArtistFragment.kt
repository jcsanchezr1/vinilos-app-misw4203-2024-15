package com.example.vinilos.ui.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vinilos.common.Constant
import com.example.vinilos.databinding.ArtistFragmentBinding
import com.example.vinilos.ui.adapters.ArtistAdapter
import com.example.vinilos.ui.viewmodels.ArtistViewModel

class ArtistFragment : Fragment() {
    private var _binding: ArtistFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: ArtistViewModel
    private lateinit var viewModelAdapter: ArtistAdapter
    private var currentType: ArtistViewModel.ArtistType = ArtistViewModel.ArtistType.MUSICIAN

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ArtistFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModelAdapter = ArtistAdapter { artist ->
            val intent = Intent(requireContext(), ArtistDetailActivity::class.java)
            intent.putExtra(Constant.ARTIST_ID, artist.id)
            intent.putExtra(Constant.ARTIST_TYPE, if (binding.musiciansButton.isSelected) "MUSICIAN" else "BAND")
            startActivity(intent)
        }

        binding.musiciansButton.isSelected = true
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.artistRv
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = viewModelAdapter

        binding.musiciansButton.setOnClickListener {
            currentType = ArtistViewModel.ArtistType.MUSICIAN
            binding.musiciansButton.isSelected = true
            binding.bandsButton.isSelected = false
            viewModel.loadArtists(ArtistViewModel.ArtistType.MUSICIAN)
        }

        binding.bandsButton.setOnClickListener {
            currentType = ArtistViewModel.ArtistType.BAND
            binding.bandsButton.isSelected = true
            binding.musiciansButton.isSelected = false
            viewModel.loadArtists(ArtistViewModel.ArtistType.BAND)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        viewModel = ViewModelProvider(this, ArtistViewModel.Factory(activity.application))[ArtistViewModel::class.java]

        viewModel.artists.observe(viewLifecycleOwner) { artistList ->
            viewModelAdapter.submitList(artistList)
        }

        viewModel.eventNetworkError.observe(viewLifecycleOwner) { isNetworkError ->
            if (isNetworkError) onNetworkError()
        }

        viewModel.loadArtists(currentType)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onNetworkError() {
        if (!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }
}

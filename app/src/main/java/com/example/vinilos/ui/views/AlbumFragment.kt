package com.example.vinilos.ui.views

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vinilos.common.Constant
import com.example.vinilos.databinding.AlbumFragmentBinding
import com.example.vinilos.ui.viewmodels.AlbumViewModel
import com.example.vinilos.ui.views.adapters.AlbumAdapter
import java.text.Normalizer
import java.util.Locale

class AlbumFragment : Fragment() {

    private var _binding: AlbumFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: AlbumViewModel
    private lateinit var viewModelAdapter: AlbumAdapter
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AlbumFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModelAdapter = AlbumAdapter()

        viewModelAdapter.setOnItemClickListener { albumId ->
            val intent = Intent(requireContext(), AlbumDetailActivity::class.java)
            intent.putExtra(Constant.ALBUM_ID, albumId)
            startActivity(intent)
        }

        progressBar = binding.progressBar
        recyclerView = binding.albumRv
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = viewModelAdapter

        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterAlbums(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        viewModel = ViewModelProvider(
            this,
            AlbumViewModel.Factory(activity.application)
        )[AlbumViewModel::class.java]

        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        viewModel.albums.observe(viewLifecycleOwner) { albumList ->
            val sortedAlbums = albumList.sortedBy { it.name }
            viewModelAdapter.albums = sortedAlbums
            progressBar.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }

        viewModel.eventNetworkError.observe(viewLifecycleOwner) { isNetworkError ->
            if (isNetworkError) onNetworkError()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onNetworkError() {
        if (!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
            progressBar.visibility = View.GONE
        }
    }

    private fun filterAlbums(query: String) {
        val normalizedQuery = query.normalize()
        val filteredAlbums = viewModel.albums.value?.filter { album ->
            album.name.normalize().contains(normalizedQuery)
        } ?: emptyList()
        viewModelAdapter.albums = filteredAlbums
        binding.tvNoResults.visibility = if (filteredAlbums.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun String.normalize(): String {
        return Normalizer.normalize(this, Normalizer.Form.NFD)
            .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
            .lowercase(Locale.getDefault())
    }
}

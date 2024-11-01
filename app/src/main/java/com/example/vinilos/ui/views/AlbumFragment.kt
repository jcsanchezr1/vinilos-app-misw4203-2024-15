package com.example.vinilos.ui.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vinilos.R
import com.example.vinilos.databinding.AlbumFragmentBinding
import com.example.vinilos.models.Album
import com.example.vinilos.ui.viewmodels.AlbumViewModel
import com.example.vinilos.ui.views.adapters.AlbumAdapter

class AlbumFragment : Fragment() {

    private lateinit var ivLogout: ImageView
    private var _binding: AlbumFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: AlbumViewModel
    private lateinit var viewModelAdapter: AlbumAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = AlbumFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModelAdapter = AlbumAdapter()

        ivLogout = view.findViewById(R.id.ivLogout)

        ivLogout.setOnClickListener {
            val intent = Intent(requireActivity(), HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            requireActivity().finish()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.albumRv
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = viewModelAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        viewModel = ViewModelProvider(this, AlbumViewModel.Factory(activity.application)).get(
            AlbumViewModel::class.java
        )
        viewModel.albums.observe(viewLifecycleOwner, Observer<List<Album>> {
            it.apply {
                viewModelAdapter!!.albums = this
            }
        })
        viewModel.eventNetworkError.observe(
            viewLifecycleOwner,
            Observer<Boolean> { isNetworkError ->
                if (isNetworkError) onNetworkError()
            })
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
package com.example.vinilos.ui.views

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
import com.example.vinilos.databinding.CollectorFragmentBinding
import com.example.vinilos.ui.viewmodels.CollectorViewModel
import com.example.vinilos.ui.views.adapters.CollectorAdapter
import java.text.Normalizer
import java.util.Locale

class CollectorFragment : Fragment() {

    private var _binding: CollectorFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: CollectorViewModel
    private lateinit var viewModelAdapter: CollectorAdapter
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CollectorFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModelAdapter = CollectorAdapter()

        progressBar = binding.progressBarCollector
        recyclerView = binding.collectorRv

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = viewModelAdapter

        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterCollectors(s.toString())
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
            CollectorViewModel.Factory(activity.application)
        )[CollectorViewModel::class.java]

        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        viewModel.collectors.observe(viewLifecycleOwner) { collectorList ->
            viewModelAdapter.submitList(collectorList)
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

    private fun filterCollectors(query: String) {
        val normalizedQuery = query.normalize()
        val filteredCollectors = viewModel.collectors.value?.filter { collector ->
            collector.name.normalize().contains(normalizedQuery)
        } ?: emptyList()
        val sortedFilterCollectors = filteredCollectors.sortedBy { it.name }
        viewModelAdapter.submitList(sortedFilterCollectors)
        binding.tvNoResults.visibility = if (filteredCollectors.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun String.normalize(): String {
        return Normalizer.normalize(this, Normalizer.Form.NFD)
            .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
            .lowercase(Locale.getDefault())
    }

}
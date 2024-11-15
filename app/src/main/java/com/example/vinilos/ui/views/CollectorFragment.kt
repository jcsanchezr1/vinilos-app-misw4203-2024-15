package com.example.vinilos.ui.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.vinilos.databinding.CollectorFragmentBinding

class CollectorFragment : Fragment() {

    private var _binding: CollectorFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CollectorFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

}
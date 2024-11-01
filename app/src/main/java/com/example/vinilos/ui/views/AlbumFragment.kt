package com.example.vinilos.ui.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.vinilos.R

class AlbumFragment : Fragment() {

    private lateinit var ivLogout: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.album_fragment, container, false)

        ivLogout = view.findViewById(R.id.ivLogout)

        ivLogout.setOnClickListener {
            val intent = Intent(requireActivity(), HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            requireActivity().finish()
        }

        return view
    }
}
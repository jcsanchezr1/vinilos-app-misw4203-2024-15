package com.example.vinilos.ui.views

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.vinilos.R
import com.example.vinilos.common.Constant
import com.example.vinilos.common.UserType
import com.example.vinilos.databinding.ActivityMainBinding
import com.example.vinilos.ui.viewmodels.HeaderViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var headerViewModel: HeaderViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val userTypeValue = intent.getStringExtra(Constant.USER_TYPE) ?: UserType.VISITOR.type

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        headerViewModel = ViewModelProvider(this).get(HeaderViewModel::class.java)
        binding.headerLayout.header = headerViewModel
        binding.headerLayout.lifecycleOwner = this

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.albums -> {
                    headerViewModel.setTitleAndAddButtonVisibility(
                        "Álbumes",
                        UserType.COLLECTOR.type == userTypeValue
                    )
                    loadFragment(AlbumFragment(), userTypeValue)
                    true
                }

                R.id.artists -> {
                    headerViewModel.setTitleAndAddButtonVisibility("Artistas", false)
                    loadFragment(ArtistFragment(), userTypeValue)
                    true
                }

                R.id.collectors -> {
                    headerViewModel.setTitleAndAddButtonVisibility("Coleccionistas", false)
                    loadFragment(CollectorFragment(), userTypeValue)
                    true
                }

                else -> false
            }
        }

        headerViewModel.setTitleAndAddButtonVisibility(
            "Álbumes",
            UserType.COLLECTOR.type == userTypeValue
        )
        loadFragment(AlbumFragment(), userTypeValue)

        binding.headerLayout.ivLogout.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }
    }

    private fun loadFragment(fragment: Fragment, userType: String) {
        val bundle = Bundle()
        bundle.putString(Constant.USER_TYPE, userType)
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .commit()
    }
}
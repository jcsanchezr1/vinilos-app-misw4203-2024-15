package com.example.vinilos.ui.views

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.vinilos.R
import com.example.vinilos.databinding.ActivityMainBinding
import com.example.vinilos.ui.viewmodels.HeaderViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var headerViewModel: HeaderViewModel
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val userType = intent.getStringExtra("user_type")
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        headerViewModel = ViewModelProvider(this).get(HeaderViewModel::class.java)
        binding.headerLayout.header = headerViewModel
        binding.headerLayout.lifecycleOwner = this

        // Configuración del NavController
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.albums -> {
                    headerViewModel.setTitleAndAddButtonVisibility(
                        "Álbumes",
                        userType == "collector"
                    )
                    navController.navigate(R.id.albumFragment)
                    true
                }

                R.id.artists -> {
                    headerViewModel.setTitleAndAddButtonVisibility("Artistas", false)
                    navController.navigate(R.id.artistFragment)
                    true
                }

                R.id.collectors -> {
                    headerViewModel.setTitleAndAddButtonVisibility("Coleccionistas", false)
                    true
                }

                else -> false
            }
        }

        headerViewModel.setTitleAndAddButtonVisibility("Álbumes", userType == "collector")

        binding.headerLayout.ivLogout.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }
    }
}
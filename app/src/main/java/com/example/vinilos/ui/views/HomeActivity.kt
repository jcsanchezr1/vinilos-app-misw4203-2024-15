package com.example.vinilos.ui.views

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.vinilos.R

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val visitorButton = findViewById<Button>(R.id.visitorButton)
        val collectorButton = findViewById<Button>(R.id.collectorButton)

        val intent = Intent(this, MainActivity::class.java)

        visitorButton.setOnClickListener {
            startActivity(intent)
        }

        collectorButton.setOnClickListener {
            startActivity(intent)
        }

    }
}
package com.example.vinilos.ui.views

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.vinilos.R
import com.example.vinilos.common.Constant
import com.example.vinilos.common.UserType

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val visitorButton = findViewById<Button>(R.id.visitorButton)
        val collectorButton = findViewById<Button>(R.id.collectorButton)

        visitorButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(Constant.USER_TYPE, UserType.VISITOR.type)
            startActivity(intent)
        }

        collectorButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(Constant.USER_TYPE, UserType.COLLECTOR.type)
            startActivity(intent)
        }

    }
}
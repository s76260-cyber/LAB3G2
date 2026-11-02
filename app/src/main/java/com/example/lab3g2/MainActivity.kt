package com.example.lab3g2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : LoggingActivity() {
    private var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tvCount = findViewById<TextView>(R.id.tvCount)
        count = savedInstanceState?.getInt("count") ?: 0
        tvCount.text = "Count: $count"
        val btnPlus = findViewById<Button>(R.id.btnPlus)
        val btnDialog = findViewById<Button>(R.id.btnDialog)
        val btnSecondScreen = findViewById<Button>(R.id.btnSecondScreen)
        val btnTransparent = findViewById<Button>(R.id.btnTransparentScreen)
        val btnClose = findViewById<Button>(R.id.btnClose)

        btnPlus.setOnClickListener {
            count++
            tvCount.text = "Count: ${count}"

        }

        btnDialog.setOnClickListener {
            AlertDialog.Builder(this).setTitle("Dialog")
                .setMessage("See Logcat").setPositiveButton("OK", null).show()
        }

        btnSecondScreen.setOnClickListener {
            val intent = Intent(this, secondActivity::class.java)
            startActivity(intent)
        }
        btnTransparent.setOnClickListener {
            val intent = Intent(this, transparentActivity::class.java)
            startActivity(intent)


        }

        btnClose.setOnClickListener {
            finish()
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("count", count)
    }
}
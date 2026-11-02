package com.example.lab3g2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MainActivity : LoggingActivity() {
   // private var count = 0
   private val vm: CounterViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        lifecycle.addObserver(LogObserver())
       // lifecycleScope.launch {
         //   ticker().collect { Log.d("LC_FLOW", "tick $it") }
       // }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                ticker().collect { Log.d("LC_FLOW", "tick $it") }
            }
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        val tvCount = findViewById<TextView>(R.id.tvCount)
        tvCount.text = "Count: ${vm.count}"
        Log.d("LC_PREF", "saved=${getSharedPreferences("lab", MODE_PRIVATE).getInt("saved", -1)}")
      //  count = savedInstanceState?.getInt("count") ?: 0
      // tvCount.text = "Count: $count"
        val btnPlus = findViewById<Button>(R.id.btnPlus)
        val btnDialog = findViewById<Button>(R.id.btnDialog)
        val btnSecondScreen = findViewById<Button>(R.id.btnSecondScreen)
        val btnTransparent = findViewById<Button>(R.id.btnTransparentScreen)
        val btnClose = findViewById<Button>(R.id.btnClose)

       // btnPlus.setOnClickListener {
        //    count++
        //    tvCount.text = "Count: ${count}"

       // }

        btnPlus.setOnClickListener {
            vm.count++
            tvCount.text = "Count: ${vm.count}"
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

   // override fun onSaveInstanceState(outState: Bundle) {
      //  super.onSaveInstanceState(outState)
      //  outState.putInt("count", count)

    private fun ticker() = flow { var i = 0; while (true) { emit(i++); delay(1000) } }

    override fun onStop() {
        super.onStop()
        getSharedPreferences("lab", MODE_PRIVATE).edit().putInt("saved", vm.count).apply()
    }

    }

//class CounterViewModel : ViewModel() {
  //  var count = 0
   // init { Log.d("LC_VM", "ViewModel created") }
   // override fun onCleared() { Log.d("LC_VM", "ViewModel destroyed") }
// }

class CounterViewModel(private val state: SavedStateHandle) : ViewModel() {
    var count: Int
        get() = state["count"] ?: 0
        set(value) { state["count"] = value }

    init { Log.d("LC_VM", "ViewModel created") }
    override fun onCleared() { Log.d("LC_VM", "ViewModel destroyed") }
}

class LogObserver : DefaultLifecycleObserver {
    override fun onStart(owner: LifecycleOwner) { Log.d("LC_OBS", "start") }
    override fun onStop(owner: LifecycleOwner) { Log.d("LC_OBS", "stop") }
}
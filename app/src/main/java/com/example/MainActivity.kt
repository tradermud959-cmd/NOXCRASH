package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.ui.theme.NoxCrashTheme

import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    ProfileManager.init(this)
    MusicPlayerManager.init(this)
    MiningManager.init(this)
    HistoryManager.init(this)
    StatisticsManager.init(this)
    AIManager.init(this)
    
    enableEdgeToEdge()
    
    val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
    windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

    setContent {
      NoxCrashTheme {
        NoxCrashApp()
      }
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    MusicPlayerManager.release()
  }
}

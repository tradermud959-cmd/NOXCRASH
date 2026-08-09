package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.ui.theme.NoxCrashTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    ProfileManager.init(this)
    MusicPlayerManager.init(this)
    MiningManager.init(this)
    enableEdgeToEdge()
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

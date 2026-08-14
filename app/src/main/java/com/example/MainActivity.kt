package com.example

import android.os.Bundle
import android.os.Build
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.Manifest
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
    NoxNotificationManager.init(this)
    ProfileManager.init(this)
    MusicPlayerManager.init(this)
    MiningManager.init(this)
    SupplyManager.init(this)
    HistoryManager.init(this)
    StatisticsManager.init(this)
    AIManager.init(this)
    GachaManager.init(this)
    CheckInManager.init(this)
    NoxNotificationManager.cleanOldNotifications()
    
    enableEdgeToEdge()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }
    
    val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
    windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

    setContent {
      NoxCrashTheme {
        NoxCrashApp()
      }
    }
  }

  override fun onResume() {
    super.onResume()
    NoxNotificationManager.cleanOldNotifications()
  }

  override fun onDestroy() {
    super.onDestroy()
    MusicPlayerManager.release()
  }
}

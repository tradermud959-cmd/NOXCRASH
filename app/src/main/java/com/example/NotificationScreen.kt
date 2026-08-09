package com.example

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.ui.theme.*

@Composable
fun NotificationScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopBarWithBack(title = "NOTIFIKASI", navController = navController, color = ColorNotification)
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            EmptyState(
                icon = "🔔",
                title = "Tidak Ada Notifikasi",
                description = "Belum ada notifikasi baru untuk saat ini."
            )
        }
    }
}

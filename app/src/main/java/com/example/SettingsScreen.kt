package com.example

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.ui.theme.*

@Composable
fun SettingsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopBarWithBack(title = "PENGATURAN", navController = navController, color = ColorSettings)
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            EmptyState(
                icon = "⚙️",
                title = "Pengaturan Sistem",
                description = "Opsi pengaturan akan ditambahkan pada tahap selanjutnya."
            )
        }
    }
}

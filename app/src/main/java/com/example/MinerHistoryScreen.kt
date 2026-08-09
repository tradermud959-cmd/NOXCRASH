package com.example

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.ui.theme.*

@Composable
fun MinerHistoryScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopBarWithBack(title = "RIWAYAT MINER", navController = navController, color = ColorHistory)
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            EmptyState(
                icon = "📜",
                title = "Belum Ada Riwayat",
                description = "Riwayat miner akan muncul setelah sistem mining tersedia."
            )
        }
    }
}

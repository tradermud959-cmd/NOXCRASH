package com.example

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.ui.theme.*

@Composable
fun StatisticsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopBarWithBack(title = "STATISTIK", navController = navController, color = ColorStatistics)
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            EmptyState(
                icon = "📊",
                title = "Statistik Mining",
                description = "Data statistik akan tersedia setelah sistem mining diaktifkan."
            )
        }
    }
}

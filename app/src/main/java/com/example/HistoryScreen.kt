package com.example

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningRed
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(navController: NavController) {
    val historyList by HistoryManager.historyList.collectAsState()

    Scaffold(
        topBar = {
            TopBarWithBack(title = "RIWAYAT", navController = navController, color = TextPrimary)
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        if (historyList.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
            ) {
                Text("Belum ada riwayat aktivitas", color = TextSecondary, fontSize = 16.sp)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(historyList) { item ->
                    HistoryCard(item)
                }
            }
        }
    }
}

@Composable
fun HistoryCard(item: HistoryItem) {
    val strokeColor = when (item.type) {
        HistoryType.MINING -> Color(0xFF06B6D4) // Cyan
        HistoryType.CLAIM -> Color(0xFF64DD17) // Neon Green
        HistoryType.PURCHASE -> Color(0xFFFF9800) // Neon Orange
        HistoryType.WARNING -> WarningRed
    }

    val dateFormat = SimpleDateFormat("dd MMM yyyy • HH:mm", Locale("id", "ID"))
    val dateString = dateFormat.format(Date(item.timestamp))

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF141011)),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, strokeColor.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(item.title, color = strokeColor, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(item.description, color = TextPrimary, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(item.valueLabel, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(dateString, color = TextSecondary, fontSize = 12.sp)
        }
    }
}

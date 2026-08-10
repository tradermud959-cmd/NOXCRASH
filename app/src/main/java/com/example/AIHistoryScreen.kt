package com.example

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AIHistoryScreen(navController: NavController) {
    val aiHistory by AIManager.aiHistory.collectAsState()

    Scaffold(
        topBar = {
            TopBarWithBack(title = "RIWAYAT AI MODE", navController = navController, color = Color(0xFFE040FB))
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        if (aiHistory.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🤖", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Belum ada riwayat penggunaan AI.",
                        color = TextSecondary,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(aiHistory.reversed()) { historyItem ->
                    AIHistoryCard(historyItem)
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
fun AIHistoryCard(item: AIHistoryItem) {
    val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
    val startDateStr = dateFormat.format(Date(item.startedAt))
    val endDateStr = dateFormat.format(Date(item.expiresAt))
    
    val accentColor = when (item.type) {
        AIType.SCOUT -> Color(0xFF00E5FF)
        AIType.SMART -> Color(0xFFE040FB)
        AIType.PRO -> Color(0xFFFF9800)
        AIType.VOID -> Color(0xFF8E24AA)
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ProfileCardBg),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, accentColor.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🤖", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = item.name,
                        color = accentColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
                Text(
                    text = item.status,
                    color = if (item.status == "AKTIF") Color(0xFF64DD17) else TextSecondary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = BorderColor, thickness = 1.dp)
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("DURASI", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text("${item.durationDays} HARI", color = TextPrimary, fontSize = 14.sp)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("HARGA", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text("${item.price.toNXFormat()}", color = TextPrimary, fontSize = 14.sp)
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Column {
                Text("AKTIF:", color = TextSecondary, fontSize = 11.sp)
                Text(startDateStr, color = TextPrimary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text("BERAKHIR:", color = TextSecondary, fontSize = 11.sp)
                Text(endDateStr, color = TextPrimary, fontSize = 12.sp)
            }
        }
    }
}

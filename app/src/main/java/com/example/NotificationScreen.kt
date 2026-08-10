package com.example

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ui.theme.*

@Composable
fun NotificationScreen(navController: NavController) {
    val notifications by NoxNotificationManager.notificationsList.collectAsState()

    Scaffold(
        topBar = {
            TopBarWithBack(title = "NOTIFIKASI", navController = navController, color = Color(0xFFFFD54F))
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        if (notifications.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(Color(0xFF161616), RoundedCornerShape(32.dp))
                            .border(1.dp, Color(0x33FFD54F), RoundedCornerShape(32.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("✨", fontSize = 48.sp)
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = "TIDAK ADA KABAR BARU",
                        color = Color(0xFFFFD54F),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Semua aktivitas NoxCrash akan muncul di sini\nketika ada sesuatu yang perlu kamu ketahui.",
                        color = Color(0xFFAAAAAA),
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(notifications, key = { it.id }) { notif ->
                    NotificationCard(notif)
                }
            }
        }
    }
}

@Composable
fun NotificationCard(notif: NoxNotification) {
    val color = when (notif.type) {
        NoxNotificationType.MINER -> Color(0xFFFFD54F) // Gold/Amber
        NoxNotificationType.AI_MODE -> Color(0xFF00E5FF) // Cyan
        NoxNotificationType.SYSTEM -> Color(0xFFB388FF) // Violet
        NoxNotificationType.WARNING -> Color(0xFFFF1744) // Crimson
    }
    
    val icon = when (notif.type) {
        NoxNotificationType.MINER -> "⛏️"
        NoxNotificationType.AI_MODE -> "🤖"
        NoxNotificationType.SYSTEM -> "⚙️"
        NoxNotificationType.WARNING -> "⚠️"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161616))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, color.copy(alpha = 0.2f), RoundedCornerShape(20.dp))
                .background(Color(0xFF161616))
                .padding(20.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(color.copy(alpha = 0.15f), RoundedCornerShape(16.dp))
                    .border(1.dp, color.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(icon, fontSize = 24.sp)
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = notif.title,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = getTimeAgo(notif.timestamp),
                        color = color.copy(alpha = 0.8f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = notif.description,
                    color = Color(0xFFAAAAAA),
                    fontSize = 13.sp,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

fun getTimeAgo(timeMillis: Long): String {
    val diff = System.currentTimeMillis() - timeMillis
    return when {
        diff < 60 * 1000 -> "Baru saja"
        diff < 60 * 60 * 1000 -> "${diff / (60 * 1000)} menit lalu"
        diff < 24 * 60 * 60 * 1000 -> "${diff / (60 * 60 * 1000)} jam lalu"
        else -> "Kemarin"
    }
}

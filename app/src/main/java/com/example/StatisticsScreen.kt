package com.example

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.ui.theme.BorderColor

@Composable
fun StatisticsScreen(navController: NavController) {
    val totalWallet by ProfileManager.profileData.collectAsState()
    val totalSessions by StatisticsManager.totalSessions.collectAsState()
    val totalReward by StatisticsManager.totalReward.collectAsState()
    val totalPurchases by StatisticsManager.totalPurchases.collectAsState()
    val totalMiningTime by StatisticsManager.totalMiningTime.collectAsState()
    
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopBarWithBack(title = "STATISTICS", navController = navController, color = TextPrimary)
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Text("STATISTIK NOXCRASH", color = TextSecondary, fontSize = 14.sp, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(16.dp))
            
            StatCard(title = "💰 TOTAL WALLET", value = "${totalWallet.balance} NX", description = "Saldo saat ini")
            StatCard(title = "⛏️ TOTAL MINING", value = "$totalSessions", description = "Jumlah session mining")
            StatCard(title = "💎 TOTAL REWARD", value = "${totalReward.toPlainString()} NX", description = "Total NX yang pernah di-claim")
            StatCard(title = "🛒 TOTAL PEMBELIAN MINER", value = "$totalPurchases", description = "Jumlah miner yang pernah dibeli")
            StatCard(title = "⏱️ WAKTU MINING", value = "$totalMiningTime JAM", description = "Total waktu mining")
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun StatCard(title: String, value: String, description: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF141011)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .border(1.dp, BorderColor, RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(title, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(description, color = TextSecondary, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, color = Color(0xFF06B6D4), fontWeight = FontWeight.Bold, fontSize = 24.sp)
        }
    }
}

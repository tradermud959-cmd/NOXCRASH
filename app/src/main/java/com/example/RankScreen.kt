package com.example

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
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
import java.math.BigDecimal

data class RankPlayer(val name: String, val balance: BigDecimal, val isUser: Boolean = false)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RankScreen(onOpenDrawer: () -> Unit) {
    val profileData by ProfileManager.profileData.collectAsState()
    val players by RankManager.topPlayers.collectAsState()
    val userRank by RankManager.userRank.collectAsState()

    // Trigger calculation whenever balance changes
    LaunchedEffect(profileData.balance, profileData.username) {
        RankManager.calculateRank(profileData.balance, profileData.username)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("GLOBAL RANK", color = TextPrimary, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, letterSpacing = 1.sp) },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu", tint = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
            )
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Determine if we need to show the exact player list or inject the user at the bottom
                itemsIndexed(players) { index, player ->
                    RankCard(rank = index + 1, player = player)
                }
                
                // If user is ranked below the explicit list, show them at the very bottom
                val isUserInTopList = players.any { it.isUser }
                if (!isUserInTopList) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "PERINGKAT KAMU",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
                        )
                        
                        val userBalance = try {
                            BigDecimal(profileData.balance.replace(" NX", ""))
                        } catch (e: Exception) {
                            BigDecimal.ZERO
                        }
                        
                        RankCard(rank = userRank, player = RankPlayer(profileData.username, userBalance, true))
                    }
                }
            }
        }
    }
}

@Composable
fun RankCard(rank: Int, player: RankPlayer) {
    val isTop3 = rank <= 3
    val cardColor = if (player.isUser) Color(0xFF1B2C22) else Color(0xFF141011)
    val borderColor = if (player.isUser) Color(0xFF00E676) else if (isTop3) Color(0xFFFFD54F) else BorderColor
    
    val rankIcon = when(rank) {
        1 -> "👑"
        2 -> "🥈"
        3 -> "🥉"
        else -> "#$rank"
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(Color(0xFF161616), RoundedCornerShape(12.dp))
                    .border(1.dp, borderColor.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(rankIcon, fontSize = if (isTop3) 22.sp else 16.sp, color = TextPrimary, fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = player.name,
                    color = if (player.isUser) Color(0xFF00E676) else TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                if (player.isUser) {
                    Text("Itu Kamu!", color = Color(0xFF00E676), fontSize = 12.sp)
                }
            }
            
            Text(
                text = player.balance.toShortNXFormat(),
                color = Color(0xFF06B6D4),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp
            )
        }
    }
}

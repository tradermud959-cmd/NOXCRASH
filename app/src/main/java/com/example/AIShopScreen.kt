package com.example

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.BigDecimal

@Composable
fun AIShopPage(showNotification: (NotificationType, String, String) -> Unit) {
    val profileData by ProfileManager.profileData.collectAsState()
    val aiState by AIManager.aiState.collectAsState()
    val miningStatus by MiningManager.miningStatus.collectAsState()
    
    val onPurchaseClick = { config: AIConfig ->
        if (miningStatus != MiningStatus.OFF) {
            showNotification(
                NotificationType.WARNING,
                "☠️ MINER MASIH AKTIF BJIR",
                "Mining dulu sampai selesai sebelum menggunakan AI Mode."
            )
        } else if (aiState != AIState.NO_AI) {
            showNotification(
                NotificationType.WARNING,
                "⚠️ AI MODE MASIH AKTIF ☠️",
                "Nonaktifkan AI Mode terlebih dahulu sebelum membeli AI baru."
            )
        } else {
            if (AIManager.purchaseAI(config.type, config.name, config.durationDays, config.price)) {
                HistoryManager.addHistory(HistoryType.PURCHASE, "🤖 ${config.name.uppercase()}", "AI Mode dibeli", "-${config.price.toShortNXFormat()}")
                StatisticsManager.addPurchase()
                showNotification(
                    NotificationType.SUCCESS,
                    "🤖 AI DIAKTIFKAN",
                    "${config.name} berhasil dibeli dan mulai beroperasi."
                )
            } else {
                showNotification(
                    NotificationType.ERROR,
                    "☠️ WADUH",
                    "Saldo gak cukup bjir 😹. Mining dulu sana."
                )
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Wallet Balance Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = ProfileCardBg),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFFE040FB).copy(alpha = 0.3f), RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "N",
                        color = Color.Black,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier
                            .background(Color(0xFFE040FB), androidx.compose.foundation.shape.CircleShape)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "SALDO NX: ${profileData.balance}",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Text(
            text = "PILIH AI MODE",
            color = Color(0xFFE040FB),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 8.dp, top = 8.dp)
        )

        // AI Scout
        AICard(
            aiType = AIType.SCOUT,
            name = "AI SCOUT",
            tier = "BASIC AI",
            description = "AI dasar untuk memantau aktivitas mining dan memberikan rekomendasi sederhana.",
            capabilities = listOf("Monitoring miner", "Monitoring saldo NX", "Estimasi pendapatan", "Rekomendasi mining", "Peringatan status mining"),
            durationLabel = "${AIEconomy.scout.durationDays} HARI",
            priceLabel = AIEconomy.scout.priceString,
            accentColor = Color(0xFF00E5FF),
            onActionClick = { onPurchaseClick(AIEconomy.scout) }
        )

        // AI Smart
        AICard(
            aiType = AIType.SMART,
            name = "AI SMART",
            tier = "INTERMEDIATE AI",
            description = "AI pintar yang menganalisis saldo, performa miner, dan memberikan strategi mining yang lebih optimal.",
            capabilities = listOf("Analisis saldo wallet", "Rekomendasi upgrade", "Strategi mining", "+ Semua fitur AI Scout"),
            durationLabel = "${AIEconomy.smart.durationDays} HARI",
            priceLabel = AIEconomy.smart.priceString,
            accentColor = Color(0xFFE040FB),
            onActionClick = { onPurchaseClick(AIEconomy.smart) }
        )

        // AI Pro
        AICard(
            aiType = AIType.PRO,
            name = "AI PRO",
            tier = "ADVANCED AI",
            description = "AI tingkat lanjut untuk mengoptimalkan strategi mining dan mengelola rekomendasi miner secara lebih cerdas.",
            capabilities = listOf("Monitoring otomatis", "Evaluasi miner", "Optimasi strategi", "+ Semua fitur AI Smart"),
            durationLabel = "${AIEconomy.pro.durationDays} HARI",
            priceLabel = AIEconomy.pro.priceString,
            accentColor = Color(0xFFFF9800),
            onActionClick = { onPurchaseClick(AIEconomy.pro) }
        )

        // AI Void
        AICard(
            aiType = AIType.VOID,
            name = "AI VOID",
            tier = "ULTIMATE AI",
            description = "AI otomatisasi tertinggi NoxCrash untuk mengelola siklus mining secara otomatis tanpa intervensi.",
            capabilities = listOf("AUTO CLAIM", "AUTO PURCHASE", "AUTO MANAGEMENT", "+ Semua fitur AI Pro"),
            durationLabel = "${AIEconomy.void.durationDays} HARI",
            priceLabel = AIEconomy.void.priceString,
            accentColor = Color(0xFF8E24AA),
            onActionClick = { onPurchaseClick(AIEconomy.void) },
            isSpecial = true
        )
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun AICard(
    aiType: AIType,
    name: String,
    tier: String,
    description: String,
    capabilities: List<String>,
    durationLabel: String,
    priceLabel: String,
    accentColor: Color,
    onActionClick: () -> Unit,
    isSpecial: Boolean = false
) {
    val bgColor = if (isSpecial) Color(0xFF1A0A24) else ProfileCardBg
    val strokeColor = accentColor.copy(alpha = if (isSpecial) 0.8f else 0.4f)
    
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        modifier = Modifier
            .fillMaxWidth()
            .border(if (isSpecial) 2.dp else 1.dp, strokeColor, RoundedCornerShape(24.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "🤖",
                        fontSize = 24.sp,
                        modifier = Modifier
                            .background(accentColor.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                            .border(1.dp, accentColor.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .padding(8.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = name,
                            color = accentColor,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = tier,
                            color = TextSecondary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = description,
                color = TextPrimary,
                fontSize = 13.sp,
                lineHeight = 20.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "KEMAMPUAN:",
                color = accentColor.copy(alpha = 0.8f),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            capabilities.forEach { cap ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) {
                    Text("•", color = accentColor, fontSize = 14.sp, modifier = Modifier.padding(end = 8.dp))
                    Text(cap, color = TextSecondary, fontSize = 12.sp)
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = BorderColor, thickness = 1.dp)
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "HARGA", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = java.math.BigDecimal(priceLabel).toShortNXFormat(),
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "MASA AKTIF", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = durationLabel,
                        color = accentColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            var isProcessing by remember { mutableStateOf(false) }
            val coroutineScope = rememberCoroutineScope()
            
            Button(
                onClick = {
                    if (!isProcessing) {
                        isProcessing = true
                        onActionClick()
                        coroutineScope.launch {
                            delay(500) // Prevent double click
                            isProcessing = false
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "AKTIFKAN SEKARANG",
                    color = if (isSpecial) Color.White else Color(0xFF111111),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

package com.example

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ui.theme.*

@Composable
fun GachaPullScreen(navController: NavController) {
    var animationState by remember { mutableStateOf(0) } // 0: Start, 1: Animating, 2: Result
    var gachaResult by remember { mutableStateOf<GachaResult?>(null) }
    var compensationMsg by remember { mutableStateOf<String?>(null) }
    
    val coroutineScope = rememberCoroutineScope()
    val scale = remember { Animatable(1f) }
    
    LaunchedEffect(animationState) {
        if (animationState == 1) {
            // Animating phase
            for (i in 0..5) {
                scale.animateTo(1.2f, animationSpec = tween(150, easing = LinearEasing))
                scale.animateTo(0.9f, animationSpec = tween(150, easing = LinearEasing))
            }
            scale.animateTo(1f, animationSpec = tween(200))
            
            // Generate Result
            gachaResult = GachaManager.pullGacha()
            
            // Give Rewards
            when (gachaResult?.type) {
                GachaResultType.CONSOLATION -> {
                    gachaResult?.rewardAmount?.let { 
                        ProfileManager.addBalance(it)
                        HistoryManager.addHistory(HistoryType.CLAIM, "🎁 DAILY DROP", "Hadiah hiburan", "+${it.toNXFormat()}")
                    }
                }
                GachaResultType.MINER_LOW, GachaResultType.MINER_MID, GachaResultType.MINER_HIGH -> {
                    gachaResult?.minerId?.let { minerId -> 
                        val name = NoxEconomyConfig.getMinerName(minerId)
                        val price = NoxEconomyConfig.getMinerPrice(minerId)
                        
                        if (MiningManager.miningStatus.value == MiningStatus.OFF) {
                            MiningManager.startMining(
                                id = minerId,
                                name = name,
                                targetReward = NoxEconomyConfig.getTargetRewardForMiner(minerId)
                            )
                            HistoryManager.addHistory(HistoryType.CLAIM, "🎁 $name", "Didapatkan dari Daily Drop", "GRATIS")
                        } else {
                            // Slot penuh, konversi
                            ProfileManager.addBalance(price)
                            compensationMsg = "Slot mining kamu sedang penuh. Miner dikonversi menjadi ${price.toShortNXFormat()}."
                            HistoryManager.addHistory(HistoryType.CLAIM, "🎁 KONVERSI MINER", "Gacha: $name", "+${price.toNXFormat()}")
                        }
                    }
                }
                else -> {
                    HistoryManager.addHistory(HistoryType.WARNING, "🎁 DAILY DROP", "Sistem gagal menemukan Miner", "ZONK")
                }
            }
            
            animationState = 2
        }
    }
    
    Scaffold(
        topBar = {
            TopBarWithBack(title = "DAILY DROP", navController = navController, color = Color(0xFF00E676))
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (animationState) {
                0 -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("📦", fontSize = 100.sp)
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        if (GachaManager.canPullGacha()) {
                            Button(
                                onClick = { animationState = 1 },
                                modifier = Modifier
                                    .fillMaxWidth(0.8f)
                                    .height(56.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E676)),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text("BUKA SEKARANG (GRATIS)", color = Color.Black, fontWeight = FontWeight.ExtraBold)
                            }
                        } else {
                            val remainingMs = GachaManager.getRemainingTimeMs()
                            val hours = remainingMs / (1000 * 60 * 60)
                            val minutes = (remainingMs / (1000 * 60)) % 60
                            
                            Button(
                                onClick = { },
                                modifier = Modifier
                                    .fillMaxWidth(0.8f)
                                    .height(56.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF424242)),
                                shape = RoundedCornerShape(16.dp),
                                enabled = false
                            ) {
                                Text("TERSEDIA DALAM ${hours}J ${minutes}M", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
                1 -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "📦", 
                            fontSize = 120.sp,
                            modifier = Modifier.scale(scale.value)
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        Text(
                            "MEMBUKA KOORDINAT...",
                            color = Color(0xFF00E676),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                    }
                }
                2 -> {
                    val result = gachaResult!!
                    
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        when (result.type) {
                            GachaResultType.ZONK -> {
                                Text("🪫", fontSize = 100.sp)
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("SISTEM GAGAL", color = WarningRed, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "Mesin pencari tidak menemukan Miner apa pun di koordinat ini. Coba lagi besok!",
                                    color = TextPrimary,
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                            GachaResultType.CONSOLATION -> {
                                Text("🪙", fontSize = 100.sp)
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("LUMAYAN!", color = ColorShop, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "Kamu mendapatkan hadiah hiburan sebesar ${result.rewardAmount?.toShortNXFormat()}.",
                                    color = TextPrimary,
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                            else -> {
                                Text("🎉", fontSize = 100.sp)
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("JACKPOT!", color = Color(0xFF00E676), fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "Kamu berhasil menemukan:\n${NoxEconomyConfig.getMinerName(result.minerId!!)}",
                                    color = TextPrimary,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                if (compensationMsg != null) {
                                    Text(
                                        compensationMsg!!,
                                        color = ColorShop,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        textAlign = TextAlign.Center
                                    )
                                } else {
                                    Text(
                                        "Miner telah aktif dan otomatis memulai proses mining NX.",
                                        color = TextSecondary,
                                        fontSize = 13.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(48.dp))
                        
                        Button(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C30)),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("TUTUP", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

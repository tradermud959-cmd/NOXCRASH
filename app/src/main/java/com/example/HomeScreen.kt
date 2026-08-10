package com.example

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.navigation.NavController
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Stop
import androidx.compose.ui.graphics.Brush
import com.example.ui.theme.*
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            MiningCard()
        }
        item {
            AIModeCard(navController)
        }
        item {
            ProfileCard()
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    ActionCard(
                        title = "💥 CRASH",
                        subtitle = "SEGERA HADIR",
                        color = ColorCrash,
                        backgroundColor = CrashCardBg,
                        borderColor = Color(0x33EF4444),
                        onClick = {
                            Toast.makeText(context, "Fitur Crash sedang dalam pengembangan.", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    ActionCard(
                        title = "🛒 SHOP",
                        subtitle = "MINER MARKET",
                        color = ColorShop,
                        backgroundColor = ShopCardBg,
                        borderColor = Color(0x33F59E0B),
                        onClick = {
                            navController.navigate("shop")
                        }
                    )
                }
            }
        }
        item {
            MusicPlayerCard(navController)
        }
        item {
            ActiveMinersCard()
        }
    }
}

@Composable
fun MiningCard() {
    val miningStatus by MiningManager.miningStatus.collectAsState()
    val activeMiner by MiningManager.activeMiner.collectAsState()
    var currentTime by remember { mutableLongStateOf(System.currentTimeMillis()) }

    LaunchedEffect(miningStatus) {
        if (miningStatus == MiningStatus.ACTIVE) {
            while (true) {
                delay(50)
                currentTime = System.currentTimeMillis()
                if (currentTime % 1000 < 50) MiningManager.refreshState()
            }
        }
    }

    val pickaxeRotation = remember { androidx.compose.animation.core.Animatable(0f) }
    LaunchedEffect(miningStatus) {
        if (miningStatus == MiningStatus.ACTIVE) {
            while (true) {
                pickaxeRotation.animateTo(
                    targetValue = 30f,
                    animationSpec = androidx.compose.animation.core.tween(500)
                )
                pickaxeRotation.animateTo(
                    targetValue = -10f,
                    animationSpec = androidx.compose.animation.core.tween(500)
                )
            }
        } else {
            pickaxeRotation.snapTo(0f)
        }
    }

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = ActiveMinersCardBg),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0x3364DD17), RoundedCornerShape(24.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "⛏️",
                fontSize = 48.sp,
                modifier = Modifier.graphicsLayer { rotationZ = pickaxeRotation.value }
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            if (miningStatus == MiningStatus.ACTIVE && activeMiner != null) {
                Text(
                    text = "SEDANG MINING",
                    color = ColorMining,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                val miner = activeMiner!!
                val totalDurationMs = miner.durationHours * 60 * 60 * 1000
                val elapsedMs = (currentTime - miner.startedAt).coerceAtLeast(0).coerceAtMost(totalDurationMs)
                
                val currentReward = miner.reward.multiply(java.math.BigDecimal(elapsedMs)).divide(java.math.BigDecimal(totalDurationMs), 15, java.math.RoundingMode.DOWN)
                val currentRewardString = currentReward.toNXFormat()
                
                Text(
                    text = currentRewardString,
                    maxLines = 1,
                    softWrap = false,
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                val remainingMs = (miner.endsAt - currentTime).coerceAtLeast(0)
                val remainingSeconds = remainingMs / 1000
                val hours = remainingSeconds / 3600
                val minutes = (remainingSeconds % 3600) / 60
                val seconds = remainingSeconds % 60
                val timeString = java.lang.String.format("%02d:%02d:%02d", hours, minutes, seconds)
                Text(
                    text = "Sisa Waktu: $timeString",
                    color = TextSecondary,
                    fontSize = 14.sp
                )
                
                Text(
                    text = "Estimasi Pendapatan: ${miner.reward.toNXFormat()}",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            } else if (miningStatus == MiningStatus.COMPLETED) {
                Text(
                    text = "MINING SELESAI!",
                    color = Color(0xFF64DD17),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Silakan claim reward Anda.",
                    color = TextSecondary,
                    fontSize = 14.sp
                )
            } else {
                Text(
                    text = "NOX MINER",
                    color = ColorMining,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Tidak ada aktivitas mining.",
                    color = TextSecondary,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun AIModeCard(navController: NavController) {
    val aiState by AIManager.aiState.collectAsState()
    var currentTime by remember { mutableLongStateOf(System.currentTimeMillis()) }

    LaunchedEffect(aiState) {
        if (aiState != AIState.NO_AI) {
            while (true) {
                delay(1000)
                currentTime = System.currentTimeMillis()
                AIManager.refreshState()
            }
        }
    }

    val (accentColor, title, tier) = when (aiState) {
        AIState.NO_AI -> Triple(Color(0xFF888888), "AI MODE", "TIDAK AKTIF")
        AIState.AI_SCOUT_ACTIVE -> Triple(Color(0xFF00E5FF), "AI SCOUT", "BASIC AI")
        AIState.AI_SMART_ACTIVE -> Triple(Color(0xFFE040FB), "AI SMART", "INTERMEDIATE AI")
        AIState.AI_PRO_ACTIVE -> Triple(Color(0xFFFF9800), "AI PRO", "ADVANCED AI")
        AIState.AI_VOID_ACTIVE -> Triple(Color(0xFF8E24AA), "AI VOID", "ULTIMATE AI")
    }

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = ProfileCardBg),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, accentColor.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
            .clickable { navController.navigate("shop") }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (aiState != AIState.NO_AI) "☠" else "🤖",
                        fontSize = 20.sp,
                        color = accentColor
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = title,
                        color = accentColor,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        letterSpacing = 1.sp
                    )
                }
                Text(
                    text = tier,
                    color = TextSecondary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (aiState == AIState.NO_AI) {
                Text(
                    text = "Pilih AI Mode untuk mendapatkan otomatisasi dan strategi mining sesuai tier.",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "BUKA MINER SHOP ➔",
                    color = accentColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            } else {
                Text(
                    text = "AUTO MODE ACTIVE",
                    color = Color(0xFF64DD17),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 12.sp,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                val expiresAt = AIManager.activeAI.value?.expiresAt ?: 0L
                val remainingMs = (expiresAt - currentTime).coerceAtLeast(0)
                val remainingSeconds = remainingMs / 1000
                val days = remainingSeconds / (24 * 3600)
                val hours = (remainingSeconds % (24 * 3600)) / 3600
                val minutes = (remainingSeconds % 3600) / 60
                val seconds = remainingSeconds % 60
                val timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds)

                Text(
                    text = "$days HARI $timeString TERSISA",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                if (aiState == AIState.AI_VOID_ACTIVE) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row { Text("AUTO CLAIM", color = TextSecondary, fontSize = 11.sp, modifier = Modifier.weight(1f)); Text("✓", color = accentColor, fontSize = 11.sp) }
                        Row { Text("AUTO PURCHASE", color = TextSecondary, fontSize = 11.sp, modifier = Modifier.weight(1f)); Text("✓", color = accentColor, fontSize = 11.sp) }
                        Row { Text("AUTO MANAGEMENT", color = TextSecondary, fontSize = 11.sp, modifier = Modifier.weight(1f)); Text("✓", color = accentColor, fontSize = 11.sp) }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileCard() {
    val profileData by ProfileManager.profileData.collectAsState()

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = ProfileCardBg),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0x1AFFFFFF), RoundedCornerShape(24.dp))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f/9f)
            ) {
                coil.compose.AsyncImage(
                    model = profileData.coverUri,
                    contentDescription = "Profile Banner",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, ProfileCardBg)
                            )
                        )
                )
            }
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .offset(y = (-32).dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(DarkBackground)
                        .border(2.dp, ColorProfile, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    coil.compose.AsyncImage(
                        model = profileData.photoUri,
                        contentDescription = "Profile Avatar",
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column(modifier = Modifier.padding(bottom = 8.dp)) {
                    Text(
                        text = profileData.username,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "Miner Level 1",
                        color = ColorProfile,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 16.dp)
                    .offset(y = (-16).dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "SALDO NX",
                        color = TextSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = profileData.balance,
                        maxLines = 1,
                        softWrap = false,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
                
                Box(
                    modifier = Modifier
                        .background(ColorProfile.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "RANK #100",
                        color = ColorProfile,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ActionCard(
    title: String,
    subtitle: String,
    color: Color,
    backgroundColor: Color,
    borderColor: Color,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .border(1.dp, borderColor, RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                    .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (title.contains("SHOP")) {
                    Text("🛒", fontSize = 20.sp)
                } else {
                    Text("💥", fontSize = 20.sp)
                }
            }
            
            Column {
                Text(
                    text = title,
                    color = color,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    color = TextSecondary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}

@Composable
fun MusicPlayerCard(navController: NavController) {
    val isPlaying by MusicPlayerManager.isPlaying.collectAsState()
    val currentSong by MusicPlayerManager.currentSong.collectAsState()
    val isRepeat by MusicPlayerManager.isRepeat.collectAsState()
    val context = LocalContext.current

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MusicSurface),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0x33D500F9), RoundedCornerShape(24.dp))
            .clickable { navController.navigate("musik") }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🎵 MUSIC PLAYER",
                    color = ColorMusicAccent,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    letterSpacing = 1.sp
                )
                if (isPlaying) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(ColorMusicAccent, CircleShape)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = currentSong?.title ?: "No Music Playing",
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = currentSong?.duration ?: "--:--",
                color = TextSecondary,
                fontSize = 12.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    IconButton(
                        onClick = { MusicPlayerManager.togglePlayPause(context) },
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0x1AD500F9), CircleShape)
                    ) {
                        Text(if (isPlaying) "⏸" else "▶", color = ColorMusicAccent, fontSize = 18.sp)
                    }
                    IconButton(
                        onClick = { MusicPlayerManager.stop() },
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0x1AD500F9), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Stop,
                            contentDescription = "Stop",
                            tint = ColorMusicAccent
                        )
                    }
                }
                
                IconButton(
                    onClick = { MusicPlayerManager.toggleRepeat() },
                    modifier = Modifier
                        .size(40.dp)
                        .background(if (isRepeat) Color(0x4DD500F9) else Color(0x1AD500F9), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Repeat",
                        tint = if (isRepeat) TextPrimary else ColorMusicAccent
                    )
                }
            }
        }
    }
}

@Composable
fun ActiveMinersCard() {
    val miningStatus by MiningManager.miningStatus.collectAsState()
    val activeMiner by MiningManager.activeMiner.collectAsState()
    var currentTime by remember { mutableLongStateOf(System.currentTimeMillis()) }
    val coroutineScope = rememberCoroutineScope()
    var isClaiming by remember { mutableStateOf(false) }

    LaunchedEffect(miningStatus) {
        if (miningStatus == MiningStatus.ACTIVE) {
            while (true) {
                delay(1000)
                currentTime = System.currentTimeMillis()
            }
        }
    }

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = ActiveMinersCardBg),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0x0DFFFFFF), RoundedCornerShape(24.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "⛏️ MINER YANG AKTIF",
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(24.dp))
            
            if (miningStatus == MiningStatus.OFF || activeMiner == null) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Belum ada miner yang aktif.", color = TextSecondary, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Pergi ke Miner Shop untuk membeli miner.",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                val miner = activeMiner!!
                val isCompleted = miningStatus == MiningStatus.COMPLETED
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    coil.compose.AsyncImage(
                        model = getMinerIconPath(miner.id),
                        contentDescription = miner.name,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = miner.name,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = if (isCompleted) "Status: MINING SELESAI" else "Status: MINING",
                    color = if (isCompleted) Color(0xFF64DD17) else ColorMining,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Reward: ${miner.reward.toNXFormat()} / ${miner.durationHours} JAM",
                    color = TextSecondary,
                    fontSize = 14.sp
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                if (isCompleted) {
                    Button(
                        onClick = {
                            if (!isClaiming) {
                                isClaiming = true
                                MiningManager.claimReward()
                                coroutineScope.launch {
                                    delay(500)
                                    isClaiming = false
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF64DD17)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("CLAIM ${miner.reward.toNXFormat()}", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                } else {
                    val totalDurationMs = miner.durationHours * 60 * 60 * 1000
                    val elapsedMs = currentTime - miner.startedAt
                    val remainingMs = miner.endsAt - currentTime
                    val progress = (elapsedMs.toFloat() / totalDurationMs.toFloat()).coerceIn(0f, 1f)
                    
                    val remainingSeconds = (remainingMs / 1000).coerceAtLeast(0)
                    val hours = remainingSeconds / 3600
                    val minutes = (remainingSeconds % 3600) / 60
                    val seconds = remainingSeconds % 60
                    val timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                    
                    Text("Progress: ${(progress * 100).toInt()}%", color = TextSecondary, fontSize = 14.sp)
                    Text("Sisa: $timeString", color = TextSecondary, fontSize = 14.sp)
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .background(DarkSurfaceVariant, RoundedCornerShape(4.dp))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(progress)
                                .height(8.dp)
                                .background(ColorMining, RoundedCornerShape(4.dp))
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

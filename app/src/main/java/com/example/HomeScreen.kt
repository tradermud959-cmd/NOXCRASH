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
                delay(1000)
                currentTime = System.currentTimeMillis()
                MiningManager.refreshState()
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

    val gradientBrush = androidx.compose.ui.graphics.Brush.linearGradient(
        colors = listOf(MiningCardStart, MiningCardEnd)
    )
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        modifier = Modifier
            .fillMaxWidth()
            .background(gradientBrush, RoundedCornerShape(24.dp))
            .border(1.dp, Color(0x3306B6D4), RoundedCornerShape(24.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "⛏️",
                    fontSize = 18.sp,
                    modifier = Modifier.graphicsLayer(rotationZ = pickaxeRotation.value)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (miningStatus == MiningStatus.ACTIVE) activeMiner?.name?.uppercase() ?: "MINING ACTIVE" else "MINING OFF",
                    color = ColorMining,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    letterSpacing = 1.sp
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (miningStatus == MiningStatus.ACTIVE && activeMiner != null) {
                val miner = activeMiner!!
                val totalDurationMs = miner.durationHours * 60 * 60 * 1000
                val elapsedMs = currentTime - miner.startedAt
                val remainingMs = miner.endsAt - currentTime
                val progress = (elapsedMs.toFloat() / totalDurationMs.toFloat()).coerceIn(0f, 1f)
                
                val remainingSeconds = (remainingMs / 1000).coerceAtLeast(0)
                val hours = remainingSeconds / 3600
                val minutes = (remainingSeconds % 3600) / 60
                val seconds = remainingSeconds % 60
                val timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                
                val currentReward = (miner.reward.toFloat() * progress).toBigDecimal()
                
                Text(
                    text = String.format(java.util.Locale.US, "%.8f NX", currentReward),
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${miner.reward.toPlainString()} NX / ${miner.durationHours} JAM",
                    color = ColorMining.copy(alpha = 0.8f),
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .background(DarkSurfaceVariant, RoundedCornerShape(2.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress)
                            .height(4.dp)
                            .background(ColorMining, RoundedCornerShape(2.dp))
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("$timeString tersisa", color = TextSecondary, fontSize = 12.sp)
                    Text("${(progress * 100).toInt()}%", color = TextSecondary, fontSize = 12.sp)
                }
            } else if (miningStatus == MiningStatus.COMPLETED) {
                Text(
                    text = "MINING SELESAI",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${activeMiner?.reward?.toPlainString() ?: "0"} NX SIAP DIKLAIM",
                    color = ColorMining,
                    fontSize = 14.sp
                )
            } else {
                Text(
                    text = "0.00000000 NX",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "0 NX/JAM",
                    color = ColorMining.copy(alpha = 0.8f),
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .background(DarkSurfaceVariant, RoundedCornerShape(2.dp))
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Beli miner di Shop untuk memulai", color = TextSecondary, fontSize = 12.sp)
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
            .border(1.dp, Color(0x338B5CF6), RoundedCornerShape(24.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(androidx.compose.foundation.shape.CircleShape)
                    .background(Color(0x1A8B5CF6))
                    .border(1.dp, Color(0x4D8B5CF6), androidx.compose.foundation.shape.CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (profileData.photoUri != null) {
                    coil.compose.AsyncImage(
                        model = java.io.File(profileData.photoUri!!),
                        contentDescription = "Profile Photo",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                    )
                } else {
                    Text("👤", fontSize = 24.sp)
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("PROFIL", color = ColorProfile, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text(profileData.username, color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Text("Level --", color = TextSecondary, fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("-- NX", color = TextSecondary, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun ActionCard(title: String, subtitle: String, color: Color, backgroundColor: Color, borderColor: Color, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, borderColor, RoundedCornerShape(24.dp))
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                color = color,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = subtitle,
                color = TextSecondary,
                fontSize = 10.sp,
                letterSpacing = 1.sp
            )
        }
    }
}

@Composable
fun MusicPlayerCard(navController: NavController) {
    val currentSong by MusicPlayerManager.currentSong.collectAsState()
    val isPlaying by MusicPlayerManager.isPlaying.collectAsState()
    val isRepeat by MusicPlayerManager.isRepeat.collectAsState()
    val context = LocalContext.current
    
    val gradientBrush = Brush.linearGradient(
        colors = listOf(MusicCardStart, MusicCardEnd)
    )
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        modifier = Modifier
            .fillMaxWidth()
            .background(gradientBrush, RoundedCornerShape(24.dp))
            .border(1.dp, Color(0x33D500F9), RoundedCornerShape(24.dp))
            .clickable { navController.navigate("musik") }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🎵", fontSize = 18.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "MUSIC PLAYER",
                    color = ColorMusicAccent,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    letterSpacing = 1.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = currentSong?.title ?: "Belum ada musik",
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
                        // Using a simple text icon for Play/Pause if Icons.Default.Pause is missing, or just check if it's there
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
                    Text(
                        text = "⛏️ ${miner.name}",
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
                    text = "Reward: ${miner.reward.toPlainString()} NX / ${miner.durationHours} JAM",
                    color = TextSecondary,
                    fontSize = 14.sp
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                if (isCompleted) {
                    Button(
                        onClick = {
                            if (!isClaiming) {
                                isClaiming = true
                                val success = MiningManager.claimReward()
                                // No snackbar host easily available here without hoist, but balance updates instantly
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
                        Text("CLAIM ${miner.reward.toPlainString()} NX", color = Color.Black, fontWeight = FontWeight.Bold)
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

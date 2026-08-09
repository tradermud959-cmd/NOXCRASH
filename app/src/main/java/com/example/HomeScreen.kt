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
                        subtitle = "SEGERA HADIR",
                        color = ColorShop,
                        backgroundColor = ShopCardBg,
                        borderColor = Color(0x33F59E0B),
                        onClick = {
                            Toast.makeText(context, "Miner Shop sedang dalam pengembangan.", Toast.LENGTH_SHORT).show()
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
            Text(
                text = "⛏️ MINING",
                color = ColorMining,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "0.00000000 NX",
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "⚡ 0.000000000 NX/s",
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
                        .fillMaxWidth(0.3f)
                        .height(4.dp)
                        .background(ColorMining, RoundedCornerShape(2.dp))
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("MINING PREVIEW", color = TextSecondary, fontSize = 12.sp)
                Text("24 JAM", color = TextSecondary, fontSize = 12.sp)
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
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Belum ada miner yang aktif.", color = TextSecondary, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Sistem mining akan tersedia pada pengembangan berikutnya.",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

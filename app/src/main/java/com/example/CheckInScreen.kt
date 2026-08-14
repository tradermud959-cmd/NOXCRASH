package com.example

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CheckInScreen(navController: NavController) {
    val canCheckIn by CheckInManager.canCheckInToday.collectAsState()
    val currentDayToClaim by CheckInManager.currentDayToClaim.collectAsState()
    val currentStreak by CheckInManager.currentStreak.collectAsState()
    
    var showSuccess by remember { mutableStateOf(false) }
    var claimedAmount by remember { mutableStateOf("") }
    
    val coroutineScope = rememberCoroutineScope()
    
    LaunchedEffect(Unit) {
        CheckInManager.refreshState()
    }

    Scaffold(
        topBar = {
            TopBarWithBack(title = "DAILY CHECK-IN", navController = navController, color = Color(0xFF00E676))
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("📅", fontSize = 64.sp)
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "HADIAH HARIAN",
                    color = Color(0xFF00E676),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Login setiap hari tanpa terputus untuk mendapatkan bonus NX. Streak akan kembali ke Hari 1 jika kamu terlewat 1 hari.",
                    color = TextSecondary,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Days Grid
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Row 1: Days 1-4
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (i in 1..4) {
                            DayCard(
                                day = i,
                                reward = CheckInManager.rewards[i-1].toShortNXFormat(),
                                status = getDayStatus(i, currentDayToClaim, canCheckIn, currentStreak),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Row 2: Days 5-7
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (i in 5..6) {
                            DayCard(
                                day = i,
                                reward = CheckInManager.rewards[i-1].toShortNXFormat(),
                                status = getDayStatus(i, currentDayToClaim, canCheckIn, currentStreak),
                                modifier = Modifier.weight(1f)
                            )
                        }
                        
                        // Day 7 (Jackpot)
                        DayCard(
                            day = 7,
                            reward = CheckInManager.rewards[6].toShortNXFormat(),
                            status = getDayStatus(7, currentDayToClaim, canCheckIn, currentStreak),
                            modifier = Modifier.weight(2f),
                            isJackpot = true
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(48.dp))
                
                if (showSuccess) {
                    Text(
                        text = "Berhasil Klaim +$claimedAmount!",
                        color = Color(0xFF00E676),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                } else if (canCheckIn) {
                    Button(
                        onClick = {
                            val reward = CheckInManager.claimCheckIn()
                            if (reward != null) {
                                claimedAmount = reward.toShortNXFormat()
                                showSuccess = true
                                coroutineScope.launch {
                                    delay(3000)
                                    showSuccess = false
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E676)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("KLAIM HARI INI", color = Color.Black, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                    }
                } else {
                    Button(
                        onClick = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF424242)),
                        shape = RoundedCornerShape(16.dp),
                        enabled = false
                    ) {
                        Text("SUDAH DIKLAIM", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

enum class DayStatus { CLAIMED, TODAY, LOCKED }

fun getDayStatus(day: Int, currentDayToClaim: Int, canCheckIn: Boolean, currentStreak: Int): DayStatus {
    return when {
        day < currentDayToClaim -> DayStatus.CLAIMED
        day == currentDayToClaim -> if (canCheckIn) DayStatus.TODAY else DayStatus.CLAIMED
        else -> DayStatus.LOCKED
    }
}

@Composable
fun DayCard(
    day: Int,
    reward: String,
    status: DayStatus,
    modifier: Modifier = Modifier,
    isJackpot: Boolean = false
) {
    val bgColor = when (status) {
        DayStatus.CLAIMED -> Color(0xFF1B2C22)
        DayStatus.TODAY -> Color(0xFF00E676).copy(alpha = 0.2f)
        DayStatus.LOCKED -> Color(0xFF1B0B0E)
    }
    
    val borderColor = when (status) {
        DayStatus.CLAIMED -> Color(0xFF00E676).copy(alpha = 0.5f)
        DayStatus.TODAY -> Color(0xFF00E676)
        DayStatus.LOCKED -> Color.DarkGray
    }
    
    val textColor = when (status) {
        DayStatus.CLAIMED -> Color(0xFF00E676)
        DayStatus.TODAY -> Color(0xFF00E676)
        DayStatus.LOCKED -> TextSecondary
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        modifier = modifier
            .aspectRatio(if (isJackpot) 2f else 1f)
            .border(if (status == DayStatus.TODAY) 2.dp else 1.dp, borderColor, RoundedCornerShape(12.dp))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (status == DayStatus.CLAIMED) {
                Text("✅", fontSize = if (isJackpot) 24.sp else 20.sp)
            } else {
                Text(if (isJackpot) "🎁" else "🪙", fontSize = if (isJackpot) 24.sp else 20.sp)
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = "HARI $day",
                color = textColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(2.dp))
            
            Text(
                text = reward,
                color = if (status == DayStatus.LOCKED) Color.Gray else TextPrimary,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

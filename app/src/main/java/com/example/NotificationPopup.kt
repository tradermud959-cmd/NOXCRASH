package com.example

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun NotificationPopupOverlay(
    onNavigateToNotification: () -> Unit
) {
    val newNotification by NoxNotificationManager.newNotificationFlow.collectAsState(initial = null)
    var currentPopup by remember { mutableStateOf<NoxNotification?>(null) }
    val lifecycleOwner = LocalLifecycleOwner.current
    
    // When a new notification arrives, show it for exactly 4 seconds of RESUMED time
    LaunchedEffect(newNotification, lifecycleOwner) {
        newNotification?.let { notif ->
            currentPopup = notif
            lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                delay(4000)
                if (currentPopup == notif) {
                    currentPopup = null
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            .statusBarsPadding(),
        contentAlignment = Alignment.TopCenter
    ) {
        AnimatedVisibility(
            visible = currentPopup != null,
            enter = slideInVertically(
                initialOffsetY = { -it - 50 },
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
            ) + fadeIn(animationSpec = tween(300)),
            exit = slideOutVertically(
                targetOffsetY = { -it - 50 },
                animationSpec = tween(300, easing = FastOutSlowInEasing)
            ) + fadeOut(animationSpec = tween(300))
        ) {
            currentPopup?.let { notif ->
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
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .shadow(16.dp, RoundedCornerShape(24.dp))
                        .clip(RoundedCornerShape(24.dp))
                        .clickable {
                            currentPopup = null
                            onNavigateToNotification()
                        },
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF161616))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(24.dp))
                            .background(Color(0xFF161616))
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(color.copy(alpha = 0.15f), RoundedCornerShape(16.dp))
                                .border(1.dp, color.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(icon, fontSize = 24.sp)
                        }
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "ADA KABAR BARU",
                                color = color,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = notif.title,
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = notif.description,
                                color = Color(0xFFAAAAAA),
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

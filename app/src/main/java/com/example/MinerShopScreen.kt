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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.BigDecimal

enum class NotificationType { ERROR, WARNING, SUCCESS }

data class ShopNotification(
    val type: NotificationType,
    val title: String,
    val message: String
)

@Composable
fun MinerShopScreen(navController: NavController) {
    val profileData by ProfileManager.profileData.collectAsState()
    val miningStatus by MiningManager.miningStatus.collectAsState()
    val activeMiner by MiningManager.activeMiner.collectAsState()
    val lastFreeMinerUsedAt by MiningManager.lastFreeMinerUsedAt.collectAsState()
    
    var currentNotification by remember { mutableStateOf<ShopNotification?>(null) }
    var notificationId by remember { mutableStateOf(0) }
    
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        MiningManager.refreshState()
    }
    
    LaunchedEffect(notificationId) {
        if (currentNotification != null) {
            delay(3000)
            currentNotification = null
        }
    }
    
    fun showNotification(type: NotificationType, title: String, message: String) {
        currentNotification = ShopNotification(type, title, message)
        notificationId++
    }

    val onPurchaseClick = { price: String, minerId: String, minerName: String, reward: String ->
        if (miningStatus != MiningStatus.OFF) {
            showNotification(
                type = NotificationType.WARNING,
                title = "⚠️ MINER MASIH AKTIF",
                message = "Miner masih aktif. Tunggu sampai mining selesai."
            )
        } else {
            val priceDec = BigDecimal(price)
            val success = ProfileManager.updateBalance(priceDec)
            if (success) {
                HistoryManager.addHistory(HistoryType.PURCHASE, "🛒 ${minerName.uppercase()}", "Miner dibeli", "-$price NX")
                StatisticsManager.addPurchase()
                
                val rewardDec = BigDecimal(reward)
                MiningManager.startMining(minerId, minerName, rewardDec, 24)
                showNotification(
                    type = NotificationType.SUCCESS,
                    title = "⛏️ MINER AKTIF",
                    message = "$minerName berhasil dibeli dan mulai mining."
                )
            } else {
                showNotification(
                    type = NotificationType.ERROR,
                    title = "☠️ WADUH",
                    message = "Gak cukup bjir, mining dulu sana yang free\uD83D\uDE39"
                )
            }
        }
    }
    
    val onFreeMinerClick: () -> Unit = {
        if (miningStatus != MiningStatus.OFF) {
            showNotification(
                type = NotificationType.WARNING,
                title = "⚠️ MINER MASIH AKTIF",
                message = "Miner masih aktif. Tunggu sampai mining selesai."
            )
        } else {
            val currentTime = System.currentTimeMillis()
            val cooldownMs = 2L * 24 * 60 * 60 * 1000 // 2 days
            if (currentTime - lastFreeMinerUsedAt >= cooldownMs) {
                MiningManager.startMining("free_miner", "Free Miner", BigDecimal("2"), 24)
                showNotification(
                    type = NotificationType.SUCCESS,
                    title = "⛏️ MINING DIMULAI",
                    message = "Free Miner berhasil diaktifkan."
                )
            } else {
                showNotification(
                    type = NotificationType.WARNING,
                    title = "⚠️ COOLDOWN",
                    message = "Free Miner masih dalam cooldown."
                )
            }
        }
    }

    Scaffold(
        topBar = {
            TopBarWithBack(title = "MINER SHOP", navController = navController, color = ColorShop)
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
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
                        .border(1.dp, Color(0x33F59E0B), RoundedCornerShape(16.dp))
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
                                    .background(ColorShop, androidx.compose.foundation.shape.CircleShape)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "SALDO WALLET",
                                color = ColorShop,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = profileData.balance,
                            color = TextPrimary,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Market Description Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1F1F24)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "MINER MARKET",
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Temukan miner yang sesuai dengan strategi mining kamu. Setiap miner memiliki harga dan pendapatan berbeda. Semakin tinggi tier, semakin besar potensi pendapatan NX.",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    }
                }

                // Free Miner
                MinerCard(
                    iconRes = R.drawable.ic_miner_free,
                    name = "FREE MINER",
                    description = "Miner gratis dengan kecepatan sangat rendah. Cocok sebagai pilihan ketika saldo NX habis.",
                    priceLabel = "GRATIS",
                    rewardLabel = "2 NX / 24 JAM",
                    extraInfo = "Reward tersedia:\nSETIAP 2 HARI",
                    accentColor = MinerFreeAccent,
                    buttonText = "AKTIFKAN",
                    onActionClick = onFreeMinerClick
                )

                // Basic Miner
                MinerCard(
                    iconRes = R.drawable.ic_miner_basic,
                    name = "BASIC MINER",
                    description = "Miner standar untuk memulai perjalanan mining NX.",
                    priceLabel = "25",
                    rewardLabel = "5 NX / 24 JAM",
                    accentColor = MinerBasicAccent,
                    buttonText = "BELI",
                    onActionClick = { onPurchaseClick("25", "basic_miner", "Basic Miner", "5") }
                )

                // Slow Miner
                MinerCard(
                    iconRes = R.drawable.ic_miner_slow,
                    name = "SLOW MINER",
                    description = "Miner lambat dengan performa lebih baik dari Basic Miner.",
                    priceLabel = "100",
                    rewardLabel = "25 NX / 24 JAM",
                    accentColor = MinerSlowAccent,
                    buttonText = "BELI",
                    onActionClick = { onPurchaseClick("100", "slow_miner", "Slow Miner", "25") }
                )

                // Fast Miner
                MinerCard(
                    iconRes = R.drawable.ic_miner_fast,
                    name = "FAST MINER",
                    description = "Miner cepat untuk meningkatkan produksi NX secara signifikan.",
                    priceLabel = "400",
                    rewardLabel = "120 NX / 24 JAM",
                    accentColor = MinerFastAccent,
                    buttonText = "BELI",
                    onActionClick = { onPurchaseClick("400", "fast_miner", "Fast Miner", "120") }
                )

                // Ultra Miner
                MinerCard(
                    iconRes = R.drawable.ic_miner_ultra,
                    name = "ULTRA MINER",
                    description = "Miner kelas tinggi dengan kemampuan produksi NX yang jauh lebih besar.",
                    priceLabel = "1500",
                    rewardLabel = "550 NX / 24 JAM",
                    accentColor = MinerUltraAccent,
                    buttonText = "BELI",
                    onActionClick = { onPurchaseClick("1500", "ultra_miner", "Ultra Miner", "550") }
                )

                // Void Miner
                MinerCard(
                    iconRes = R.drawable.ic_miner_void,
                    name = "VOID MINER",
                    description = "Miner kelas ekstrem yang menggunakan kekuatan Void untuk menghasilkan NX dalam jumlah besar.",
                    priceLabel = "4000",
                    rewardLabel = "2.000 NX / 24 JAM",
                    accentColor = MinerVoidAccent,
                    buttonText = "BELI",
                    isSpecial = true,
                    onActionClick = { onPurchaseClick("4000", "void_miner", "Void Miner", "2000") }
                )
                
                Spacer(modifier = Modifier.height(32.dp))
            }

            // Custom Notification Card Overlay
            AnimatedVisibility(
                visible = currentNotification != null,
                enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { -it }),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            ) {
                currentNotification?.let { notification ->
                    val strokeColor = when (notification.type) {
                        NotificationType.ERROR -> WarningRed
                        NotificationType.WARNING -> Color(0xFFFF9800)
                        NotificationType.SUCCESS -> Color(0xFF64DD17)
                    }
                    
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF141011)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(3.dp, strokeColor.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                            .border(1.dp, strokeColor, RoundedCornerShape(16.dp))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = notification.title,
                                color = strokeColor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = notification.message,
                                color = TextPrimary,
                                fontSize = 14.sp,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MinerCard(
    iconRes: Int,
    name: String,
    description: String,
    priceLabel: String,
    rewardLabel: String,
    extraInfo: String? = null,
    accentColor: Color,
    buttonText: String,
    isSpecial: Boolean = false,
    onActionClick: () -> Unit
) {
    val bgColor = if (isSpecial) MinerVoidBg else ProfileCardBg
    val strokeColor = accentColor.copy(alpha = if (isSpecial) 0.8f else 0.4f)
    
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        modifier = Modifier
            .fillMaxWidth()
            .border(if (isSpecial) 2.dp else 1.dp, strokeColor, RoundedCornerShape(20.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(accentColor.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                        .border(1.dp, accentColor.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = name,
                        tint = accentColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = name,
                    color = accentColor,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    letterSpacing = 1.sp
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = description,
                color = TextSecondary,
                fontSize = 13.sp,
                lineHeight = 20.sp
            )
            
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
                        text = if (priceLabel == "GRATIS") priceLabel else "$priceLabel NX",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "PENDAPATAN", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = rewardLabel,
                        color = accentColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            if (extraInfo != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = extraInfo,
                    color = TextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(accentColor.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                        .padding(8.dp)
                )
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
                    text = buttonText,
                    color = if (isSpecial) Color.White else Color(0xFF111111),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

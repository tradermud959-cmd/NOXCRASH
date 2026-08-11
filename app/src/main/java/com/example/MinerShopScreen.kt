package com.example

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun MinerShopScreen(navController: NavController) {
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

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 2 })

    Scaffold(
        topBar = {
            Column {
                TopBarWithBack(
                    title = if (pagerState.currentPage == 0) "MINER SHOP" else "AI MODE", 
                    navController = navController, 
                    color = if (pagerState.currentPage == 0) ColorShop else Color(0xFFE040FB)
                )
                TabRow(
                    selectedTabIndex = pagerState.currentPage,
                    containerColor = DarkBackground,
                    contentColor = Color.White,
                    indicator = { tabPositions ->
                        if (pagerState.currentPage < tabPositions.size) {
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                                color = if (pagerState.currentPage == 0) ColorShop else Color(0xFFE040FB)
                            )
                        }
                    }
                ) {
                    Tab(
                        selected = pagerState.currentPage == 0,
                        onClick = { coroutineScope.launch { pagerState.animateScrollToPage(0) } },
                        text = { Text("MINER MARKET", fontWeight = FontWeight.Bold, color = if (pagerState.currentPage == 0) ColorShop else TextSecondary) }
                    )
                    Tab(
                        selected = pagerState.currentPage == 1,
                        onClick = { coroutineScope.launch { pagerState.animateScrollToPage(1) } },
                        text = { Text("AI MODE", fontWeight = FontWeight.Bold, color = if (pagerState.currentPage == 1) Color(0xFFE040FB) else TextSecondary) }
                    )
                }
            }
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> MinerMarketPage(showNotification = ::showNotification)
                    1 -> AIShopPage(showNotification = ::showNotification)
                }
            }
            
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
fun MinerMarketPage(showNotification: (NotificationType, String, String) -> Unit) {
    val profileData by ProfileManager.profileData.collectAsState()
    val miningStatus by MiningManager.miningStatus.collectAsState()
    val aiState by AIManager.aiState.collectAsState()
    val lastFreeMinerUsedAt by MiningManager.lastFreeMinerUsedAt.collectAsState()

    val onPurchaseClick = { price: String, minerId: String, minerName: String ->
        if (miningStatus != MiningStatus.OFF) {
            showNotification(
                NotificationType.WARNING,
                "⚠️ MINER MASIH AKTIF",
                "Miner masih aktif. Tunggu sampai mining selesai."
            )
        } else if (aiState != AIState.NO_AI) {
            showNotification(
                NotificationType.WARNING,
                "AI MODE MASIH AKTIF ☠️",
                "Nonaktifkan AI Mode terlebih dahulu sebelum mengaktifkan miner."
            )
        } else {
            val priceDec = BigDecimal(price)
            val success = ProfileManager.updateBalance(priceDec)
            if (success) {
                val formattedPrice = java.text.DecimalFormat("#,###.########", java.text.DecimalFormatSymbols(java.util.Locale("id", "ID"))).format(priceDec)
                HistoryManager.addHistory(HistoryType.PURCHASE, "🛒 \${minerName.uppercase()}", "Miner dibeli", "-\${formattedPrice} NX")
                StatisticsManager.addPurchase()
                
                val rewardDec = NoxEconomyConfig.getTargetRewardForMiner(minerId)
                MiningManager.startMining(minerId, minerName, rewardDec, 24)
                showNotification(
                    NotificationType.SUCCESS,
                    "⛏️ MINER AKTIF",
                    "\$minerName berhasil dibeli dan mulai mining."
                )
            } else {
                showNotification(
                    NotificationType.ERROR,
                    "☠️ WADUH",
                    "Gak cukup bjir, mining dulu sana yang free\uD83D\uDE39"
                )
            }
        }
    }
    
    val onFreeMinerClick: () -> Unit = {
        if (miningStatus != MiningStatus.OFF) {
            showNotification(
                NotificationType.WARNING,
                "⚠️ MINER MASIH AKTIF",
                "Miner masih aktif. Tunggu sampai mining selesai."
            )
        } else if (aiState != AIState.NO_AI) {
            showNotification(
                NotificationType.WARNING,
                "AI MODE MASIH AKTIF ☠️",
                "Nonaktifkan AI Mode terlebih dahulu sebelum mengaktifkan miner."
            )
        } else {
            val currentTime = System.currentTimeMillis()
            val cooldownMs = 2L * 24 * 60 * 60 * 1000 // 2 days
            if (currentTime - lastFreeMinerUsedAt >= cooldownMs) {
                MiningManager.startMining("free_miner", "Free Miner", NoxEconomyConfig.getTargetRewardForMiner("free_miner"), 24)
                showNotification(
                    NotificationType.SUCCESS,
                    "⛏️ MINING DIMULAI",
                    "Free Miner berhasil diaktifkan."
                )
            } else {
                showNotification(
                    NotificationType.WARNING,
                    "⚠️ COOLDOWN",
                    "Free Miner masih dalam cooldown."
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

        MinerCard(
            minerId = "free_miner",
            name = "FREE MINER",
            description = "Miner gratis dengan kecepatan sangat rendah. Cocok sebagai pilihan ketika saldo NX habis.",
            priceLabel = "GRATIS",
            accentColor = MinerFreeAccent,
            buttonText = "AKTIFKAN",
            onActionClick = onFreeMinerClick
        )

        MinerCard(
            minerId = "basic_miner",
            name = "BASIC MINER",
            description = "Miner standar untuk memulai perjalanan mining NX.",
            priceLabel = "25",
            accentColor = MinerBasicAccent,
            buttonText = "BELI",
            onActionClick = { onPurchaseClick("25", "basic_miner", "Basic Miner") }
        )

        MinerCard(
            minerId = "slow_miner",
            name = "SLOW MINER",
            description = "Miner lambat dengan performa lebih baik dari Basic Miner.",
            priceLabel = "100",
            accentColor = MinerSlowAccent,
            buttonText = "BELI",
            onActionClick = { onPurchaseClick("100", "slow_miner", "Slow Miner") }
        )

        MinerCard(
            minerId = "fast_miner",
            name = "FAST MINER",
            description = "Miner cepat untuk meningkatkan produksi NX secara signifikan.",
            priceLabel = "400",
            accentColor = MinerFastAccent,
            buttonText = "BELI",
            onActionClick = { onPurchaseClick("400", "fast_miner", "Fast Miner") }
        )

        MinerCard(
            minerId = "ultra_miner",
            name = "ULTRA MINER",
            description = "Miner kelas tinggi dengan kemampuan produksi NX yang jauh lebih besar.",
            priceLabel = "1500",
            accentColor = MinerUltraAccent,
            buttonText = "BELI",
            onActionClick = { onPurchaseClick("1500", "ultra_miner", "Ultra Miner") }
        )

        MinerCard(
            minerId = "void_miner",
            name = "VOID MINER",
            description = "Miner kelas ekstrem yang menggunakan kekuatan Void untuk menghasilkan NX dalam jumlah besar.",
            priceLabel = "4000",
            accentColor = MinerVoidAccent,
            buttonText = "BELI",
            isSpecial = true,
            onActionClick = { onPurchaseClick("4000", "void_miner", "Void Miner") }
        )
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun MinerCard(
    minerId: String,
    name: String,
    description: String,
    priceLabel: String,
    accentColor: Color,
    buttonText: String,
    isSpecial: Boolean = false,
    onActionClick: () -> Unit
) {
    val bgColor = if (isSpecial) MinerVoidBg else ProfileCardBg
    val strokeColor = accentColor.copy(alpha = if (isSpecial) 0.8f else 0.4f)
    
    val hashrate = NoxEconomyConfig.getHashrateForMiner(minerId)
    val targetReward = NoxEconomyConfig.getTargetRewardForMiner(minerId)
    val formattedTargetReward = java.text.DecimalFormat("#,###.########", java.text.DecimalFormatSymbols(java.util.Locale("id", "ID"))).format(targetReward)
    
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
                    coil.compose.AsyncImage(
                        model = getMinerIconPath(minerId),
                        contentDescription = name,
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
                    Text(text = "HASHRATE", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = NoxEconomyConfig.getHashrateString(hashrate),
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "ESTIMASI REWARD", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${formattedTargetReward} NX",
                        color = accentColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "TIME", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "CALCULATED BY ENGINE",
                        color = TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "HARGA", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (priceLabel == "GRATIS") priceLabel else {
                            val priceDec = BigDecimal(priceLabel)
                            val formattedPrice = java.text.DecimalFormat("#,###.########", java.text.DecimalFormatSymbols(java.util.Locale("id", "ID"))).format(priceDec)
                            "\$formattedPrice NX"
                        },
                        color = TextPrimary,
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
                            delay(500)
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

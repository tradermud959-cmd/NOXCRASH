package com.example

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material3.*
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoxCrashApp() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "home"

    ModalNavigationDrawer(
        drawerState = drawerState,
        scrimColor = Color.Black.copy(alpha = 0.6f),
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = TopBarBackground,
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                DrawerContent(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        scope.launch { drawerState.close() }
                        if (currentRoute != route) {
                            navController.navigate(route) {
                                popUpTo("home") { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    onClose = {
                        scope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {
        val blurRadius = if (drawerState.isOpen) 8.dp else 0.dp
        Scaffold(
            topBar = {
                if (currentRoute == "home") {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                "NOXCRASH",
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                letterSpacing = 2.sp
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu", tint = TextPrimary)
                            }
                        },
                        actions = {
                            val hasUnread by NoxNotificationManager.notificationsList.collectAsState()
                            val canCheckIn by CheckInManager.canCheckInToday.collectAsState()
                            val lastGachaTime by GachaManager.lastGachaTime.collectAsState()
                            val canGacha = GachaManager.canPullGacha()
                            
                            val unreadCount = hasUnread.size + (if (canCheckIn) 1 else 0) + (if (canGacha) 1 else 0)
                            
                            IconButton(onClick = { navController.navigate("notifikasi") }) {
                                if (unreadCount > 0) {
                                    BadgedBox(badge = { Badge { Text(unreadCount.toString()) } }) {
                                        Icon(Icons.Default.Notifications, contentDescription = "Notifikasi", tint = ColorNotification)
                                    }
                                } else {
                                    Icon(Icons.Default.Notifications, contentDescription = "Notifikasi", tint = ColorNotification)
                                }
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = TopBarBackground
                        )
                    )
                }
            },
            containerColor = DarkBackground,
            modifier = Modifier.blur(blurRadius)
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                NotificationPopupOverlay(onNavigateToNotification = { navController.navigate("notifikasi") })

                NavHost(navController = navController, startDestination = "home") {
                    composable("home") { HomeScreen(navController) }
                    composable("profil") { ProfileScreen(onOpenDrawer = { scope.launch { drawerState.open() } }) }
                    composable("notifikasi") { NotificationScreen(navController) }
                    composable("statistik") { StatisticsScreen(navController) }
                    composable("riwayat") { HistoryScreen(navController) }
                    composable("ai_riwayat") { AIHistoryScreen(navController) }
                    composable("pengaturan") { SettingsScreen(navController) }
                    composable("musik") { MusicListScreen(navController) }
                    composable("shop") { MinerShopScreen(navController) }
                    composable("info_app") { InfoAppScreen(navController) }
                    composable("info_nx") { InfoNXScreen(navController) }
                    composable("info_penting") { InfoPentingScreen(navController) }
                    composable("white_paper") { WhitePaperScreen(navController) }
                    composable("backup") { BackupScreen(navController) }
                    composable("restore") { RestoreScreen(navController) }
                    composable("reset") { ResetScreen(navController) }
                    composable("withdraw") { WithdrawScreen(onOpenDrawer = { scope.launch { drawerState.open() } }) }
                    composable("gacha_pull") { GachaPullScreen(navController) }
                    composable("checkin") { CheckInScreen(navController) }
                }
            }
        }
    }
}

@Composable
fun DrawerContent(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TopBarBackground)
    ) {
        DrawerHeader(onClose = onClose)
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp)
        ) {
            DrawerSectionTitle("MAIN")
            DrawerItem(icon = Icons.Default.Home, title = "Home", route = "home", currentRoute = currentRoute, onNavigate = onNavigate)
            DrawerItem(icon = Icons.Default.Person, title = "Profil", route = "profil", currentRoute = currentRoute, onNavigate = onNavigate)
            DrawerItem(icon = Icons.Default.AccountBalanceWallet, title = "Withdraw", route = "withdraw", currentRoute = currentRoute, onNavigate = onNavigate)
            
            Spacer(modifier = Modifier.height(8.dp))
            DrawerSectionTitle("ACTIVITY")
            DrawerItem(icon = Icons.Default.Notifications, title = "Notifikasi", route = "notifikasi", currentRoute = currentRoute, onNavigate = onNavigate)
            DrawerItem(icon = Icons.Default.Info, title = "Statistik", route = "statistik", currentRoute = currentRoute, onNavigate = onNavigate) // Using Info instead of unsupported BarChart
            DrawerItem(icon = Icons.Default.List, title = "Riwayat Miner", route = "riwayat", currentRoute = currentRoute, onNavigate = onNavigate)
            DrawerItem(icon = Icons.Default.Menu, title = "Riwayat AI Mode", route = "ai_riwayat", currentRoute = currentRoute, onNavigate = onNavigate) // Fallbacks
            
            Spacer(modifier = Modifier.height(8.dp))
            DrawerSectionTitle("SYSTEM")
            DrawerItem(icon = Icons.Default.Settings, title = "Pengaturan", route = "pengaturan", currentRoute = currentRoute, onNavigate = onNavigate)
        }
    }
}

@Composable
fun DrawerHeader(onClose: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = ColorMining.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .border(1.dp, ColorMining.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "N",
                    color = ColorMining,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                "NOXCRASH",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                color = TextPrimary,
                letterSpacing = 2.sp
            )
        }
        IconButton(
            onClick = onClose,
            modifier = Modifier
                .size(36.dp)
                .background(DarkSurfaceVariant, CircleShape)
        ) {
            Icon(Icons.Default.Close, contentDescription = "Tutup", tint = TextSecondary, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
fun DrawerSectionTitle(title: String) {
    Text(
        text = title,
        color = TextSecondary.copy(alpha = 0.5f),
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.5.sp,
        modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 12.dp)
    )
}

@Composable
fun DrawerItem(
    icon: ImageVector,
    title: String,
    route: String,
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    val isSelected = route == currentRoute
    val containerColor = if (isSelected) ColorMining.copy(alpha = 0.15f) else Color.Transparent
    val contentColor = if (isSelected) ColorMining else TextSecondary
    val fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
    val borderColor = if (isSelected) ColorMining.copy(alpha = 0.3f) else Color.Transparent

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .padding(vertical = 2.dp)
            .border(1.dp, borderColor, RoundedCornerShape(14.dp))
            .background(containerColor, RoundedCornerShape(14.dp))
             .clip(RoundedCornerShape(14.dp))
            .clickable { onNavigate(route) }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = contentColor,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            color = if (isSelected) TextPrimary else TextSecondary,
            fontSize = 15.sp,
            fontWeight = fontWeight,
            letterSpacing = 0.5.sp
        )
    }
}

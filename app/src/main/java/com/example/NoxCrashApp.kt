package com.example

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
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
                            IconButton(onClick = { navController.navigate("notifikasi") }) {
                                Icon(Icons.Default.Notifications, contentDescription = "Notifikasi", tint = ColorNotification)
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
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") { HomeScreen(navController) }
                    composable("profil") { ProfileScreen(onOpenDrawer = { scope.launch { drawerState.open() } }) }
                    composable("notifikasi") { NotificationScreen(navController) }
                    composable("statistik") { StatisticsScreen(navController) }
                    composable("riwayat") { HistoryScreen(navController) }
                    composable("pengaturan") { SettingsScreen(navController) }
                    composable("musik") { MusicListScreen(navController) }
                    composable("shop") { MinerShopScreen(navController) }
                    composable("info_app") { InfoAppScreen(navController) }
                    composable("info_nx") { InfoNXScreen(navController) }
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
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "NOXCRASH",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = TextPrimary,
                letterSpacing = 2.sp
            )
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Tutup", tint = TextPrimary)
            }
        }
        Spacer(modifier = Modifier.height(32.dp))

        DrawerItem("🏠 Home", "home", currentRoute, onNavigate)
        DrawerItem("👤 Profil", "profil", currentRoute, onNavigate)
        DrawerItem("🔔 Notifikasi", "notifikasi", currentRoute, onNavigate)
        DrawerItem("📊 Statistik", "statistik", currentRoute, onNavigate)
        DrawerItem("📜 Riwayat Miner", "riwayat", currentRoute, onNavigate)
        DrawerItem("⚙️ Pengaturan", "pengaturan", currentRoute, onNavigate)
    }
}

@Composable
fun DrawerItem(
    title: String,
    route: String,
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    val isSelected = route == currentRoute
    val containerColor = if (isSelected) DarkSurfaceVariant else Color.Transparent
    val textColor = if (isSelected) ColorMining else TextPrimary

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onNavigate(route) }
            .background(containerColor, RoundedCornerShape(8.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = textColor,
            fontSize = 16.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

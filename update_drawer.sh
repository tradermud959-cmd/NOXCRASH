sed -i '143,$d' app/src/main/java/com/example/NoxCrashApp.kt
cat << 'INNER_EOF' >> app/src/main/java/com/example/NoxCrashApp.kt
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
            DrawerItem(icon = androidx.compose.material.icons.filled.Info, title = "Statistik", route = "statistik", currentRoute = currentRoute, onNavigate = onNavigate) // Using Info instead of unsupported BarChart
            DrawerItem(icon = androidx.compose.material.icons.filled.List, title = "Riwayat Miner", route = "riwayat", currentRoute = currentRoute, onNavigate = onNavigate)
            DrawerItem(icon = androidx.compose.material.icons.filled.Menu, title = "Riwayat AI Mode", route = "ai_riwayat", currentRoute = currentRoute, onNavigate = onNavigate) // Fallbacks
            
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
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                    )
                    .androidx.compose.foundation.border(1.dp, ColorMining.copy(alpha = 0.5f), androidx.compose.foundation.shape.RoundedCornerShape(12.dp)),
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
                .background(DarkSurfaceVariant, androidx.compose.foundation.shape.CircleShape)
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
            .androidx.compose.foundation.border(1.dp, borderColor, androidx.compose.foundation.shape.RoundedCornerShape(14.dp))
            .background(containerColor, androidx.compose.foundation.shape.RoundedCornerShape(14.dp))
            .androidx.compose.foundation.clickable(
                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                indication = androidx.compose.material.ripple.rememberRipple(color = ColorMining)
            ) { onNavigate(route) }
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
INNER_EOF

package com.example

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningRed
import android.app.Activity
import androidx.compose.ui.platform.LocalContext

@Composable
fun SettingsScreen(navController: NavController) {
    var showExitDialog by remember { mutableStateOf(false) }
    val activity = LocalContext.current as? Activity

    Scaffold(
        topBar = {
            TopBarWithBack(title = "PENGATURAN", navController = navController, color = TextPrimary)
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                Text("DATA & SISTEM", color = TextSecondary, fontSize = 14.sp, letterSpacing = 1.sp)
                Spacer(modifier = Modifier.height(16.dp))
                
                SettingsItem(title = "💾 Backup", description = "Simpan data saat ini") { navController.navigate("backup") }
                SettingsItem(title = "♻️ Restore", description = "Kembalikan data") { navController.navigate("restore") }
                SettingsItem(title = "🗑️ Reset", description = "Hapus semua data") { navController.navigate("reset") }
                
                Spacer(modifier = Modifier.height(32.dp))
                Text("INFORMASI", color = TextSecondary, fontSize = 14.sp, letterSpacing = 1.sp)
                Spacer(modifier = Modifier.height(16.dp))
                
                SettingsItem(title = "ℹ️ Info Aplikasi", description = "Versi dan deskripsi game") {
                    navController.navigate("info_app")
                }
                SettingsItem(title = "🪙 Info Koin NX", description = "Penjelasan sistem NX") {
                    navController.navigate("info_nx")
                }
                SettingsItem(title = "⚠️ Informasi Penting", description = "Tujuan NoxCrash sebagai game") {
                    navController.navigate("info_penting")
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                SettingsItem(
                    title = "🚪 Keluar Aplikasi", 
                    description = "Tutup NoxCrash dengan aman",
                    titleColor = WarningRed
                ) {
                    showExitDialog = true
                }
            }

            if (showExitDialog) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.8f))
                        .clickable(enabled = false) {},
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B0B0E)),
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .border(1.dp, WarningRed.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "KELUAR",
                                color = WarningRed,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "Apakah Anda yakin ingin keluar dari NoxCrash?",
                                color = TextPrimary,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                lineHeight = 22.sp
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Button(
                                    onClick = { showExitDialog = false },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C30)),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("BATAL", color = TextPrimary, fontWeight = FontWeight.Bold)
                                }
                                Button(
                                    onClick = { 
                                        showExitDialog = false
                                        activity?.finish()
                                    },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = WarningRed),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("KELUAR", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsItem(
    title: String,
    description: String,
    titleColor: Color = TextPrimary,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick)
            .background(Color(0xFF1A1B1F), RoundedCornerShape(12.dp))
            .border(1.dp, Color(0x1AFFFFFF), RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = title,
                color = titleColor,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                color = TextSecondary,
                fontSize = 12.sp
            )
        }
        Text(
            text = "➔",
            color = TextSecondary,
            fontSize = 14.sp
        )
    }
}

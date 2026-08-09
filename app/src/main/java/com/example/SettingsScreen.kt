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
                
                SettingsItem(title = "💾 Backup", description = "Simpan data saat ini") { /* Todo */ }
                SettingsItem(title = "♻️ Restore", description = "Kembalikan data") { /* Todo */ }
                SettingsItem(title = "🗑️ Reset", description = "Hapus semua data") { /* Todo */ }
                
                Spacer(modifier = Modifier.height(32.dp))
                Text("INFORMASI", color = TextSecondary, fontSize = 14.sp, letterSpacing = 1.sp)
                Spacer(modifier = Modifier.height(16.dp))
                
                SettingsItem(title = "ℹ️ Info Aplikasi", description = "Versi dan deskripsi game") {
                    navController.navigate("info_app")
                }
                SettingsItem(title = "🪙 Info Koin NX", description = "Penjelasan sistem NX") {
                    navController.navigate("info_nx")
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
                        .background(Color.Black.copy(alpha = 0.7f)),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF141011)),
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .border(2.dp, WarningRed.copy(alpha = 0.8f), RoundedCornerShape(16.dp))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("⚠️ KELUAR APLIKASI", color = WarningRed, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Yakin ingin keluar dari NoxCrash?",
                                color = TextPrimary,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Button(
                                    onClick = { showExitDialog = false },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF333333)),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1f).height(48.dp)
                                ) {
                                    Text("BATAL", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Button(
                                    onClick = { 
                                        showExitDialog = false
                                        activity?.finish()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = WarningRed),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1f).height(48.dp)
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
fun SettingsItem(title: String, description: String, titleColor: Color = TextPrimary, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp)
    ) {
        Text(title, color = titleColor, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(description, color = TextSecondary, fontSize = 12.sp)
    }
}

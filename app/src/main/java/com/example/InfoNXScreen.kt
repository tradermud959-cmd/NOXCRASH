package com.example

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.ui.theme.BorderColor

@Composable
fun InfoNXScreen(navController: NavController) {
    val profileData by ProfileManager.profileData.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopBarWithBack(title = "INFO KOIN NX", navController = navController, color = TextPrimary)
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF141011)),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderColor, RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Text("🪙 SALDO KAMU", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${profileData.balance}",
                        color = Color(0xFF06B6D4),
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            Text("APA ITU NX?", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "NX adalah mata uang virtual internal yang digunakan di dalam game NoxCrash.",
                color = TextSecondary,
                fontSize = 14.sp,
                lineHeight = 22.sp
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            Text("KEGUNAAN NX", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "Gunakan NX untuk:\n• membeli miner\n• mengembangkan aktivitas mining\n• fitur game yang membutuhkan NX",
                color = TextSecondary,
                fontSize = 14.sp,
                lineHeight = 22.sp
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            Text("SATUAN NX", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "Sistem NoxCrash menggunakan satuan NX dengan angka desimal.\n\nContoh:\n1 NX\n0.1 NX\n0.01 NX\n0.00000001 NX",
                color = TextSecondary,
                fontSize = 14.sp,
                lineHeight = 22.sp
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                "PERHATIAN: NX pada tahap ini adalah MATA UANG VIRTUAL INTERNAL APLIKASI. NX bukan cryptocurrency nyata dan tidak dapat ditukar menjadi uang asli.",
                color = Color(0xFF64DD17),
                fontSize = 12.sp,
                lineHeight = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

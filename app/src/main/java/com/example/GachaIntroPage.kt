package com.example

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ui.theme.*

@Composable
fun GachaIntroPage(navController: NavController) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "🎁 DAILY DROP",
            color = Color(0xFF00E676),
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.sp
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Dapatkan Miner Gratis Setiap Hari!",
            color = TextSecondary,
            fontSize = 14.sp
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1B0B0E)),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFF00E676).copy(alpha = 0.5f), RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text("INFORMASI SISTEM", color = Color(0xFF00E676), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Sistem Daily Drop memungkinkan kamu untuk mencari koordinat Miner secara acak. Kamu bisa mendapatkan Miner tier tinggi, tier rendah, bonus NX, atau bahkan gagal (Zonk).",
                    color = TextPrimary,
                    fontSize = 13.sp,
                    lineHeight = 20.sp
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                Text("PROBABILITAS DROP:", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                
                DropRateItem("ZONK (Gagal)", "60%")
                DropRateItem("Hadiah Hiburan (NX)", "25%")
                DropRateItem("Miner Low-Tier", "12%")
                DropRateItem("Miner Mid-Tier", "2% (1:50)")
                DropRateItem("Miner High-Tier", "1% (1:100)")
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Sistem ini 100% Gratis dan reset setiap 24 Jam setelah kamu menarik Gacha.",
                    color = WarningRed,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 18.sp
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = { navController.navigate("gacha_pull") },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E676)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("MULAI DAILY DROP", color = Color.Black, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun DropRateItem(label: String, rate: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "• $label", color = TextPrimary, fontSize = 13.sp)
        Text(text = rate, color = Color(0xFF00E676), fontSize = 13.sp, fontWeight = FontWeight.Bold)
    }
}

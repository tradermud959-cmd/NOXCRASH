package com.example

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ui.theme.*

@Composable
fun ResetScreen(navController: NavController) {
    val context = LocalContext.current
    
    Scaffold(
        topBar = {
            TopBarHamburger(title = "RESET", onNavigateHome = {
                navController.popBackStack("home", inclusive = false)
            }, color = Color(0xFFFF1744))
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color(0xFFFF1744).copy(alpha = 0.15f), RoundedCornerShape(24.dp))
                    .border(2.dp, Color(0xFFFF1744).copy(alpha = 0.5f), RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("🗑️", fontSize = 40.sp)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "RESET DATA",
                color = Color(0xFFFF1744),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp,
                letterSpacing = 1.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1B0B0E)),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0x33FF1744), RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Hapus seluruh data user yang sedang tersimpan di aplikasi.",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "File backup yang berada di Documents/Noxcrash/backup/ tetap aman dan dapat digunakan untuk mengembalikan data melalui Restore.",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Perhatian:\nTindakan ini akan menghapus data user dari aplikasi.",
                        color = Color(0xFFFF1744),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = {
                    BackupRestoreManager.performReset(context)
                    Toast.makeText(context, "Data berhasil direset.", Toast.LENGTH_SHORT).show()
                    navController.popBackStack("home", inclusive = false)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF1744)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "RESET DATA",
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

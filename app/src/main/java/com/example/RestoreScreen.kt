package com.example

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
fun RestoreScreen(navController: NavController) {
    val context = LocalContext.current
    var isRestoring by remember { mutableStateOf(false) }
    
    val restoreLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            if (uri != null) {
                val success = BackupRestoreManager.performRestore(context, uri)
                if (success) {
                    Toast.makeText(context, "Data berhasil dikembalikan.", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Gagal mengembalikan data. Pastikan file valid.", Toast.LENGTH_LONG).show()
                }
            }
        }
        isRestoring = false
    }

    Scaffold(
        topBar = {
            TopBarHamburger(title = "RESTORE", onNavigateHome = {
                navController.popBackStack("home", inclusive = false)
            }, color = Color(0xFF00E5FF))
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
                    .background(Color(0xFF00E5FF).copy(alpha = 0.15f), RoundedCornerShape(24.dp))
                    .border(2.dp, Color(0xFF00E5FF).copy(alpha = 0.5f), RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("♻️", fontSize = 40.sp)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "RESTORE DATA",
                color = Color(0xFF00E5FF),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp,
                letterSpacing = 1.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ProfileCardBg),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0x3300E5FF), RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Kembalikan data user dari file backup JSON yang sebelumnya telah dibuat melalui fitur Backup.",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Gunakan file backup yang valid untuk mengembalikan data aplikasi ke kondisi yang tersimpan di dalam backup.",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = {
                    if (!isRestoring) {
                        isRestoring = true
                        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                            addCategory(Intent.CATEGORY_OPENABLE)
                            type = "application/json"
                        }
                        restoreLauncher.launch(intent)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E5FF)),
                shape = RoundedCornerShape(16.dp),
                enabled = !isRestoring
            ) {
                if (isRestoring) {
                    CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        text = "PILIH BACKUP",
                        color = Color.Black,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}

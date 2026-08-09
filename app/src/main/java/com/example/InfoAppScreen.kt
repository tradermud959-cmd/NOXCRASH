package com.example

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import android.content.pm.PackageManager
import androidx.compose.ui.platform.LocalContext

@Composable
fun InfoAppScreen(navController: NavController) {
    val context = LocalContext.current
    val packageInfo = try {
        context.packageManager.getPackageInfo(context.packageName, 0)
    } catch (e: PackageManager.NameNotFoundException) {
        null
    }
    
    val versionName = packageInfo?.versionName ?: "1.0"
    
    Scaffold(
        topBar = {
            TopBarWithBack(title = "INFO APLIKASI", navController = navController, color = TextPrimary)
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("☠️", fontSize = 64.sp)
            Spacer(modifier = Modifier.height(24.dp))
            Text("NOXCRASH", color = TextPrimary, fontWeight = FontWeight.ExtraBold, fontSize = 28.sp, letterSpacing = 2.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Versi $versionName", color = TextSecondary, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Game mining offline NoxCrash.",
                color = TextSecondary,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(64.dp))
        }
    }
}

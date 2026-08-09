package com.example

import android.content.pm.PackageManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun InfoAppScreen(navController: NavController) {
    val context = LocalContext.current
    val packageInfo = try {
        context.packageManager.getPackageInfo(context.packageName, 0)
    } catch (e: PackageManager.NameNotFoundException) {
        null
    }
    
    val versionName = packageInfo?.versionName ?: "1.0.1"
    
    val neonCyan = Color(0xFF06B6D4)
    val voidBlack = Color(0xFF070707)
    val darkCharcoal = Color(0xFF101314)
    val textLightGray = Color(0xFFAAAAAA)
    
    val bgGradient = Brush.verticalGradient(
        colors = listOf(
            voidBlack,
            Color(0xFF041216), // Dark cyan-black
            voidBlack
        )
    )

    val glowGradient = Brush.radialGradient(
        colors = listOf(
            neonCyan.copy(alpha = 0.25f),
            Color.Transparent
        )
    )

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val interactionSource = remember { MutableInteractionSource() }
                val isPressed by interactionSource.collectIsPressedAsState()
                
                IconButton(
                    onClick = { navController.popBackStack() },
                    interactionSource = interactionSource
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Kembali",
                        tint = if (isPressed) neonCyan else Color.White
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "INFO APLIKASI",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    letterSpacing = 1.sp
                )
            }
        },
        containerColor = voidBlack
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(bgGradient)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(48.dp))
                
                // Logo with Glow
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(160.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(glowGradient)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = "NoxCrash Logo",
                        modifier = Modifier.size(120.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Identity Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = darkCharcoal),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = neonCyan.copy(alpha = 0.4f),
                                shape = RoundedCornerShape(24.dp)
                            )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "NOXCRASH",
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 32.sp,
                                letterSpacing = 4.sp
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Text(
                                text = "VERSI $versionName",
                                color = neonCyan,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                letterSpacing = 2.sp
                            )
                            
                            Spacer(modifier = Modifier.height(32.dp))
                            
                            HorizontalDivider(
                                modifier = Modifier.fillMaxWidth(0.5f),
                                color = neonCyan.copy(alpha = 0.3f),
                                thickness = 1.dp
                            )
                            
                            Spacer(modifier = Modifier.height(32.dp))
                            
                            Text(
                                text = "Game mining offline NoxCrash.",
                                color = textLightGray,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                lineHeight = 24.sp
                            )
                            
                            Spacer(modifier = Modifier.height(48.dp))
                            
                            // Futuristic/Cyber dots decoration
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(modifier = Modifier.size(4.dp).background(neonCyan))
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(modifier = Modifier.size(4.dp).background(neonCyan.copy(alpha = 0.5f)))
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(modifier = Modifier.size(4.dp).background(neonCyan.copy(alpha = 0.2f)))
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}

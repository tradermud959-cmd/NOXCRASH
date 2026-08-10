package com.example

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
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

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun TopBarHamburger(title: String, onNavigateHome: () -> Unit, color: Color) {
    var expanded by remember { mutableStateOf(false) }
    CenterAlignedTopAppBar(
        title = { Text(title, color = color, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, letterSpacing = 1.sp) },
        navigationIcon = {
            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu", tint = TextPrimary)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(DarkSurfaceVariant)
                ) {
                    DropdownMenuItem(
                        text = { Text("HOME", color = TextPrimary, fontWeight = FontWeight.Bold) },
                        onClick = {
                            expanded = false
                            onNavigateHome()
                        }
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = TopBarBackground)
    )
}

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun BackupScreen(navController: NavController) {
    val context = LocalContext.current
    var isBackingUp by remember { mutableStateOf(false) }
    
    val backupLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            if (uri != null) {
                BackupRestoreManager.saveTreeUri(context, uri)
                isBackingUp = true
                val fileName = BackupRestoreManager.performBackup(context, uri)
                if (fileName != null) {
                    Toast.makeText(context, "Backup berhasil: \$fileName", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Backup gagal.", Toast.LENGTH_SHORT).show()
                }
                isBackingUp = false
            }
        } else {
            isBackingUp = false
        }
    }

    Scaffold(
        topBar = {
            TopBarHamburger(title = "BACKUP", onNavigateHome = {
                navController.popBackStack("home", inclusive = false)
            }, color = Color(0xFF64DD17))
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
                    .background(Color(0xFF64DD17).copy(alpha = 0.15f), RoundedCornerShape(24.dp))
                    .border(2.dp, Color(0xFF64DD17).copy(alpha = 0.5f), RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("💾", fontSize = 40.sp)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "BACKUP DATA",
                color = Color(0xFF64DD17),
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
                    .border(1.dp, Color(0x3364DD17), RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Simpan seluruh data user aplikasi ke dalam file JSON.\nFile backup dapat digunakan kembali untuk mengembalikan data setelah melakukan Reset.",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Lokasi:\nDocuments/Noxcrash/backup/",
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
                    if (!isBackingUp) {
                        isBackingUp = true
                        val savedUri = BackupRestoreManager.getSavedTreeUri(context)
                        if (savedUri != null) {
                            val fileName = BackupRestoreManager.performBackup(context, savedUri)
                            if (fileName != null) {
                                Toast.makeText(context, "Backup berhasil: \$fileName", Toast.LENGTH_LONG).show()
                                isBackingUp = false
                            } else {
                                // If fail, maybe uri is invalid now, request again
                                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
                                backupLauncher.launch(intent)
                            }
                        } else {
                            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
                            backupLauncher.launch(intent)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF64DD17)),
                shape = RoundedCornerShape(16.dp),
                enabled = !isBackingUp
            ) {
                if (isBackingUp) {
                    CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        text = "BUAT BACKUP",
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

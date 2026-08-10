package com.example

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ui.theme.*

@Composable
fun InfoPentingScreen(navController: NavController) {
    val pagerState = rememberPagerState(pageCount = { 2 })

    Scaffold(
        topBar = {
            TopBarWithBack(title = "INFORMASI PENTING", navController = navController, color = Color(0xFFFF1744))
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                when (page) {
                    0 -> Page1Content()
                    1 -> Page2Content()
                }
            }

            // Indicator
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(2) { index ->
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(if (pagerState.currentPage == index) 10.dp else 8.dp)
                            .clip(CircleShape)
                            .background(if (pagerState.currentPage == index) Color(0xFFFF1744) else TextSecondary)
                    )
                }
            }
        }
    }
}

@Composable
fun Page1Content() {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(24.dp)
    ) {
        Text(
            text = "INFORMASI PENTING",
            color = Color(0xFFFF1744),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 24.sp,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "NOXCRASH ADALAH GAME",
            color = TextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "NoxCrash dibuat sebagai game jangka panjang dengan konsep simulasi mining, perkembangan miner, sistem NX, dan berbagai fitur hiburan di dalam aplikasi.",
            color = TextSecondary,
            fontSize = 14.sp,
            lineHeight = 22.sp
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Tujuan utama NoxCrash adalah memberikan pengalaman bermain dan sensasi mining secara virtual.",
            color = TextSecondary,
            fontSize = 14.sp,
            lineHeight = 22.sp
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1B0B0E)),
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, Color(0xFFFF1744).copy(alpha = 0.5f), RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("⚠️", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "PERHATIAN",
                        color = Color(0xFFFF1744),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        letterSpacing = 1.sp
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "NOXCRASH TIDAK DIBUAT UNTUK MENGGANTIKAN PEKERJAAN ATAU PENGHASILAN UTAMA.",
                    color = TextPrimary,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    lineHeight = 26.sp
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Jangan menjadikan NoxCrash sebagai satu-satunya sumber penghasilan atau menggantungkan kebutuhan hidup pada game ini.",
                    color = TextSecondary,
                    fontSize = 14.sp,
                    lineHeight = 22.sp
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "SWIPE KANAN UNTUK MELANJUTKAN ➔",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun Page2Content() {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(24.dp)
    ) {
        Text(
            text = "GUNAKAN SEBAGAI HIBURAN",
            color = ColorMining,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 24.sp,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "NoxCrash dibuat untuk hiburan dan pengalaman bermain. Sistem mining di dalam game merupakan bagian dari mekanisme permainan dan tidak dimaksudkan sebagai pengganti pekerjaan utama.",
            color = TextSecondary,
            fontSize = 14.sp,
            lineHeight = 22.sp
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Pertahankan pekerjaan, usaha, bisnis, atau sumber penghasilan utama Anda. Jangan meninggalkan pekerjaan atau mengubah keputusan keuangan penting hanya karena aktivitas di NoxCrash.",
            color = TextSecondary,
            fontSize = 14.sp,
            lineHeight = 22.sp
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = ProfileCardBg),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderColor, RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "TIDAK ADA JAMINAN PENGHASILAN",
                    color = Color(0xFFFFD54F),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = "NoxCrash tidak menjamin pendapatan, keuntungan, hasil tertentu, atau kemampuan untuk memenuhi kebutuhan hidup pengguna.",
                    color = TextPrimary,
                    fontSize = 14.sp,
                    lineHeight = 22.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Nilai NX dan mekanisme ekonomi dalam game merupakan bagian dari sistem NoxCrash dan tidak boleh dianggap sebagai penghasilan tetap.",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 20.sp
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF142417)),
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, Color(0xFF64DD17).copy(alpha = 0.5f), RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "MAIN DENGAN BIJAK.",
                    color = Color(0xFF64DD17),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                
                Text("• Nikmati NoxCrash sebagai game.", color = TextPrimary, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text("• Gunakan waktu dan sumber daya secara bertanggung jawab.", color = TextPrimary, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text("• Jangan menggantikan pekerjaan utama Anda dengan game.", color = TextPrimary, fontSize = 14.sp)
            }
        }
    }
}

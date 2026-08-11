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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ui.theme.*

val WpBackground = Color(0xFF050509)
val WpSecondaryBackground = Color(0xFF0A0A10)
val WpCard = Color(0xFF0D0D15)
val WpCardSecondary = Color(0xFF11111B)
val WpTextPrimary = Color(0xFFF5F7FA)
val WpTextSecondary = Color(0xFFA8ADB8)
val WpDivider = Color(0xFF252535)

val AccentCyan = Color(0xFF00E5FF)
val AccentBlue = Color(0xFF2979FF)
val AccentGreen = Color(0xFF00FF9D)
val AccentGold = Color(0xFFFFD54F)
val AccentOrange = Color(0xFFFF9800)
val AccentViolet = Color(0xFF7C4DFF)
val AccentSoftPurple = Color(0xFFB388FF)

@Composable
fun WhitePaperScreen(navController: NavController) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 12 })

    Scaffold(
        topBar = {
            TopBarWithBack(
                title = "WHITE PAPER",
                navController = navController,
                color = AccentCyan
            )
        },
        containerColor = WpBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { page ->
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(horizontal = 24.dp, vertical = 24.dp)
                ) {
                    when (page) {
                        0 -> Page01()
                        1 -> Page02()
                        2 -> Page03()
                        3 -> Page04()
                        4 -> Page05()
                        5 -> Page06()
                        6 -> Page07()
                        7 -> Page08()
                        8 -> Page09()
                        9 -> Page10()
                        10 -> Page11()
                        11 -> Page12()
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }

            // Pager Indicator
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(12) { iteration ->
                    val color = if (pagerState.currentPage == iteration) AccentCyan else WpDivider
                    val size = if (pagerState.currentPage == iteration) 8.dp else 6.dp
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(size)
                    )
                }
            }
        }
    }
}

// Components
@Composable
fun WpHeader(number: String, title: String, accentColor: Color) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = number,
            color = accentColor,
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 2.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            color = WpTextPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            lineHeight = 32.sp
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun WpParagraph(text: String) {
    Text(
        text = text,
        color = WpTextSecondary,
        fontSize = 15.sp,
        lineHeight = 24.sp,
        modifier = Modifier.padding(bottom = 16.dp)
    )
}

@Composable
fun WpHardParagraph(text: String, color: Color = WpTextPrimary) {
    Text(
        text = text,
        color = color,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 26.sp,
        modifier = Modifier.padding(bottom = 16.dp)
    )
}

@Composable
fun WpCardContainer(accentColor: Color, content: @Composable ColumnScope.() -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = WpCard),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .border(1.dp, accentColor.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            .drawBehind {
                drawRoundRect(
                    color = accentColor.copy(alpha = 0.05f),
                    size = size,
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(16.dp.toPx())
                )
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            content = content
        )
    }
}

@Composable
fun Page01() {
    WpHeader("01", "MENGAPA NOXCRASH DICIPTAKAN?", AccentCyan)
    
    WpParagraph("NoxCrash dibuat sebagai game jangka panjang yang mengambil inspirasi dari konsep mining dan sistem ekonomi digital, kemudian mengubahnya menjadi pengalaman bermain yang sepenuhnya berada di dalam ekosistem game.")
    
    WpParagraph("Tujuan NoxCrash bukan sekadar membuat simulasi mining sederhana, tetapi membangun sebuah sistem permainan yang memiliki progression, ekonomi virtual, strategi, dan mekanisme yang dapat berkembang dalam jangka panjang.")
    
    WpCardContainer(accentColor = AccentCyan) {
        Text(
            text = "NOXCRASH ADALAH GAME.",
            color = AccentCyan,
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 2.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun Page02() {
    WpHeader("02", "KONSEP MINING NOXCRASH", AccentBlue)
    
    WpParagraph("Mining di NoxCrash merupakan mekanisme permainan yang memberikan pemain pengalaman mengelola miner, memahami kecepatan, reward, waktu aktif, dan perkembangan ekonomi virtual.")
    
    WpCardContainer(accentColor = AccentBlue) {
        WpHardParagraph("Mining tidak menggunakan konsep timer sederhana seperti '2 NX setiap 24 jam'.")
        
        WpParagraph("Reward ditentukan oleh sistem matematika internal NoxCrash yang menghubungkan parameter miner, hashrate, difficulty, reward rate, elapsed time, dan target reward.")
    }
    
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = "Waktu penyelesaian miner bukan angka yang ditentukan secara manual. Waktu tersebut merupakan hasil dari sistem.",
        color = AccentBlue,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 24.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
    )
}

@Composable
fun Page03() {
    WpHeader("03", "NOXCRASH HASHRATE", AccentGreen)
    
    WpParagraph("NoxCrash menggunakan satuan hashrate internal yang terinspirasi dari sistem mining cryptocurrency.")
    
    WpCardContainer(accentColor = AccentGreen) {
        WpHardParagraph("HX adalah NoxCrash Hash, yaitu satuan aktivitas mining internal NoxCrash.", AccentGreen)
        Spacer(modifier = Modifier.height(8.dp))
        WpParagraph("Hashrate NoxCrash bukan hashrate Bitcoin dan bukan ukuran kemampuan hardware nyata.")
    }
    
    Spacer(modifier = Modifier.height(16.dp))
    
    val units = listOf(
        "1 KHX/S" to "1.000 HX/S",
        "1 MHX/S" to "1.000 KHX/S",
        "1 GHX/S" to "1.000 MHX/S",
        "1 THX/S" to "1.000 GHX/S",
        "1 PHX/S" to "1.000 THX/S",
        "1 EHX/S" to "1.000 PHX/S"
    )
    
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = WpCardSecondary),
        modifier = Modifier.fillMaxWidth().border(1.dp, WpDivider, RoundedCornerShape(12.dp))
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            units.forEachIndexed { index, pair ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = pair.first, color = AccentGreen, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(text = "=", color = WpTextSecondary, fontSize = 16.sp)
                    Text(text = pair.second, color = WpTextPrimary, fontWeight = FontWeight.Medium, fontSize = 16.sp)
                }
                if (index < units.size - 1) {
                    HorizontalDivider(color = WpDivider, thickness = 1.dp)
                }
            }
        }
    }
}

@Composable
fun Page04() {
    WpHeader("04", "SUPPLY NX", AccentGold)
    
    WpParagraph("Setiap instalasi NoxCrash memiliki supply NX mining tersendiri sebesar 50.000.000 NX.")
    
    WpCardContainer(accentColor = AccentGold) {
        Text(
            text = "PENTING",
            color = AccentGold,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 14.sp,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        WpHardParagraph("50.000.000 NX bukan supply global yang dibagi oleh seluruh pemain NoxCrash.")
        
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("User A", color = WpTextSecondary, fontSize = 14.sp)
                Text("50M NX", color = AccentGold, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("User B", color = WpTextSecondary, fontSize = 14.sp)
                Text("50M NX", color = AccentGold, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("User C", color = WpTextSecondary, fontSize = 14.sp)
                Text("50M NX", color = AccentGold, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Setiap instalasi memiliki supply mining sendiri.",
            color = WpTextSecondary,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
    
    Spacer(modifier = Modifier.height(16.dp))
    WpParagraph("Supply berkurang ketika reward mining benar-benar dihasilkan.")
    WpHardParagraph("Pembelian miner tidak mengurangi supply mining.", color = WpTextPrimary)
}

@Composable
fun Page05() {
    WpHeader("05", "DIBANGUN UNTUK JANGKA PANJANG", AccentOrange)
    
    WpParagraph("NoxCrash dirancang sebagai game jangka panjang, bukan simulasi mining yang dibuat untuk selesai dalam waktu singkat.")
    
    WpParagraph("Parameter supply, difficulty, hashrate, reward rate, progression miner, dan sistem ekonomi dirancang agar perjalanan game berlangsung dalam skala waktu yang panjang.")
    
    WpCardContainer(accentColor = AccentOrange) {
        Text(
            text = "150 TAHUN",
            color = AccentOrange,
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Target ekonomi jangka panjang digunakan sebagai referensi matematis dalam perancangan sistem, bukan sebagai timer yang dipaksakan kepada pemain.",
            color = WpTextPrimary,
            fontSize = 14.sp,
            lineHeight = 22.sp,
            textAlign = TextAlign.Center
        )
    }
    
    Spacer(modifier = Modifier.height(16.dp))
    WpParagraph("Model ekonomi menggunakan horizon jangka panjang sekitar 150 tahun sebagai salah satu referensi desain matematis.")
}

@Composable
fun Page06() {
    WpHeader("06", "PERKEMBANGAN MINER", AccentCyan) // The header is cyan, but cards will use gradient-like progression
    
    WpParagraph("Miner NoxCrash memiliki tingkatan kemampuan yang berbeda.")
    
    val miners = listOf(
        "FREE MINER" to Color(0xFF00E5FF),
        "BASIC MINER" to Color(0xFF2979FF),
        "SLOW MINER" to Color(0xFF7C4DFF),
        "FAST MINER" to Color(0xFFFF9800),
        "VOID MINER" to Color(0xFFFFD54F) // We can use Gold or Void purple here
    )
    
    Column(modifier = Modifier.fillMaxWidth()) {
        miners.forEachIndexed { index, pair ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = WpCardSecondary),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .border(1.dp, pair.second.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            ) {
                Text(
                    text = pair.first,
                    color = pair.second,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    letterSpacing = 2.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                )
            }
            if (index < miners.size - 1) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("↓", color = WpTextSecondary, fontSize = 20.sp)
                }
            }
        }
    }
    
    Spacer(modifier = Modifier.height(24.dp))
    WpParagraph("Perbedaan miner ditentukan oleh parameter ekonomi dan matematika internal, bukan sekadar angka durasi.")
    WpHardParagraph("Miner dengan tingkat lebih tinggi memiliki karakteristik mining yang berbeda, termasuk hashrate dan target reward.")
}

@Composable
fun Page07() {
    WpHeader("07", "AI MODE", AccentSoftPurple)
    
    WpParagraph("AI Mode merupakan sistem automation internal NoxCrash yang berbeda dari Miner Market.")
    
    val aiTiers = listOf(
        Pair("AI SCOUT", Color(0xFF00E5FF)) to "Monitoring",
        Pair("AI SMART", Color(0xFFE040FB)) to "Analysis dan strategy",
        Pair("AI PRO", Color(0xFFFF9800)) to "Advanced optimization",
        Pair("AI VOID", Color(0xFF7C4DFF)) to "Automation tertinggi"
    )
    
    Column(modifier = Modifier.fillMaxWidth()) {
        aiTiers.forEach { (pair, desc) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .background(WpCardSecondary, RoundedCornerShape(12.dp))
                    .border(1.dp, pair.second.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = pair.first,
                    color = pair.second,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = desc,
                    color = WpTextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
    
    Spacer(modifier = Modifier.height(16.dp))
    WpCardContainer(accentColor = Color(0xFF7C4DFF)) {
        Text("AI VOID MEMILIKI:", color = Color(0xFF7C4DFF), fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text("• AUTO CLAIM\n• AUTO PURCHASE\n• AUTO MANAGEMENT", color = WpTextPrimary, lineHeight = 24.sp, fontWeight = FontWeight.Medium)
    }
    
    Spacer(modifier = Modifier.height(16.dp))
    WpHardParagraph("AI Mode bukan Android auto-clicker.", AccentCyan)
    WpHardParagraph("AI Mode bekerja melalui logic dan state internal NoxCrash.", AccentSoftPurple)
}

@Composable
fun Page08() {
    WpHeader("08", "NX DAN EKONOMI GAME", AccentGold)
    
    WpParagraph("NX merupakan mata uang virtual yang digunakan dalam ekosistem permainan NoxCrash.")
    
    WpCardContainer(accentColor = AccentCyan) {
        Text("NX digunakan untuk berbagai mekanisme game seperti:", color = WpTextPrimary, fontWeight = FontWeight.Medium, modifier = Modifier.padding(bottom = 8.dp))
        Text("• Pembelian miner\n• Progression\n• Fitur game\n• AI Mode", color = AccentCyan, lineHeight = 24.sp, fontWeight = FontWeight.Bold)
    }
    
    Spacer(modifier = Modifier.height(16.dp))
    
    WpParagraph("Nilai dan mekanisme NX merupakan bagian dari ekonomi internal game.")
    WpHardParagraph("NX tidak boleh dianggap sebagai pendapatan tetap atau jaminan keuntungan.", AccentGold)
}

@Composable
fun Page09() {
    WpHeader("09", "MATEMATIKA DI BALIK NOXCRASH", AccentViolet)
    
    WpParagraph("NoxCrash menggunakan sistem matematika untuk menjaga hubungan antara hashrate, reward, difficulty, elapsed time, target reward, dan supply.")
    
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = WpCardSecondary),
        modifier = Modifier.fillMaxWidth().border(1.dp, AccentViolet.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val steps = listOf(
                "HASHRATE",
                "DIFFICULTY",
                "REWARD RATE",
                "ELAPSED TIME",
                "ACCUMULATED REWARD",
                "TARGET REWARD",
                "MINER COMPLETED"
            )
            
            steps.forEachIndexed { index, step ->
                Text(
                    text = step,
                    color = if (index == 0 || index == steps.size - 1) AccentViolet else WpTextPrimary,
                    fontWeight = if (index == 0 || index == steps.size - 1) FontWeight.ExtraBold else FontWeight.Bold,
                    fontSize = if (index == 0 || index == steps.size - 1) 16.sp else 14.sp,
                    letterSpacing = 1.sp
                )
                if (index < steps.size - 1) {
                    Text(
                        text = "↓",
                        color = AccentViolet.copy(alpha = 0.6f),
                        fontSize = 20.sp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
    
    Spacer(modifier = Modifier.height(24.dp))
    WpHardParagraph("Durasi bukan parameter yang ditentukan secara manual.")
    WpHardParagraph("Durasi adalah hasil dari perhitungan sistem.", AccentViolet)
}

@Composable
fun Page10() {
    WpHeader("10", "FILOSOFI", AccentCyan)
    
    WpParagraph("NoxCrash dibuat untuk memberikan sensasi mengelola sistem mining virtual tanpa menjadikan game ini sebagai pekerjaan atau sumber penghasilan utama.")
    
    WpParagraph("NoxCrash mengutamakan pengalaman bermain, progression, eksperimen sistem, strategi, dan perjalanan jangka panjang.")
    
    WpCardContainer(accentColor = AccentSoftPurple) {
        Text(
            text = "Game ini dibuat untuk dimainkan dan dinikmati, bukan untuk menggantikan kehidupan nyata.",
            color = AccentSoftPurple,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 24.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun Page11() {
    WpHeader("11", "MASA DEPAN NOXCRASH", AccentBlue)
    
    WpParagraph("NoxCrash dirancang sebagai sistem yang dapat berkembang.")
    
    WpParagraph("Perkembangan game dapat mencakup miner baru, sistem ekonomi baru, fitur automation, statistik, progression, dan mekanisme game lainnya.")
    
    WpCardContainer(accentColor = AccentViolet) {
        Text(
            text = "Setiap perubahan ekonomi harus tetap mempertimbangkan keseimbangan sistem dan keberlanjutan jangka panjang game.",
            color = WpTextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 22.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun Page12() {
    Column(
        modifier = Modifier.fillMaxSize().padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "NOXCRASH",
            color = AccentCyan,
            fontSize = 48.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 4.sp,
            style = androidx.compose.ui.text.TextStyle(
                shadow = androidx.compose.ui.graphics.Shadow(
                    color = AccentCyan.copy(alpha = 0.5f),
                    blurRadius = 20f
                )
            )
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "BUILT FOR THE LONG RUN.",
            color = AccentViolet,
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 2.sp,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        WpParagraph("NoxCrash bukan sekadar simulasi mining.")
        WpParagraph("Ini adalah game dengan sistem ekonomi virtual, progression, matematika, strategi, dan perjalanan yang dirancang untuk jangka panjang.")
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = WpCardSecondary),
            modifier = Modifier.fillMaxWidth().border(1.dp, AccentCyan.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "MAIN DENGAN BIJAK.",
                    color = WpTextPrimary,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "NOXCRASH = GAME / HIBURAN",
                    color = AccentCyan,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

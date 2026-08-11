sed -i '663,$d' app/src/main/java/com/example/HomeScreen.kt
cat << 'INNER_EOF' >> app/src/main/java/com/example/HomeScreen.kt
@Composable
fun ActiveMinersCard() {
    val miningStatus by MiningManager.miningStatus.collectAsState()
    val activeMiner by MiningManager.activeMiner.collectAsState()
    var currentTime by remember { mutableLongStateOf(System.currentTimeMillis()) }
    val coroutineScope = rememberCoroutineScope()
    var isClaiming by remember { mutableStateOf(false) }

    LaunchedEffect(miningStatus) {
        if (miningStatus == MiningStatus.ACTIVE) {
            while (true) {
                delay(1000)
                currentTime = System.currentTimeMillis()
            }
        }
    }

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = ActiveMinersCardBg),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0x0DFFFFFF), RoundedCornerShape(24.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "⛏️ MINER YANG AKTIF",
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(24.dp))
            
            if (miningStatus == MiningStatus.OFF || activeMiner == null) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Belum ada miner yang aktif.", color = TextSecondary, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Pergi ke Miner Shop untuk membeli miner.",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                val miner = activeMiner!!
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = miner.name.uppercase(),
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "HASHRATE: ${NoxEconomyConfig.getHashrateString(miner.hashrate)}",
                            color = ColorMining,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    if (miningStatus == MiningStatus.COMPLETED) {
                        Button(
                            onClick = {
                                if (isClaiming) return@Button
                                isClaiming = true
                                coroutineScope.launch {
                                    MiningManager.claimReward()
                                    isClaiming = false
                                }
                            },
                            enabled = !isClaiming,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF64DD17)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                "CLAIM",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .background(ColorMining.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("AKTIF", color = ColorMining, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                val currentReward = miner.calculateCurrentReward(currentTime)
                Text(
                    text = "Reward: ${currentReward.toNXFormat()} / ${miner.targetReward.toShortNXFormat()}",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                val elapsedMs = (currentTime - miner.startedAt).coerceAtLeast(0)
                val elapsedSeconds = elapsedMs / 1000
                val days = elapsedSeconds / (24 * 3600)
                val hours = (elapsedSeconds % (24 * 3600)) / 3600
                val minutes = (elapsedSeconds % 3600) / 60
                val seconds = elapsedSeconds % 60
                val timeString = if (days > 0) {
                    "$days hari $hours jam $minutes menit $seconds detik"
                } else {
                    "$hours jam $minutes menit $seconds detik"
                }
                
                Text(
                    text = "Total Aktif: $timeString",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun SupplyCard() {
    val currentSupply by SupplyManager.currentSupply.collectAsState()

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, ColorMining.copy(alpha = 0.3f), RoundedCornerShape(24.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "NX SUPPLY",
                color = ColorMining,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "SISA SUPPLY",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            
            val formattedSupply = java.text.DecimalFormat("#,###.########", java.text.DecimalFormatSymbols(java.util.Locale("id", "ID"))).format(currentSupply)
            Text(
                text = "$formattedSupply NX",
                color = TextPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Supply tersedia untuk ditambang",
                color = TextSecondary.copy(alpha = 0.7f),
                fontSize = 11.sp
            )
        }
    }
}
INNER_EOF

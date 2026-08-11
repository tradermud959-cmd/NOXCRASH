sed -i '/val miner = activeMiner!!/,/fontSize = 12.sp/c\
                val miner = activeMiner!!\
                val currentReward = miner.calculateCurrentReward(currentTime)\
                val currentRewardString = currentReward.toNXFormat()\
                \
                Text(\
                    text = currentRewardString,\
                    maxLines = 1,\
                    softWrap = false,\
                    color = TextPrimary,\
                    fontSize = 20.sp,\
                    fontWeight = FontWeight.Bold\
                )\
                \
                Spacer(modifier = Modifier.height(16.dp))\
                val elapsedMs = (currentTime - miner.startedAt).coerceAtLeast(0)\
                val elapsedSeconds = elapsedMs / 1000\
                val days = elapsedSeconds / (24 * 3600)\
                val hours = (elapsedSeconds % (24 * 3600)) / 3600\
                val minutes = (elapsedSeconds % 3600) / 60\
                val seconds = elapsedSeconds % 60\
                val timeString = if (days > 0) {\
                    "$days hari $hours jam $minutes menit $seconds detik"\
                } else {\
                    "$hours jam $minutes menit $seconds detik"\
                }\
                \
                Text(\
                    text = "TOTAL AKTIF",\
                    color = TextSecondary.copy(alpha = 0.7f),\
                    fontSize = 10.sp,\
                    letterSpacing = 1.sp,\
                    fontWeight = FontWeight.Bold\
                )\
                Text(\
                    text = timeString,\
                    color = TextSecondary,\
                    fontSize = 14.sp\
                )\
                \
                Spacer(modifier = Modifier.height(8.dp))\
                \
                Text(\
                    text = "HASHRATE",\
                    color = TextSecondary.copy(alpha = 0.7f),\
                    fontSize = 10.sp,\
                    letterSpacing = 1.sp,\
                    fontWeight = FontWeight.Bold\
                )\
                Text(\
                    text = NoxEconomyConfig.getHashrateString(miner.hashrate),\
                    color = ColorMining,\
                    fontSize = 14.sp,\
                    fontWeight = FontWeight.Bold\
                )\
                \
                Spacer(modifier = Modifier.height(8.dp))\
                Text(\
                    text = "ESTIMASI REWARD: ${miner.targetReward.toShortNXFormat()}",\
                    color = TextSecondary,\
                    fontSize = 12.sp\
                )' app/src/main/java/com/example/HomeScreen.kt

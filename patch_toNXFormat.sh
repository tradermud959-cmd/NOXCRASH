sed -i 's/${totalReward.toPlainString()} NX/${totalReward.toNXFormat()}/g' app/src/main/java/com/example/StatisticsScreen.kt
sed -i 's/${item.price.toPlainString()} NX/${item.price.toNXFormat()}/g' app/src/main/java/com/example/AIHistoryScreen.kt
sed -i 's/-${config.price.toPlainString()} NX/-${config.price.toNXFormat()}/g' app/src/main/java/com/example/AIShopScreen.kt
sed -i 's/+${reward.toPlainString()} NX/+${reward.toNXFormat()}/g' app/src/main/java/com/example/MiningManager.kt
sed -i 's/-${price.toPlainString()} NX/-${price.toNXFormat()}/g' app/src/main/java/com/example/AIManager.kt
sed -i 's/${miner.reward.toPlainString()} NX/${miner.reward.toNXFormat()}/g' app/src/main/java/com/example/HomeScreen.kt

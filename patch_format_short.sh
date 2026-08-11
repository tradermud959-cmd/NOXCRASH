sed -i 's/reward.toNXFormat()/reward.toShortNXFormat()/g' app/src/main/java/com/example/MiningManager.kt
sed -i 's/miner.reward.toNXFormat()/miner.reward.toShortNXFormat()/g' app/src/main/java/com/example/HomeScreen.kt
sed -i 's/config.price.toNXFormat()/config.price.toShortNXFormat()/g' app/src/main/java/com/example/AIShopScreen.kt
sed -i 's/java.math.BigDecimal(priceLabel).toNXFormat()/java.math.BigDecimal(priceLabel).toShortNXFormat()/g' app/src/main/java/com/example/AIShopScreen.kt
sed -i 's/java.math.BigDecimal(price).toNXFormat()/java.math.BigDecimal(price).toShortNXFormat()/g' app/src/main/java/com/example/MinerShopScreen.kt
sed -i 's/java.math.BigDecimal(priceLabel).toNXFormat()/java.math.BigDecimal(priceLabel).toShortNXFormat()/g' app/src/main/java/com/example/MinerShopScreen.kt
sed -i 's/item.price.toNXFormat()/item.price.toShortNXFormat()/g' app/src/main/java/com/example/AIHistoryScreen.kt
sed -i 's/price.toNXFormat()/price.toShortNXFormat()/g' app/src/main/java/com/example/AIManager.kt

sed -i 's/"-$price NX"/"-${java.math.BigDecimal(price).toNXFormat()}"/g' app/src/main/java/com/example/MinerShopScreen.kt
sed -i 's/"$priceLabel NX"/java.math.BigDecimal(priceLabel).toNXFormat()/g' app/src/main/java/com/example/MinerShopScreen.kt
sed -i 's/"$priceLabel NX"/java.math.BigDecimal(priceLabel).toNXFormat()/g' app/src/main/java/com/example/AIShopScreen.kt

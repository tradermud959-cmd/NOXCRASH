sed -i 's/durationLabel = "3 HARI",/durationLabel = "${AIEconomy.scout.durationDays} HARI",/g' app/src/main/java/com/example/AIShopScreen.kt
sed -i 's/priceLabel = "75",/priceLabel = AIEconomy.scout.priceString,/g' app/src/main/java/com/example/AIShopScreen.kt
sed -i 's/onActionClick = { onPurchaseClick(AIType.SCOUT, "AI Scout", 3, "75") }/onActionClick = { onPurchaseClick(AIEconomy.scout) }/g' app/src/main/java/com/example/AIShopScreen.kt

sed -i 's/durationLabel = "7 HARI",/durationLabel = "${AIEconomy.smart.durationDays} HARI",/g' app/src/main/java/com/example/AIShopScreen.kt
sed -i 's/priceLabel = "125",/priceLabel = AIEconomy.smart.priceString,/g' app/src/main/java/com/example/AIShopScreen.kt
sed -i 's/onActionClick = { onPurchaseClick(AIType.SMART, "AI Smart", 7, "125") }/onActionClick = { onPurchaseClick(AIEconomy.smart) }/g' app/src/main/java/com/example/AIShopScreen.kt

sed -i 's/durationLabel = "14 HARI",/durationLabel = "${AIEconomy.pro.durationDays} HARI",/g' app/src/main/java/com/example/AIShopScreen.kt
sed -i 's/priceLabel = "200",/priceLabel = AIEconomy.pro.priceString,/g' app/src/main/java/com/example/AIShopScreen.kt
sed -i 's/onActionClick = { onPurchaseClick(AIType.PRO, "AI Pro", 14, "200") }/onActionClick = { onPurchaseClick(AIEconomy.pro) }/g' app/src/main/java/com/example/AIShopScreen.kt

sed -i 's/durationLabel = "30 HARI",/durationLabel = "${AIEconomy.void.durationDays} HARI",/g' app/src/main/java/com/example/AIShopScreen.kt
sed -i 's/priceLabel = "300",/priceLabel = AIEconomy.void.priceString,/g' app/src/main/java/com/example/AIShopScreen.kt
sed -i 's/onActionClick = { onPurchaseClick(AIType.VOID, "AI Void", 30, "300") }/onActionClick = { onPurchaseClick(AIEconomy.void) }/g' app/src/main/java/com/example/AIShopScreen.kt

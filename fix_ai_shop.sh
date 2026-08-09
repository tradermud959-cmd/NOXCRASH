sed -i '/if (AIManager.purchaseAI(aiType, name, durationDays, priceDec)) {/a\
                HistoryManager.addHistory(HistoryType.PURCHASE, "🤖 ${name.uppercase()}", "AI Mode dibeli", "-$price NX")\
                StatisticsManager.addPurchase()' app/src/main/java/com/example/AIShopScreen.kt

sed -i '/val onPurchaseClick = { aiType: AIType, name: String, durationDays: Long, price: String ->/,/                StatisticsManager.addPurchase()/c\
    val onPurchaseClick = { config: AIConfig ->\
        if (miningStatus != MiningStatus.OFF) {\
            showNotification(\
                NotificationType.WARNING,\
                "☠️ MINER MASIH AKTIF BJIR",\
                "Mining dulu sampai selesai sebelum menggunakan AI Mode."\
            )\
        } else if (aiState != AIState.NO_AI) {\
            showNotification(\
                NotificationType.WARNING,\
                "⚠️ AI MODE MASIH AKTIF ☠️",\
                "Nonaktifkan AI Mode terlebih dahulu sebelum membeli AI baru."\
            )\
        } else {\
            if (AIManager.purchaseAI(config.type, config.name, config.durationDays, config.price)) {\
                HistoryManager.addHistory(HistoryType.PURCHASE, "🤖 ${config.name.uppercase()}", "AI Mode dibeli", "-${config.price.toPlainString()} NX")\
                StatisticsManager.addPurchase()' app/src/main/java/com/example/AIShopScreen.kt

sed -i '/SettingsItem(title = "🪙 Info Koin NX", description = "Penjelasan sistem NX") {/,/                }/a\
                SettingsItem(title = "⚠️ Informasi Penting", description = "Tujuan NoxCrash sebagai game") {\
                    navController.navigate("info_penting")\
                }' app/src/main/java/com/example/SettingsScreen.kt

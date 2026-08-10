sed -i '/SettingsItem(title = "🪙 Info Koin NX", description = "Penjelasan sistem NX") {/,/                }/c\
                SettingsItem(title = "🪙 Info Koin NX", description = "Penjelasan sistem NX") {\
                    navController.navigate("info_nx")\
                }\
                SettingsItem(title = "⚠️ Informasi Penting", description = "Tujuan NoxCrash sebagai game") {\
                    navController.navigate("info_penting")\
                }' app/src/main/java/com/example/SettingsScreen.kt

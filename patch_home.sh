sed -i '/item {/!b;n;/MiningCard()/!b;n;/}/!b;a\
        item {\
            AIModeCard(navController)\
        }' app/src/main/java/com/example/HomeScreen.kt

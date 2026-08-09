sed -i 's/MiningCardBg/ActiveMinersCardBg/g' app/src/main/java/com/example/HomeScreen.kt
sed -i 's/AIManager.refreshAIState()/AIManager.refreshState()/g' app/src/main/java/com/example/HomeScreen.kt
sed -i 's/AIState.SCOUT/AIState.AI_SCOUT_ACTIVE/g' app/src/main/java/com/example/HomeScreen.kt
sed -i 's/AIState.SMART/AIState.AI_SMART_ACTIVE/g' app/src/main/java/com/example/HomeScreen.kt
sed -i 's/AIState.PRO/AIState.AI_PRO_ACTIVE/g' app/src/main/java/com/example/HomeScreen.kt
sed -i 's/AIState.VOID/AIState.AI_VOID_ACTIVE/g' app/src/main/java/com/example/HomeScreen.kt
sed -i 's/AIManager.aiExpiresAt.value/AIManager.activeAI.value?.expiresAt ?: 0L/g' app/src/main/java/com/example/HomeScreen.kt
sed -i 's/profileData.bannerUri/profileData.coverUri/g' app/src/main/java/com/example/HomeScreen.kt
sed -i 's/profileData.avatarUri/profileData.photoUri/g' app/src/main/java/com/example/HomeScreen.kt
sed -i 's/${profileData.level}/1/g' app/src/main/java/com/example/HomeScreen.kt
sed -i 's/${profileData.rank}/100/g' app/src/main/java/com/example/HomeScreen.kt
sed -i 's/ColorProfileAccent/ColorProfile/g' app/src/main/java/com/example/HomeScreen.kt
sed -i 's/MusicCardBg/MusicSurface/g' app/src/main/java/com/example/HomeScreen.kt

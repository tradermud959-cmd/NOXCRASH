sed -i 's/delay(1000)/delay(50)/g' app/src/main/java/com/example/HomeScreen.kt
sed -i 's/MiningManager.refreshState()/\/\/ Moving refreshState to separate coroutine\n                if (currentTime % 1000 < 50) MiningManager.refreshState()/g' app/src/main/java/com/example/HomeScreen.kt

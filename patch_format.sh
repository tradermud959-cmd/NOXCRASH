# Fix ProfileManager
sed -i 's/0.00000000 NX/0.000000000000000 NX/g' app/src/main/java/com/example/ProfileManager.kt
sed -i 's/%.8f NX/%.15f NX/g' app/src/main/java/com/example/ProfileManager.kt

# Fix HomeScreen
sed -i 's/%.8f NX/%.15f NX/g' app/src/main/java/com/example/HomeScreen.kt

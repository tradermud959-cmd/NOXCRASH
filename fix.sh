sed -i '/import androidx.compose.material3.TabRowDefaults/d' app/src/main/java/com/example/MinerShopScreen.kt
sed -i '/import androidx.compose.material3.Tab/d' app/src/main/java/com/example/MinerShopScreen.kt
sed -i 's/import androidx.compose.material3.\*/import androidx.compose.material3.*\nimport androidx.compose.material3.Tab\nimport androidx.compose.material3.TabRow\nimport androidx.compose.material3.TabRowDefaults\nimport androidx.compose.material3.TabRowDefaults.tabIndicatorOffset/' app/src/main/java/com/example/MinerShopScreen.kt

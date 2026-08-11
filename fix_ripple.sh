sed -i 's/\.androidx\.compose\.foundation\.clickable(/ \.clip(androidx.compose.foundation.shape.RoundedCornerShape(14.dp))\n            \.clickable/g' app/src/main/java/com/example/NoxCrashApp.kt
sed -i '/interactionSource = remember/,/indication = /d' app/src/main/java/com/example/NoxCrashApp.kt

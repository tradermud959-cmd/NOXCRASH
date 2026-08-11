sed -i 's/\.clickable/\.clickable { onNavigate(route) }/g' app/src/main/java/com/example/NoxCrashApp.kt
sed -i '/) { onNavigate(route) }/d' app/src/main/java/com/example/NoxCrashApp.kt

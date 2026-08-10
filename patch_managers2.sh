echo "\n    fun reload() { loadHistory() }\n}" >> app/src/main/java/com/example/HistoryManager.kt
sed -i 's/^}$//' app/src/main/java/com/example/HistoryManager.kt
sed -i -e '$ d' app/src/main/java/com/example/HistoryManager.kt

echo "\n    fun reload() { loadStats() }\n}" >> app/src/main/java/com/example/StatisticsManager.kt
sed -i 's/^}$//' app/src/main/java/com/example/StatisticsManager.kt
sed -i -e '$ d' app/src/main/java/com/example/StatisticsManager.kt

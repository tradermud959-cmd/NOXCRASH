sed -i 's/text = currentRewardString,/text = currentRewardString,\n                    maxLines = 1,\n                    softWrap = false,/g' app/src/main/java/com/example/HomeScreen.kt
sed -i 's/fontSize = 32.sp,/fontSize = 20.sp,/g' app/src/main/java/com/example/HomeScreen.kt
sed -i 's/text = profileData.balance,/text = profileData.balance,\n                        maxLines = 1,\n                        softWrap = false,/g' app/src/main/java/com/example/HomeScreen.kt

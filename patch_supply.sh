cat << 'INNER_EOF' >> app/src/main/java/com/example/HomeScreen.kt

@Composable
fun SupplyCard() {
    val currentSupply by SupplyManager.currentSupply.collectAsState()

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, ColorMining.copy(alpha = 0.3f), RoundedCornerShape(24.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "NX SUPPLY",
                color = ColorMining,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "SISA SUPPLY",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            
            val formattedSupply = java.text.DecimalFormat("#,###", java.text.DecimalFormatSymbols(java.util.Locale.US)).format(currentSupply)
            Text(
                text = "$formattedSupply NX",
                color = TextPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Supply tersedia untuk ditambang",
                color = TextSecondary.copy(alpha = 0.7f),
                fontSize = 11.sp
            )
        }
    }
}
INNER_EOF

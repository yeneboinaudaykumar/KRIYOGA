package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.SatelliteAlt
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.FarmPlotEntity
import com.example.data.local.FarmerProfileEntity
import com.example.model.ForecastDay
import com.example.model.KrishiStrings
import com.example.model.Language
import com.example.model.WeatherInfo
import com.example.ui.AppNavTab
import com.example.ui.components.FarmerCalculatorDialog
import com.example.ui.components.IndianCalendarDialog
import com.example.ui.components.WeatherAlertBanner

@Composable
fun HomeScreen(
    farmerProfile: FarmerProfileEntity?,
    farmPlots: List<FarmPlotEntity>,
    weather: WeatherInfo,
    financialSummary: Pair<Triple<Double, Double, Double>, Double>,
    currentLanguage: Language,
    onNavigateTab: (AppNavTab) -> Unit,
    onAddPlotClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val strings = KrishiStrings.get(currentLanguage)
    val farmerName = farmerProfile?.name ?: if (currentLanguage == Language.TELUGU) "రైతు మిత్ర" else if (currentLanguage == Language.HINDI) "किसान भाई" else "Farmer"
    val greeting = strings.greeting(farmerName)
    val subtitle = strings.homeSubtitle

    var showCalendarDialog by remember { mutableStateOf(false) }
    var showCalculatorDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen_content"),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Hero Card with Generated Agricultural Art
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth().height(160.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.krishi_farm_hero_1790946140552),
                        contentDescription = "Indian Agriculture Farm Hero",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f)),
                                    startY = 50f
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = greeting,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }
        }

        // Weather Alert Banner (Rainfall / Extreme Weather warning)
        item {
            WeatherAlertBanner(
                weather = weather,
                currentLanguage = currentLanguage,
                onDismiss = {}
            )
        }

        // Weather & Rainfall Agromet Radar Card
        item {
            WeatherCard(weather = weather, currentLanguage = currentLanguage)
        }

        // Indian Festival Calendar & Kisan Agro Calculator Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = when (currentLanguage) {
                            Language.TELUGU -> "రైతు ప్రత్యేక సేవలు (Farmer Utilities)"
                            Language.HINDI -> "विशेष किसान सेवाएं (Farmer Utilities)"
                            Language.ENGLISH -> "Farmer Smart Utilities"
                        },
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // 1. Indian Calendar Option
                        ElevatedCard(
                            onClick = { showCalendarDialog = true },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("home_indian_calendar_card"),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.elevatedCardColors(containerColor = Color(0xFFFFF3E0))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFE65100)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CalendarMonth,
                                        contentDescription = "Indian Calendar",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = when (currentLanguage) {
                                        Language.TELUGU -> "భారతీయ క్యాలెండర్"
                                        Language.HINDI -> "भारतीय पंचांग"
                                        Language.ENGLISH -> "Indian Calendar"
                                    },
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color(0xFFBF360C)
                                )
                                Text(
                                    text = when (currentLanguage) {
                                        Language.TELUGU -> "${farmerProfile?.state ?: "రాష్ట్ర"} పండుగలు"
                                        Language.HINDI -> "${farmerProfile?.state ?: "राज्य"} के त्यौहार"
                                        Language.ENGLISH -> "${farmerProfile?.state ?: "State"} Festivals"
                                    },
                                    fontSize = 11.sp,
                                    color = Color.DarkGray
                                )
                            }
                        }

                        // 2. Farmer Calculator Option
                        ElevatedCard(
                            onClick = { showCalculatorDialog = true },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("home_farmer_calculator_card"),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.elevatedCardColors(containerColor = Color(0xFFE8F5E9))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF2E7D32)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Calculate,
                                        contentDescription = "Farmer Calculator",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = when (currentLanguage) {
                                        Language.TELUGU -> "సాధారణ కాలిక్యులేటర్"
                                        Language.HINDI -> "साधारण कैलकुलेटर"
                                        Language.ENGLISH -> "Calculator"
                                    },
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color(0xFF1B5E20)
                                )
                                Text(
                                    text = when (currentLanguage) {
                                        Language.TELUGU -> "కూడికలు & తీసివేతలు"
                                        Language.HINDI -> "सामान्य जोड़-घटाव"
                                        Language.ENGLISH -> "Basic Calculations"
                                    },
                                    fontSize = 11.sp,
                                    color = Color.DarkGray
                                )
                            }
                        }
                    }
                }
            }
        }

        // Quick Action Shortcuts Grid
        item {
            Text(
                text = strings.toolsTitle,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionItem(
                    title = strings.toolScanTitle,
                    subtitle = strings.toolScanSub,
                    icon = Icons.Default.PhotoCamera,
                    iconBg = Color(0xFF2E7D32),
                    modifier = Modifier.weight(1f).testTag("quick_action_scan"),
                    onClick = { onNavigateTab(AppNavTab.CROPS_HEALTH) }
                )
                QuickActionItem(
                    title = strings.toolMandiTitle,
                    subtitle = strings.toolMandiSub,
                    icon = Icons.Default.TrendingUp,
                    iconBg = Color(0xFFE65100),
                    modifier = Modifier.weight(1f).testTag("quick_action_mandi"),
                    onClick = { onNavigateTab(AppNavTab.MANDI_MARKET) }
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionItem(
                    title = strings.toolGuruTitle,
                    subtitle = strings.toolGuruSub,
                    icon = Icons.Default.Chat,
                    iconBg = Color(0xFF1565C0),
                    modifier = Modifier.weight(1f).testTag("quick_action_chat"),
                    onClick = { onNavigateTab(AppNavTab.AI_GURU) }
                )
                QuickActionItem(
                    title = strings.toolSoilTitle,
                    subtitle = strings.toolSoilSub,
                    icon = Icons.Default.SatelliteAlt,
                    iconBg = Color(0xFF6A1B9A),
                    modifier = Modifier.weight(1f).testTag("quick_action_soil"),
                    onClick = { onNavigateTab(AppNavTab.ADVISORY_SOIL) }
                )
            }
        }

        // Farm Plots Section
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${strings.myPlotsTitle} (${farmPlots.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                OutlinedButton(
                    onClick = onAddPlotClick,
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    modifier = Modifier.testTag("add_plot_quick_button")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Plot", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(strings.addPlotBtn, fontSize = 12.sp)
                }
            }
        }

        items(farmPlots) { plot ->
            FarmPlotCard(plot = plot, currentLanguage = currentLanguage)
        }

        // Financial Overview Card
        item {
            val (spent, revenue, netProfit) = financialSummary.first

            Spacer(modifier = Modifier.height(12.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clickable { onNavigateTab(AppNavTab.FINANCE) }
                    .testTag("home_financial_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = strings.financeOverviewTitle,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = strings.viewFinance,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(strings.totalRevenue, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                            Text("₹${revenue.toInt()}", fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32), fontSize = 16.sp)
                        }
                        Column {
                            Text(strings.totalSpent, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                            Text("₹${spent.toInt()}", fontWeight = FontWeight.Bold, color = Color(0xFFC62828), fontSize = 16.sp)
                        }
                        Column {
                            Text(strings.netProfit, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                            Text("₹${netProfit.toInt()}", fontWeight = FontWeight.Bold, color = if (netProfit >= 0) Color(0xFF2E7D32) else Color(0xFFC62828), fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }

    if (showCalendarDialog) {
        IndianCalendarDialog(
            userState = farmerProfile?.state ?: "Andhra Pradesh",
            currentLanguage = currentLanguage,
            onDismiss = { showCalendarDialog = false }
        )
    }

    if (showCalculatorDialog) {
        FarmerCalculatorDialog(
            currentLanguage = currentLanguage,
            onDismiss = { showCalculatorDialog = false }
        )
    }
}

@Composable
fun QuickActionItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconBg: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(iconBg.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconBg,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(text = title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = Color.Gray, fontSize = 11.sp)
            }
        }
    }
}

@Composable
fun WeatherCard(weather: WeatherInfo, currentLanguage: Language) {
    val strings = KrishiStrings.get(currentLanguage)

    val localizedCondition = when (currentLanguage) {
        Language.TELUGU -> "పాక్షికంగా మేఘావృతం"
        Language.HINDI -> "आंशिक बादल"
        Language.ENGLISH -> weather.condition
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("weather_forecast_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE8F5E9)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = weather.location,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B5E20)
                    )
                }
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF81C784).copy(alpha = 0.3f)
                ) {
                    Text(
                        text = "${strings.rainProb}: ${weather.rainProbability}%",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B5E20),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${weather.temperatureCelsius}°C",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1B5E20)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = localizedCondition,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF2E7D32)
                        )
                        Text(
                            text = "${strings.humidity}: ${weather.humidity}% • ${strings.wind}: ${weather.windKmh} km/h",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF4E342E)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = strings.radar7Day,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B5E20)
            )
            Spacer(modifier = Modifier.height(6.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(weather.forecastDays) { day ->
                    ForecastDayChip(day = day, currentLanguage = currentLanguage)
                }
            }
        }
    }
}

@Composable
fun ForecastDayChip(day: ForecastDay, currentLanguage: Language) {
    val localizedDay = when (day.dayName) {
        "Today" -> if (currentLanguage == Language.TELUGU) "నేడు" else if (currentLanguage == Language.HINDI) "आज" else "Today"
        "Tomorrow" -> if (currentLanguage == Language.TELUGU) "రేపు" else if (currentLanguage == Language.HINDI) "कल" else "Tomorrow"
        "Day 3" -> if (currentLanguage == Language.TELUGU) "3వ రోజు" else if (currentLanguage == Language.HINDI) "तीसरा दिन" else "Day 3"
        "Day 4" -> if (currentLanguage == Language.TELUGU) "4వ రోజు" else if (currentLanguage == Language.HINDI) "चौथा दिन" else "Day 4"
        "Day 5" -> if (currentLanguage == Language.TELUGU) "5వ రోజు" else if (currentLanguage == Language.HINDI) "पाँचवाँ दिन" else "Day 5"
        else -> day.dayName
    }

    val localizedAdvice = when (day.farmingAdvice) {
        "Hold Spraying" -> if (currentLanguage == Language.TELUGU) "పిచికారీ వాయిదా" else if (currentLanguage == Language.HINDI) "छिड़काव रोकें" else "Hold Spraying"
        "Drainage Prep" -> if (currentLanguage == Language.TELUGU) "కాల్వల సన్నద్ధత" else if (currentLanguage == Language.HINDI) "जल निकासी प्रबंध" else "Drainage Prep"
        "Safe for Weeding" -> if (currentLanguage == Language.TELUGU) "కలుపు తీయవచ్చు" else if (currentLanguage == Language.HINDI) "निराई के अनुकूल" else "Safe for Weeding"
        "Optimal Spray Day" -> if (currentLanguage == Language.TELUGU) "పిచికారీకి అనుకూలం" else if (currentLanguage == Language.HINDI) "छिड़काव का सही दिन" else "Optimal Spray Day"
        "Irrigation Needed" -> if (currentLanguage == Language.TELUGU) "తడి అందించాలి" else if (currentLanguage == Language.HINDI) "सिंचाई आवश्यक" else "Irrigation Needed"
        else -> day.farmingAdvice
    }

    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = localizedDay, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = "${day.maxTemp}° / ${day.minTemp}°", fontSize = 11.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = if (day.rainChance > 50) Color(0xFFFFEBEE) else Color(0xFFE8F5E9)
            ) {
                Text(
                    text = localizedAdvice,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp,
                    color = if (day.rainChance > 50) Color(0xFFC62828) else Color(0xFF2E7D32),
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
fun FarmPlotCard(plot: FarmPlotEntity, currentLanguage: Language) {
    val strings = KrishiStrings.get(currentLanguage)

    val localizedCrop = when {
        plot.primaryCrop.contains("Paddy", ignoreCase = true) -> if (currentLanguage == Language.TELUGU) "వరి (సాంబ మసూరి)" else if (currentLanguage == Language.HINDI) "धान (बासमती)" else plot.primaryCrop
        plot.primaryCrop.contains("Chilli", ignoreCase = true) -> if (currentLanguage == Language.TELUGU) "మిర్చి (గుంటూరు తేజ)" else if (currentLanguage == Language.HINDI) "मिर्च (तेजा)" else plot.primaryCrop
        else -> plot.primaryCrop
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = plot.plotName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "$localizedCrop • ${plot.acreage} ${strings.acresField}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = plot.soilType,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.WaterDrop, contentDescription = null, tint = Color(0xFF0288D1), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "${strings.currentMoisture}: ${plot.soilMoisturePercent}%", style = MaterialTheme.typography.bodySmall)
                }
                Text(text = "${strings.waterSource}: ${plot.irrigationSource}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Text(text = "pH: ${plot.soilPh}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
            }
        }
    }
}

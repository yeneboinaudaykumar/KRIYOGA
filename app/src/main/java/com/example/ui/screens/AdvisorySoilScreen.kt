package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ai.FertilizerRecommendation
import com.example.model.KrishiStrings
import com.example.model.Language
import com.example.model.NdviZoneData

@Composable
fun AdvisorySoilScreen(
    satelliteData: List<NdviZoneData>,
    fertilizerResult: FertilizerRecommendation?,
    currentLanguage: Language,
    onCalculateFertilizer: (crop: String, acres: Double, n: Int, p: Int, k: Int, ph: Double) -> Unit,
    modifier: Modifier = Modifier
) {
    val strings = KrishiStrings.get(currentLanguage)
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Soil & Fertilizer, 1: Satellite & IoT Monitoring

    // Form inputs for Soil Test
    var inputCrop by remember { mutableStateOf("Paddy") }
    var inputAcres by remember { mutableStateOf("2.5") }
    var inputN by remember { mutableStateOf("260") }
    var inputP by remember { mutableStateOf("24") }
    var inputK by remember { mutableStateOf("290") }
    var inputPh by remember { mutableStateOf("6.8") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("advisory_soil_screen")
    ) {
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text(strings.tabSoilHealth, fontWeight = FontWeight.Bold) },
                modifier = Modifier.testTag("tab_soil_health")
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text(strings.tabSatelliteNdvi, fontWeight = FontWeight.Bold) },
                modifier = Modifier.testTag("tab_satellite_ndvi")
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (selectedTab) {
                0 -> {
                    // Soil Health Card & Fertilizer Calculator
                    item {
                        Text(
                            text = strings.soilCardTitle,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = strings.soilCardDesc,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }

                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    OutlinedTextField(
                                        value = inputCrop,
                                        onValueChange = { inputCrop = it },
                                        label = { Text(strings.cropField) },
                                        modifier = Modifier.weight(1f).testTag("soil_crop_input")
                                    )
                                    OutlinedTextField(
                                        value = inputAcres,
                                        onValueChange = { inputAcres = it },
                                        label = { Text(strings.acresField) },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                        modifier = Modifier.weight(1f).testTag("soil_acres_input")
                                    )
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    OutlinedTextField(
                                        value = inputN,
                                        onValueChange = { inputN = it },
                                        label = { Text("N (kg/ha)") },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        modifier = Modifier.weight(1f).testTag("soil_n_input")
                                    )
                                    OutlinedTextField(
                                        value = inputP,
                                        onValueChange = { inputP = it },
                                        label = { Text("P (kg/ha)") },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        modifier = Modifier.weight(1f).testTag("soil_p_input")
                                    )
                                    OutlinedTextField(
                                        value = inputK,
                                        onValueChange = { inputK = it },
                                        label = { Text("K (kg/ha)") },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        modifier = Modifier.weight(1f).testTag("soil_k_input")
                                    )
                                }

                                OutlinedTextField(
                                    value = inputPh,
                                    onValueChange = { inputPh = it },
                                    label = { Text("pH (6.5 - 7.5)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    modifier = Modifier.fillMaxWidth().testTag("soil_ph_input")
                                )

                                Button(
                                    onClick = {
                                        val ac = inputAcres.toDoubleOrNull() ?: 2.0
                                        val n = inputN.toIntOrNull() ?: 240
                                        val p = inputP.toIntOrNull() ?: 20
                                        val k = inputK.toIntOrNull() ?: 260
                                        val ph = inputPh.toDoubleOrNull() ?: 6.8
                                        onCalculateFertilizer(inputCrop, ac, n, p, k, ph)
                                    },
                                    modifier = Modifier.fillMaxWidth().testTag("calculate_soil_button"),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Calculate, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(strings.calculateDoseBtn)
                                }
                            }
                        }
                    }

                    if (fertilizerResult != null) {
                        item {
                            FertilizerResultCard(result = fertilizerResult, currentLanguage = currentLanguage)
                        }
                    }

                    // Seasonal Crop Recommendations
                    item {
                        Text(
                            text = strings.recommendedSeasonsTitle,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    item {
                        SeasonalCropCard(
                            season = strings.kharifSeason,
                            months = if (currentLanguage == Language.TELUGU) "జూన్ - అక్టోబర్" else if (currentLanguage == Language.HINDI) "जून - अक्टूबर" else "June - October",
                            recommended = if (currentLanguage == Language.TELUGU) "వరి, పత్తి, సోయాబీన్, వేరుశనగ, మొక్కజొన్న" else if (currentLanguage == Language.HINDI) "धान, कपास, सोयाबीन, मूंगफली, मक्का" else "Paddy, Cotton, Soybean, Groundnut, Maize",
                            iconBg = Color(0xFF2E7D32)
                        )
                    }
                    item {
                        SeasonalCropCard(
                            season = strings.rabiSeason,
                            months = if (currentLanguage == Language.TELUGU) "అక్టోబర్ - మార్చి" else if (currentLanguage == Language.HINDI) "अक्टूबर - मार्च" else "October - March",
                            recommended = if (currentLanguage == Language.TELUGU) "గోధుమ, ఆవాలు, శనగలు, మిర్చి, టమోటా" else if (currentLanguage == Language.HINDI) "गेहूं, सरसों, चना, मिर्च, टमाटर" else "Wheat, Mustard, Chickpea, Chilli, Tomato",
                            iconBg = Color(0xFFE65100)
                        )
                    }
                    item {
                        SeasonalCropCard(
                            season = strings.zaidSeason,
                            months = if (currentLanguage == Language.TELUGU) "మార్చి - జూన్" else if (currentLanguage == Language.HINDI) "मार्च - जून" else "March - June",
                            recommended = if (currentLanguage == Language.TELUGU) "పుచ్చకాయ, దోసకాయ, పెసలు, కూరగాయలు" else if (currentLanguage == Language.HINDI) "तरबूज, खीरा, मूंग, सब्जियां" else "Watermelon, Cucumber, Moong Bean, Vegetables",
                            iconBg = Color(0xFF0288D1)
                        )
                    }
                }

                1 -> {
                    // Satellite NDVI Monitoring & IoT Sensors
                    item {
                        Text(
                            text = strings.satelliteTitle,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = strings.satelliteSubtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }

                    // Visual Field Health Map Canvas
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2E1D))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(strings.heatmapTitle, color = Color.White, fontWeight = FontWeight.Bold)
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color(0xFF2E7D32)
                                    ) {
                                        Text(strings.livePassToday, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFF243B27))
                                ) {
                                    Canvas(modifier = Modifier.fillMaxSize()) {
                                        val w = size.width
                                        val h = size.height

                                        drawRect(
                                            color = Color(0xFF2E7D32),
                                            topLeft = Offset(0f, 0f),
                                            size = androidx.compose.ui.geometry.Size(w * 0.6f, h)
                                        )
                                        drawRect(
                                            color = Color(0xFF8BC34A),
                                            topLeft = Offset(w * 0.6f, 0f),
                                            size = androidx.compose.ui.geometry.Size(w * 0.4f, h * 0.55f)
                                        )
                                        drawRect(
                                            color = Color(0xFFE65100),
                                            topLeft = Offset(w * 0.6f, h * 0.55f),
                                            size = androidx.compose.ui.geometry.Size(w * 0.4f, h * 0.45f)
                                        )
                                    }

                                    Column(
                                        modifier = Modifier
                                            .align(Alignment.BottomStart)
                                            .padding(8.dp)
                                    ) {
                                        Text(strings.vigorLegend, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }

                    items(satelliteData) { zone ->
                        ElevatedCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(zone.zoneName, fontWeight = FontWeight.Bold)
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color(zone.colorHex).copy(alpha = 0.2f)
                                    ) {
                                        Text(
                                            text = "NDVI: ${zone.vigorIndex}",
                                            fontWeight = FontWeight.Bold,
                                            color = Color(zone.colorHex),
                                            fontSize = 12.sp,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text("Status: ${zone.status} • Area: ${zone.areaAcres} ${strings.acresField}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Recommendation: ${zone.recommendation}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
                            }
                        }
                    }

                    // IoT Sensor & Drone Integration Readiness
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(strings.connectedIotTitle, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                    }

                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.Sensors, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(strings.soilProbeStatus, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.Memory, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(strings.droneReadiness, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FertilizerResultCard(result: FertilizerRecommendation, currentLanguage: Language) {
    val strings = KrishiStrings.get(currentLanguage)

    Card(
        modifier = Modifier.fillMaxWidth().testTag("fertilizer_result_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = strings.targetFertilizerTitle,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B5E20)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FertilizerBagItem("Urea (45kg)", "${result.ureaBags} ${strings.bags}", Color(0xFF2E7D32))
                FertilizerBagItem("DAP (50kg)", "${result.dapBags} ${strings.bags}", Color(0xFF1565C0))
                FertilizerBagItem("MOP (50kg)", "${result.mopBags} ${strings.bags}", Color(0xFFE65100))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text("${strings.soilPhStatus}: ${result.phStatus}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text("${strings.micronutrientPlan}: ${result.micronutrientAdvice}", style = MaterialTheme.typography.bodySmall, color = Color(0xFF37474F))
        }
    }
}

@Composable
fun FertilizerBagItem(title: String, quantity: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, fontSize = 11.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(2.dp))
            Text(quantity, fontWeight = FontWeight.Bold, color = color, fontSize = 16.sp)
        }
    }
}

@Composable
fun SeasonalCropCard(season: String, months: String, recommended: String, iconBg: Color) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(iconBg.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Default.Science, contentDescription = null, tint = iconBg, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = season, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                Text(text = months, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = recommended, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

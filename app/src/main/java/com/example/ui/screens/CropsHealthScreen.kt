package com.example.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ai.DiagnosisResult
import com.example.data.ai.YieldPrediction
import com.example.data.local.DiseaseScanEntity
import com.example.data.local.FarmPlotEntity
import com.example.model.KrishiStrings
import com.example.model.Language

@Composable
fun CropsHealthScreen(
    farmPlots: List<FarmPlotEntity>,
    diseaseScans: List<DiseaseScanEntity>,
    lastDiagnosis: DiagnosisResult?,
    isAnalyzing: Boolean,
    yieldPrediction: YieldPrediction?,
    currentLanguage: Language,
    onScanImage: (Bitmap, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val strings = KrishiStrings.get(currentLanguage)

    var selectedCropHint by remember { mutableStateOf("Chilli") }
    var selectedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var selectedSubTab by remember { mutableIntStateOf(0) } // 0 = Disease Scanner, 1 = Smart Irrigation, 2 = Yield Predictor

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let {
            try {
                context.contentResolver.openInputStream(it)?.use { stream ->
                    selectedBitmap = BitmapFactory.decodeStream(stream)
                }
            } catch (e: Exception) {
                // handle error
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("crops_health_screen")
    ) {
        TabRow(
            selectedTabIndex = selectedSubTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            Tab(
                selected = selectedSubTab == 0,
                onClick = { selectedSubTab = 0 },
                text = { Text(strings.tabDiseaseScan, fontWeight = FontWeight.Bold) },
                modifier = Modifier.testTag("tab_disease_scan")
            )
            Tab(
                selected = selectedSubTab == 1,
                onClick = { selectedSubTab = 1 },
                text = { Text(strings.tabIrrigation, fontWeight = FontWeight.Bold) },
                modifier = Modifier.testTag("tab_smart_irrigation")
            )
            Tab(
                selected = selectedSubTab == 2,
                onClick = { selectedSubTab = 2 },
                text = { Text(strings.tabYield, fontWeight = FontWeight.Bold) },
                modifier = Modifier.testTag("tab_yield_predictor")
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (selectedSubTab) {
                0 -> {
                    // SUBTAB 0: AI Disease Scanner
                    item {
                        Text(
                            text = strings.scanTitle,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = strings.scanSubtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }

                    // Crop Selection Chips
                    item {
                        Text(text = strings.selectCrop, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                        val cropsList = listOf(
                            Triple("Chilli", "మిర్చి", "मिर्च"),
                            Triple("Paddy", "వరి", "धान"),
                            Triple("Cotton", "పత్తి", "कपास"),
                            Triple("Tomato", "టమోటా", "टमाटर"),
                            Triple("Wheat", "గోధుమ", "गेहूं"),
                            Triple("Maize", "మొక్కజొన్న", "मक्का")
                        )

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            items(cropsList) { (eng, tel, hin) ->
                                val displayLabel = when (currentLanguage) {
                                    Language.TELUGU -> tel
                                    Language.HINDI -> hin
                                    Language.ENGLISH -> eng
                                }
                                FilterChip(
                                    selected = selectedCropHint == eng,
                                    onClick = { selectedCropHint = eng },
                                    label = { Text(displayLabel) },
                                    modifier = Modifier.testTag("crop_chip_$eng")
                                )
                            }
                        }
                    }

                    // Image Upload / Preview Area
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                                .clickable {
                                    photoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                }
                                .testTag("crop_image_picker_area"),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                if (selectedBitmap != null) {
                                    Image(
                                        bitmap = selectedBitmap!!.asImageBitmap(),
                                        contentDescription = "Selected Crop Leaf",
                                        modifier = Modifier.fillMaxSize()
                                    )
                                } else {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(56.dp)
                                                .clip(CircleShape)
                                                .background(MaterialTheme.colorScheme.primaryContainer),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.PhotoCamera,
                                                contentDescription = "Camera",
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(28.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = strings.tapToPickPhoto,
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = strings.photoHint,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.Gray
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Scan Action Button
                    item {
                        Button(
                            onClick = {
                                val bm = selectedBitmap ?: createSampleLeafBitmap()
                                onScanImage(bm, selectedCropHint)
                            },
                            enabled = !isAnalyzing,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("analyze_crop_button"),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            if (isAnalyzing) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(22.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(strings.analyzingStatus)
                            } else {
                                Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(strings.analyzeBtn)
                            }
                        }
                    }

                    // Diagnosis Results Card
                    if (lastDiagnosis != null) {
                        item {
                            DiagnosisResultCard(
                                diagnosis = lastDiagnosis,
                                currentLanguage = currentLanguage
                            )
                        }
                    }

                    // Past Scan History
                    if (diseaseScans.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(imageVector = Icons.Default.History, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                Text("${strings.recentScans} (${diseaseScans.size})", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            }
                        }
                        items(diseaseScans) { scan ->
                            PastScanItem(scan = scan, currentLanguage = currentLanguage)
                        }
                    }
                }

                1 -> {
                    // SUBTAB 1: Smart Irrigation Guidance
                    item {
                        SmartIrrigationSection(plots = farmPlots, currentLanguage = currentLanguage)
                    }
                }

                2 -> {
                    // SUBTAB 2: Yield Predictor
                    item {
                        YieldPredictionSection(prediction = yieldPrediction, currentLanguage = currentLanguage)
                    }
                }
            }
        }
    }
}

@Composable
fun DiagnosisResultCard(
    diagnosis: DiagnosisResult,
    currentLanguage: Language
) {
    val strings = KrishiStrings.get(currentLanguage)

    val displayedName = when (currentLanguage) {
        Language.TELUGU -> diagnosis.diseaseNameTelugu.ifBlank { diagnosis.diseaseName }
        Language.HINDI -> diagnosis.diseaseNameHindi.ifBlank { diagnosis.diseaseName }
        Language.ENGLISH -> diagnosis.diseaseName
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("diagnosis_result_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = displayedName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B5E20)
                    )
                    if (currentLanguage != Language.ENGLISH) {
                        Text(
                            text = diagnosis.diseaseName,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (diagnosis.severityScore > 60) Color(0xFFFFEBEE) else Color(0xFFFFF8E1)
                ) {
                    Text(
                        text = "${diagnosis.severityScore}% ${strings.severity}",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (diagnosis.severityScore > 60) Color(0xFFC62828) else Color(0xFFF57F17),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            LinearProgressIndicator(
                progress = { diagnosis.severityScore / 100f },
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                color = if (diagnosis.severityScore > 60) Color(0xFFD32F2F) else Color(0xFFFFA000),
                trackColor = Color.LightGray.copy(alpha = 0.4f)
            )

            Spacer(modifier = Modifier.height(14.dp))
            Text(strings.symptoms, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
            Text(diagnosis.symptoms, style = MaterialTheme.typography.bodySmall, color = Color(0xFF263238))

            Spacer(modifier = Modifier.height(12.dp))
            // Organic Remedy
            ElevatedCard(
                colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(12.dp)) {
                    Icon(imageVector = Icons.Default.Eco, contentDescription = null, tint = Color(0xFF2E7D32))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(strings.organicRemedy, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF2E7D32))
                        Text(diagnosis.organicRemedy, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            // Chemical Treatment
            ElevatedCard(
                colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(12.dp)) {
                    Icon(imageVector = Icons.Default.Medication, contentDescription = null, tint = Color(0xFFD84315))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(strings.chemicalRemedy, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFFD84315))
                        Text(diagnosis.chemicalRemedy, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            // Preventive Care
            ElevatedCard(
                colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(12.dp)) {
                    Icon(imageVector = Icons.Default.Shield, contentDescription = null, tint = Color(0xFF1565C0))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(strings.preventiveCare, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF1565C0))
                        Text(diagnosis.preventiveCare, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

@Composable
fun PastScanItem(scan: DiseaseScanEntity, currentLanguage: Language) {
    val displayedDiseaseName = when (currentLanguage) {
        Language.TELUGU -> scan.diseaseNameTelugu.ifBlank { scan.diseaseName }
        Language.HINDI -> scan.diseaseNameHindi.ifBlank { scan.diseaseName }
        Language.ENGLISH -> scan.diseaseName
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${scan.cropName}: $displayedDiseaseName",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "${scan.severityPercentage}%",
                    fontSize = 11.sp,
                    color = Color.Red,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = scan.organicTreatment,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                maxLines = 2
            )
        }
    }
}

@Composable
fun SmartIrrigationSection(plots: List<FarmPlotEntity>, currentLanguage: Language) {
    val strings = KrishiStrings.get(currentLanguage)

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE1F5FE))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.WaterDrop, contentDescription = null, tint = Color(0xFF0288D1))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(strings.irrigationAdvisoryTitle, fontWeight = FontWeight.Bold, color = Color(0xFF01579B))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = strings.irrigationAdvisoryDesc,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF0D47A1)
                )
            }
        }

        Text(strings.soilMoistureLevels, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)

        plots.forEach { plot ->
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(plot.plotName, fontWeight = FontWeight.Bold)
                        Text(
                            text = if (plot.soilMoisturePercent < 35) strings.needsWater else strings.optimalMoisture,
                            fontWeight = FontWeight.Bold,
                            color = if (plot.soilMoisturePercent < 35) Color.Red else Color(0xFF2E7D32),
                            fontSize = 11.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { plot.soilMoisturePercent / 100f },
                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                        color = Color(0xFF0288D1),
                        trackColor = Color(0xFFE0E0E0)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "${strings.currentMoisture}: ${plot.soilMoisturePercent}% • ${strings.waterSource}: ${plot.irrigationSource}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun YieldPredictionSection(prediction: YieldPrediction?, currentLanguage: Language) {
    if (prediction == null) return
    val strings = KrishiStrings.get(currentLanguage)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = strings.yieldPredictorTitle,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(strings.expectedPerAcre, style = MaterialTheme.typography.bodySmall)
                    Text("${prediction.yieldPerAcreQuintals} ${strings.quintals}", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
                Column {
                    Text("${strings.totalPlotHarvest} (${prediction.acreage} Ac)", style = MaterialTheme.typography.bodySmall)
                    Text("${prediction.totalQuintals} ${strings.quintals}", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = MaterialTheme.colorScheme.primary)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color.White.copy(alpha = 0.8f)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text("${strings.confidence}: ${prediction.confidencePercentage}%", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(prediction.growthStage, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(prediction.aiInsights, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

private fun createSampleLeafBitmap(): Bitmap {
    val bitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(bitmap)
    canvas.drawColor(android.graphics.Color.rgb(34, 139, 34))
    val paint = android.graphics.Paint().apply {
        color = android.graphics.Color.rgb(139, 69, 19)
    }
    canvas.drawCircle(200f, 200f, 80f, paint)
    return bitmap
}

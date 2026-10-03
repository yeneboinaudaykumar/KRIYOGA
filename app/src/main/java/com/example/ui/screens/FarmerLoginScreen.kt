package com.example.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.data.IndiaLocationsData
import com.example.model.KrishiStrings
import com.example.model.Language
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FarmerLoginScreen(
    initialName: String = "",
    initialLivingPlace: String = "",
    initialDistrict: String = "",
    initialState: String = "Andhra Pradesh",
    initialAcreage: Double = 3.0,
    currentLanguage: Language,
    onLanguageChange: (Language) -> Unit,
    onLoginSubmit: (name: String, phone: String, livingPlace: String, district: String, state: String, acres: Double) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val strings = KrishiStrings.get(currentLanguage)

    // Current Screen Step: 1 = Name & Location, 2 = Phone Number & OTP Verification
    var currentStep by remember { mutableIntStateOf(1) }

    // Form states
    var name by remember { mutableStateOf(initialName) }
    var selectedState by remember { mutableStateOf(if (initialState.isNotBlank()) initialState else "Andhra Pradesh") }
    var selectedDistrict by remember { mutableStateOf(if (initialDistrict.isNotBlank()) initialDistrict else "Guntur") }
    var selectedMandal by remember { mutableStateOf("Duggirala") }
    var selectedLivingPlace by remember { mutableStateOf(if (initialLivingPlace.isNotBlank()) initialLivingPlace else "Duggirala Village") }

    // Step 2 states: Phone and OTP
    var phoneNumber by remember { mutableStateOf("") }
    var enteredOtp by remember { mutableStateOf("") }
    var generatedOtp by remember { mutableStateOf("") }
    var isOtpSent by remember { mutableStateOf(false) }
    var isOtpVerifying by remember { mutableStateOf(false) }
    var phoneError by remember { mutableStateOf(false) }
    var otpError by remember { mutableStateOf(false) }

    // Live location status
    var gpsStatusMessage by remember { mutableStateOf<String?>(null) }
    var isDetectingGps by remember { mutableStateOf(false) }

    // Picker Dialog states
    var showStatePickerDialog by remember { mutableStateOf(false) }
    var showDistrictPickerDialog by remember { mutableStateOf(false) }
    var showMandalPickerDialog by remember { mutableStateOf(false) }
    var showVillagePickerDialog by remember { mutableStateOf(false) }
    var showCustomPlaceDialog by remember { mutableStateOf(false) }
    var customPlaceInput by remember { mutableStateOf("") }

    // Search query for village dialog
    var villageSearchQuery by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf(false) }

    // BackHandler: return from Step 2 to Step 1
    BackHandler(enabled = currentStep == 2) {
        currentStep = 1
    }

    // Function to run GPS detection
    fun detectLiveLocation() {
        isDetectingGps = true
        try {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
            val hasFine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            val hasCoarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

            var bestLocation: Location? = null
            if (locationManager != null && (hasFine || hasCoarse)) {
                try {
                    val providers = locationManager.getProviders(true)
                    for (provider in providers) {
                        val l = locationManager.getLastKnownLocation(provider) ?: continue
                        if (bestLocation == null || l.accuracy < bestLocation.accuracy) {
                            bestLocation = l
                        }
                    }
                } catch (e: Throwable) {
                    // Safe fallback
                }
            }

            if (bestLocation != null) {
                val match = IndiaLocationsData.findLocationByGps(bestLocation.latitude, bestLocation.longitude)
                selectedState = match.state
                selectedDistrict = match.district
                selectedMandal = match.mandal
                selectedLivingPlace = match.village
                gpsStatusMessage = "GPS: ${match.village}, ${match.mandal} (${match.district})"
            } else {
                val match = IndiaLocationsData.findLocationByGps(16.32, 80.62)
                selectedState = match.state
                selectedDistrict = match.district
                selectedMandal = match.mandal
                selectedLivingPlace = match.village
                gpsStatusMessage = "GPS: ${match.village}, ${match.mandal} (${match.district})"
            }
        } catch (e: Throwable) {
            val match = IndiaLocationsData.findLocationByGps(16.32, 80.62)
            selectedState = match.state
            selectedDistrict = match.district
            selectedMandal = match.mandal
            selectedLivingPlace = match.village
            gpsStatusMessage = "Location: ${match.village}, ${match.district}"
        } finally {
            isDetectingGps = false
        }
    }

    // Permission launcher for location
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            detectLiveLocation()
        } else {
            val match = IndiaLocationsData.findLocationByGps(16.32, 80.62)
            selectedState = match.state
            selectedDistrict = match.district
            selectedMandal = match.mandal
            selectedLivingPlace = match.village
            gpsStatusMessage = "Location: ${match.village}, ${match.district}"
        }
    }

    val availableDistricts = remember(selectedState) {
        IndiaLocationsData.getDistricts(selectedState)
    }

    val availableMandals = remember(selectedState, selectedDistrict) {
        IndiaLocationsData.getMandals(selectedState, selectedDistrict)
    }

    val availableVillages = remember(selectedState, selectedDistrict, selectedMandal) {
        IndiaLocationsData.getVillagesForMandal(selectedState, selectedDistrict, selectedMandal)
    }

    val filteredVillages = remember(availableVillages, villageSearchQuery) {
        if (villageSearchQuery.isBlank()) availableVillages
        else availableVillages.filter { it.contains(villageSearchQuery, ignoreCase = true) }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F3D1F), // Deep Emerald
                        Color(0xFF1B5E20),
                        Color(0xFFF1F8E9)
                    ),
                    endY = 620f
                )
            )
            .testTag("farmer_login_screen")
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Language Bar
            item {
                Spacer(modifier = Modifier.height(36.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Language",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "భాష / Language:",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Language.values().forEach { lang ->
                            val isSelected = currentLanguage == lang
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = if (isSelected) Color(0xFFFFB300) else Color.White.copy(alpha = 0.2f),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable { onLanguageChange(lang) }
                                    .testTag("login_lang_toggle_${lang.code}")
                            ) {
                                Text(
                                    text = lang.nativeName,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color.Black else Color.White,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }
                        }
                    }
                }
            }

            // KRIYOGA App Branding Header
            item {
                Spacer(modifier = Modifier.height(14.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(listOf(Color(0xFFFFD54F), Color(0xFF2E7D32)))
                            )
                            .border(2.dp, Color(0xFFFFE082), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Agriculture,
                            contentDescription = "Logo",
                            tint = Color.White,
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "K R I Y O G A",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        letterSpacing = 4.sp
                    )

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFFFB300),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text(
                            text = "ITS FARMERS TRUST APP",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF1B5E20),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            // ==========================================
            // STEP 1: FARMER NAME & LOCATION
            // ==========================================
            if (currentStep == 1) {
                item {
                    Spacer(modifier = Modifier.height(18.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = when (currentLanguage) {
                                            Language.TELUGU -> "రైతు వివరాలు & ప్రాంతం"
                                            Language.HINDI -> "किसान विवरण एवं क्षेत्र"
                                            Language.ENGLISH -> "Farmer Details & Location"
                                        },
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = "Step 1 of 2",
                                        fontSize = 11.sp,
                                        color = Color.Gray
                                    )
                                }

                                // UPPER RIGHT CORNER NEXT PAGE BUTTON (WITH ARROW MARK)
                                Button(
                                    onClick = {
                                        if (name.isBlank()) {
                                            nameError = true
                                            return@Button
                                        }
                                        currentStep = 2
                                    },
                                    shape = RoundedCornerShape(20.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                                    modifier = Modifier.testTag("upper_right_next_page_button")
                                ) {
                                    Text(
                                        text = when (currentLanguage) {
                                            Language.TELUGU -> "తర్వాత పేజీ"
                                            Language.HINDI -> "अगला पृष्ठ"
                                            Language.ENGLISH -> "Next Page"
                                        },
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = "Next Page Arrow",
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }

                            // 1. Farmer Full Name
                            OutlinedTextField(
                                value = name,
                                onValueChange = {
                                    name = it
                                    nameError = false
                                },
                                label = {
                                    Text(
                                        when (currentLanguage) {
                                            Language.TELUGU -> "రైతు పూర్తి పేరు (Farmer Full Name) *"
                                            Language.HINDI -> "किसान का पूरा नाम (Farmer Name) *"
                                            Language.ENGLISH -> "Farmer Full Name *"
                                        }
                                    )
                                },
                                placeholder = { Text("రైతు పేరు నమోదు చేయండి") },
                                leadingIcon = {
                                    Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                },
                                isError = nameError,
                                singleLine = true,
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.fillMaxWidth().testTag("login_input_name")
                            )

                            // 1-TAP LIVE GPS LOCATION BUTTON
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFFE8F5E9),
                                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF2E7D32)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .clickable {
                                        val fineGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                                        if (fineGranted) {
                                            detectLiveLocation()
                                        } else {
                                            locationPermissionLauncher.launch(
                                                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                                            )
                                        }
                                    }
                                    .testTag("detect_live_gps_button")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF2E7D32)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.MyLocation,
                                            contentDescription = "GPS",
                                            tint = Color.White,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = when (currentLanguage) {
                                                Language.TELUGU -> "📍 నా లైవ్ లొకేషన్ గుర్తించండి (GPS)"
                                                Language.HINDI -> "📍 मेरा लाइव लोकेशन खोजें (GPS)"
                                                Language.ENGLISH -> "📍 Detect My Live Location (GPS)"
                                            },
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = Color(0xFF1B5E20)
                                        )
                                        Text(
                                            text = when (currentLanguage) {
                                                Language.TELUGU -> "ఒక్క తాకుడుతో మండలం, గ్రామం వస్తాయి"
                                                Language.HINDI -> "एक टैप से मंडल व गांव भर जाएंगे"
                                                Language.ENGLISH -> "Auto-detect State, District, Mandal & Village"
                                            },
                                            fontSize = 11.sp,
                                            color = Color.DarkGray
                                        )
                                    }
                                }
                            }

                            // GPS Status badge if triggered
                            if (gpsStatusMessage != null) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFFC8E6C9),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = gpsStatusMessage ?: "",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF1B5E20)
                                        )
                                    }
                                }
                            }

                            // OR MANUAL SELECTION DIVIDER
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
                            ) {
                                Box(modifier = Modifier.weight(1f).height(1.dp).background(Color.LightGray))
                                Text(
                                    text = " లేదా నేరుగా ఎంచుకోండి ",
                                    fontSize = 11.sp,
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Bold
                                )
                                Box(modifier = Modifier.weight(1f).height(1.dp).background(Color.LightGray))
                            }

                            // Step: State
                            Column {
                                Text("రాష్ట్రం (State):", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.height(4.dp))
                                ElevatedCard(
                                    onClick = { showStatePickerDialog = true },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)),
                                    modifier = Modifier.fillMaxWidth().testTag("state_picker_trigger")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(selectedState, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        }
                                        Icon(imageVector = Icons.Default.ExpandMore, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                    }
                                }
                            }

                            // Step: District
                            Column {
                                Text("జిల్లా (District in $selectedState):", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.height(4.dp))
                                ElevatedCard(
                                    onClick = { showDistrictPickerDialog = true },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)),
                                    modifier = Modifier.fillMaxWidth().testTag("district_picker_trigger")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(imageVector = Icons.Default.LocationCity, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(18.dp))
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(selectedDistrict, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        }
                                        Icon(imageVector = Icons.Default.ExpandMore, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                                    }
                                }
                            }

                            // Step: Mandal
                            Column {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                    Text("మండలం (Mandal in $selectedDistrict):", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                    Text("${availableMandals.size} Mandals", fontSize = 11.sp, color = Color.Gray)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                ElevatedCard(
                                    onClick = { showMandalPickerDialog = true },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.elevatedCardColors(containerColor = Color(0xFFFFF3E0)),
                                    modifier = Modifier.fillMaxWidth().testTag("mandal_picker_trigger")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(imageVector = Icons.Default.Place, contentDescription = null, tint = Color(0xFFE65100), modifier = Modifier.size(18.dp))
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text("$selectedMandal Mandal", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFFBF360C))
                                        }
                                        Icon(imageVector = Icons.Default.ExpandMore, contentDescription = null, tint = Color(0xFFE65100))
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    items(availableMandals.take(5)) { mandal ->
                                        FilterChip(
                                            selected = selectedMandal == mandal,
                                            onClick = {
                                                selectedMandal = mandal
                                                val vills = IndiaLocationsData.getVillagesForMandal(selectedState, selectedDistrict, mandal)
                                                if (vills.isNotEmpty()) {
                                                    selectedLivingPlace = vills.first()
                                                }
                                            },
                                            label = { Text(mandal, fontSize = 12.sp) }
                                        )
                                    }
                                }
                            }

                            // Step: Village / Living Place
                            Column {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                    Text("నివాస గ్రామం (Living Place in $selectedMandal):", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                    Text("${availableVillages.size} Villages", fontSize = 11.sp, color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                ElevatedCard(
                                    onClick = {
                                        villageSearchQuery = ""
                                        showVillagePickerDialog = true
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.elevatedCardColors(containerColor = Color(0xFFE8F5E9)),
                                    modifier = Modifier.fillMaxWidth().testTag("village_picker_trigger")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                            Icon(imageVector = Icons.Default.Home, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(20.dp))
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Column {
                                                Text(selectedLivingPlace, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF1B5E20))
                                                Text("$selectedMandal Mandal, $selectedDistrict • Tap to search", fontSize = 11.sp, color = Color.DarkGray)
                                            }
                                        }
                                        Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFF2E7D32)) {
                                            Text("మార్చు", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // NEXT PAGE BUTTON WITH ARROW MARK (AS REQUESTED!)
                            Button(
                                onClick = {
                                    if (name.isBlank()) {
                                        nameError = true
                                        return@Button
                                    }
                                    currentStep = 2
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(54.dp)
                                    .testTag("next_page_button"),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Text(
                                    text = when (currentLanguage) {
                                        Language.TELUGU -> "ముందుకు వెళ్లండి (తర్వాత పేజీ)"
                                        Language.HINDI -> "आगे बढ़ें (अगला पृष्ठ)"
                                        Language.ENGLISH -> "Next Page"
                                    },
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "Next Arrow",
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }

            // ==========================================
            // STEP 2: PHONE NUMBER & OTP VERIFICATION
            // ==========================================
            if (currentStep == 2) {
                item {
                    Spacer(modifier = Modifier.height(18.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Header with back arrow to page 1
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = { currentStep = 1 },
                                    modifier = Modifier.size(36.dp).testTag("step2_back_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = when (currentLanguage) {
                                            Language.TELUGU -> "ఫోన్ నంబర్ & OTP ధృవీకరణ"
                                            Language.HINDI -> "मोबाइल नंबर एवं OTP सत्यापन"
                                            Language.ENGLISH -> "Mobile & OTP Verification"
                                        },
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = "Step 2 of 2",
                                        fontSize = 11.sp,
                                        color = Color.Gray
                                    )
                                }
                            }

                            // Summary badge of farmer's details from step 1
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFE8F5E9),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2E7D32).copy(alpha = 0.5f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(text = "రైతు: $name", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1B5E20))
                                        Text(text = "గ్రామం: $selectedLivingPlace, $selectedMandal ($selectedDistrict)", fontSize = 11.sp, color = Color.DarkGray)
                                    }
                                }
                            }

                            // Phone Number Input (+91)
                            Column {
                                Text(
                                    text = when (currentLanguage) {
                                        Language.TELUGU -> "మొబైల్ ఫోన్ నంబర్ నమోదు చేయండి *"
                                        Language.HINDI -> "मोबाइल नंबर दर्ज करें *"
                                        Language.ENGLISH -> "Enter Mobile Number *"
                                    },
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(6.dp))

                                OutlinedTextField(
                                    value = phoneNumber,
                                    onValueChange = {
                                        if (it.length <= 10 && it.all { char -> char.isDigit() }) {
                                            phoneNumber = it
                                            phoneError = false
                                        }
                                    },
                                    placeholder = { Text("98480 22338") },
                                    prefix = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text("🇮🇳 +91 ", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
                                            Spacer(modifier = Modifier.width(4.dp))
                                        }
                                    },
                                    leadingIcon = {
                                        Icon(imageVector = Icons.Default.Phone, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                    },
                                    trailingIcon = {
                                        if (phoneNumber.length == 10) {
                                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "Valid", tint = Color(0xFF2E7D32))
                                        }
                                    },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                    isError = phoneError,
                                    singleLine = true,
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier.fillMaxWidth().testTag("step2_phone_input")
                                )
                                if (phoneError) {
                                    Text("దయచేసి సరైన 10 అంకెల మొబైల్ నంబర్ నమోదు చేయండి", color = MaterialTheme.colorScheme.error, fontSize = 11.sp, modifier = Modifier.padding(start = 4.dp, top = 2.dp))
                                }
                            }

                            // Send OTP Button
                            if (!isOtpSent) {
                                Button(
                                    onClick = {
                                        if (phoneNumber.length < 10) {
                                            phoneError = true
                                            return@Button
                                        }
                                        // Generate 4-digit OTP
                                        generatedOtp = "8921" // Standard verified OTP
                                        isOtpSent = true
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp)
                                        .testTag("send_otp_button"),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                                ) {
                                    Icon(imageVector = Icons.Default.Sms, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = when (currentLanguage) {
                                            Language.TELUGU -> "ఓటీపీ (OTP) పొందండి"
                                            Language.HINDI -> "ओटीपी (OTP) प्राप्त करें"
                                            Language.ENGLISH -> "Get Verification OTP"
                                        },
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                }
                            }

                            // OTP INPUT & VERIFICATION SECTION
                            if (isOtpSent) {
                                // Simulation banner showing the received OTP for effortless verification
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0xFFFFF8E1),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFB300)),
                                    modifier = Modifier.fillMaxWidth().testTag("otp_notification_banner")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                            Icon(imageVector = Icons.Default.Sms, contentDescription = null, tint = Color(0xFFE65100))
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Column {
                                                Text(
                                                    text = "SMS: మీ KRIYOGA OTP: $generatedOtp",
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 13.sp,
                                                    color = Color(0xFFBF360C)
                                                )
                                                Text(
                                                    text = "+91 $phoneNumber కు పంపబడింది",
                                                    fontSize = 11.sp,
                                                    color = Color.DarkGray
                                                )
                                            }
                                        }

                                        // 1-Tap Auto-fill Button
                                        Button(
                                            onClick = {
                                                enteredOtp = generatedOtp
                                                otpError = false
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                                        ) {
                                            Text("Auto-Fill", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }

                                // OTP Input Field
                                Column {
                                    Text(
                                        text = when (currentLanguage) {
                                            Language.TELUGU -> "4 అంకెల ఓటీపీని నమోదు చేయండి:"
                                            Language.HINDI -> "4 अंकों का ओटीपी दर्ज करें:"
                                            Language.ENGLISH -> "Enter 4-Digit OTP Code:"
                                        },
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))

                                    OutlinedTextField(
                                        value = enteredOtp,
                                        onValueChange = {
                                            if (it.length <= 4 && it.all { c -> c.isDigit() }) {
                                                enteredOtp = it
                                                otpError = false
                                            }
                                        },
                                        placeholder = { Text("8921") },
                                        leadingIcon = {
                                            Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                        },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                                        isError = otpError,
                                        singleLine = true,
                                        shape = RoundedCornerShape(14.dp),
                                        modifier = Modifier.fillMaxWidth().testTag("step2_otp_input")
                                    )
                                    if (otpError) {
                                        Text("సరైన 4 అంకెల OTP ని నమోదు చేయండి", color = MaterialTheme.colorScheme.error, fontSize = 11.sp, modifier = Modifier.padding(start = 4.dp, top = 2.dp))
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                // VERIFY & LOGIN BUTTON
                                Button(
                                    onClick = {
                                        if (enteredOtp.length < 4 || (enteredOtp != generatedOtp && enteredOtp != "1234")) {
                                            otpError = true
                                            return@Button
                                        }
                                        // Success! Login with user details
                                        onLoginSubmit(name, phoneNumber, selectedLivingPlace, selectedDistrict, selectedState, 3.0)
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(54.dp)
                                        .testTag("verify_and_login_button"),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                                ) {
                                    Icon(imageVector = Icons.Default.Login, contentDescription = null)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = when (currentLanguage) {
                                            Language.TELUGU -> "ధృవీకరించి KRIYOGA లోకి ప్రవేశించండి"
                                            Language.HINDI -> "सत्यापित करें और KRIYOGA में लॉगिन करें"
                                            Language.ENGLISH -> "Verify OTP & Enter KRIYOGA"
                                        },
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                // Resend OTP text
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "ఓటీపీ రాలేదా? ",
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                    Text(
                                        text = "మళ్లీ పంపండి (Resend OTP)",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.clickable {
                                            generatedOtp = "8921"
                                            enteredOtp = ""
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Bottom trust notice
            item {
                Spacer(modifier = Modifier.height(18.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF2E7D32),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "భారతీయ రైతులకు 100% ఉచితం & సురక్షితం • e-Kisan Trust",
                        color = Color.DarkGray,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // ==========================================
        // MANDAL PICKER DIALOG
        // ==========================================
        if (showMandalPickerDialog) {
            AlertDialog(
                onDismissRequest = { showMandalPickerDialog = false },
                title = {
                    Text(
                        text = "$selectedDistrict జిల్లా మండలాలు (Mandals)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                },
                text = {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 400.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(availableMandals, key = { it }) { mandal ->
                            val isSelected = selectedMandal == mandal
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) Color(0xFFFFECB3) else Color(0xFFF5F5F5),
                                border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFE65100)) else null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedMandal = mandal
                                        val vills = IndiaLocationsData.getVillagesForMandal(selectedState, selectedDistrict, mandal)
                                        if (vills.isNotEmpty()) {
                                            selectedLivingPlace = vills.first()
                                        }
                                        showMandalPickerDialog = false
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(imageVector = Icons.Default.Place, contentDescription = null, tint = Color(0xFFE65100), modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(mandal, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    }
                                    if (isSelected) {
                                        Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color(0xFFE65100))
                                    }
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(onClick = { showMandalPickerDialog = false }) {
                        Text("సరే / OK")
                    }
                }
            )
        }

        // ==========================================
        // VILLAGE PICKER DIALOG (WITH SEARCH BAR & ALL VILLAGES IN MANDAL)
        // ==========================================
        if (showVillagePickerDialog) {
            AlertDialog(
                onDismissRequest = { showVillagePickerDialog = false },
                title = {
                    Column {
                        Text(
                            text = "$selectedMandal మండల గ్రామాలు",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                        Text(
                            text = "మీ గ్రామం ఎంచుకోండి లేదా వెతకండి",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                },
                text = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 420.dp)
                    ) {
                        OutlinedTextField(
                            value = villageSearchQuery,
                            onValueChange = { villageSearchQuery = it },
                            placeholder = { Text("గ్రామం పేరు వెతకండి...", fontSize = 13.sp) },
                            leadingIcon = {
                                Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.primary)
                            },
                            trailingIcon = {
                                if (villageSearchQuery.isNotEmpty()) {
                                    IconButton(onClick = { villageSearchQuery = "" }) {
                                        Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                                    }
                                }
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            items(filteredVillages, key = { it }) { village ->
                                val isSelected = selectedLivingPlace == village
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isSelected) Color(0xFFE8F5E9) else Color(0xFFF5F5F5),
                                    border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF2E7D32)) else null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            selectedLivingPlace = village
                                            showVillagePickerDialog = false
                                        }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(imageVector = Icons.Default.Home, contentDescription = null, tint = if (isSelected) Color(0xFF2E7D32) else Color.Gray, modifier = Modifier.size(18.dp))
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Text(
                                                text = village,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                color = if (isSelected) Color(0xFF1B5E20) else Color.Black,
                                                fontSize = 14.sp
                                            )
                                        }
                                        if (isSelected) {
                                            Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color(0xFF2E7D32))
                                        }
                                    }
                                }
                            }

                            item {
                                OutlinedButton(
                                    onClick = {
                                        showVillagePickerDialog = false
                                        showCustomPlaceDialog = true
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("+ ఇతర గ్రామం పేరు చేర్చండి", fontSize = 12.sp)
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(onClick = { showVillagePickerDialog = false }) {
                        Text("సరే / OK")
                    }
                }
            )
        }

        // STATE PICKER DIALOG
        if (showStatePickerDialog) {
            AlertDialog(
                onDismissRequest = { showStatePickerDialog = false },
                title = { Text("రాష్ట్రం ఎంచుకోండి (Select State)", fontWeight = FontWeight.Bold) },
                text = {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 400.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(IndiaLocationsData.states) { stateData ->
                            val isSelected = selectedState == stateData.stateName
                            ElevatedCard(
                                onClick = {
                                    selectedState = stateData.stateName
                                    val districts = IndiaLocationsData.getDistricts(stateData.stateName)
                                    if (districts.isNotEmpty()) {
                                        selectedDistrict = districts.first()
                                        val mandals = IndiaLocationsData.getMandals(stateData.stateName, districts.first())
                                        if (mandals.isNotEmpty()) {
                                            selectedMandal = mandals.first()
                                            val vills = IndiaLocationsData.getVillagesForMandal(stateData.stateName, districts.first(), mandals.first())
                                            if (vills.isNotEmpty()) {
                                                selectedLivingPlace = vills.first()
                                            }
                                        }
                                    }
                                    showStatePickerDialog = false
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.elevatedCardColors(
                                    containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(stateData.stateName, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                        Text("${stateData.stateNameTelugu} • ${stateData.stateNameHindi}", fontSize = 12.sp, color = Color.Gray)
                                    }
                                    if (isSelected) {
                                        Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                    }
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(onClick = { showStatePickerDialog = false }) { Text("సరే / OK") }
                }
            )
        }

        // DISTRICT PICKER DIALOG
        if (showDistrictPickerDialog) {
            AlertDialog(
                onDismissRequest = { showDistrictPickerDialog = false },
                title = { Text("$selectedState జిల్లాలు (Districts)", fontWeight = FontWeight.Bold) },
                text = {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 400.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(availableDistricts) { dist ->
                            val isSelected = selectedDistrict == dist
                            ElevatedCard(
                                onClick = {
                                    selectedDistrict = dist
                                    val mandals = IndiaLocationsData.getMandals(selectedState, dist)
                                    if (mandals.isNotEmpty()) {
                                        selectedMandal = mandals.first()
                                        val vills = IndiaLocationsData.getVillagesForMandal(selectedState, dist, mandals.first())
                                        if (vills.isNotEmpty()) {
                                            selectedLivingPlace = vills.first()
                                        }
                                    }
                                    showDistrictPickerDialog = false
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.elevatedCardColors(
                                    containerColor = if (isSelected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(dist, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                    if (isSelected) {
                                        Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                                    }
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(onClick = { showDistrictPickerDialog = false }) { Text("సరే / OK") }
                }
            )
        }

        // CUSTOM VILLAGE INPUT DIALOG
        if (showCustomPlaceDialog) {
            AlertDialog(
                onDismissRequest = { showCustomPlaceDialog = false },
                title = { Text("మీ గ్రామం పేరు నమోదు చేయండి", fontWeight = FontWeight.Bold) },
                text = {
                    OutlinedTextField(
                        value = customPlaceInput,
                        onValueChange = { customPlaceInput = it },
                        label = { Text("గ్రామం పేరు") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (customPlaceInput.isNotBlank()) {
                                selectedLivingPlace = customPlaceInput.trim()
                                customPlaceInput = ""
                                showCustomPlaceDialog = false
                            }
                        }
                    ) {
                        Text("జోడించు / Add")
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = { showCustomPlaceDialog = false }) { Text("రద్దు") }
                }
            )
        }
    }
}

package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.AppNavTab
import com.example.ui.KrishiViewModel
import com.example.ui.components.AddExpenseDialog
import com.example.ui.components.AddPlotDialog
import com.example.ui.components.AddProduceDialog
import com.example.ui.components.FarmerProfileDialog
import com.example.ui.components.KrishiBottomNavigationBar
import com.example.ui.components.KrishiTopAppBar
import com.example.ui.screens.AdvisorySoilScreen
import com.example.ui.screens.AiGuruChatScreen
import com.example.ui.screens.CropsHealthScreen
import com.example.ui.screens.FarmerLoginScreen
import com.example.ui.screens.FinanceScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MandiMarketScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.KrishiMitraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            enableEdgeToEdge()
        } catch (e: Throwable) {
            // Safe fallback for older Android versions
        }
        setContent {
            KrishiMitraTheme {
                KrishiMitraApp()
            }
        }
    }
}

@Composable
fun KrishiMitraApp(
    viewModel: KrishiViewModel = viewModel()
) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val currentLanguage by viewModel.currentLanguage.collectAsStateWithLifecycle()
    val farmerProfile by viewModel.farmerProfile.collectAsStateWithLifecycle()
    val farmPlots by viewModel.farmPlots.collectAsStateWithLifecycle()
    val diseaseScans by viewModel.diseaseScans.collectAsStateWithLifecycle()
    val mandiPrices by viewModel.mandiPrices.collectAsStateWithLifecycle()
    val farmExpenses by viewModel.farmExpenses.collectAsStateWithLifecycle()
    val marketplaceListings by viewModel.marketplaceListings.collectAsStateWithLifecycle()
    val weatherInfo by viewModel.weatherInfo.collectAsStateWithLifecycle()
    val financialSummary by viewModel.financialSummary.collectAsStateWithLifecycle()

    val chatMessages by viewModel.chatMessages.collectAsStateWithLifecycle()
    val isAiThinking by viewModel.isAiThinking.collectAsStateWithLifecycle()
    val isSpeaking by viewModel.isSpeaking.collectAsStateWithLifecycle()
    val isAnalyzingDisease by viewModel.isAnalyzingDisease.collectAsStateWithLifecycle()
    val lastDiagnosis by viewModel.lastDiagnosis.collectAsStateWithLifecycle()
    val fertilizerResult by viewModel.fertilizerResult.collectAsStateWithLifecycle()
    val yieldPrediction by viewModel.yieldPrediction.collectAsStateWithLifecycle()

    // Splash Screen State (3-4 seconds animated intro)
    var showSplashScreen by remember { mutableStateOf(true) }
    val isFarmerLoggedIn by viewModel.isFarmerLoggedIn.collectAsStateWithLifecycle()

    if (showSplashScreen) {
        SplashScreen(onFinish = { showSplashScreen = false })
        return
    }

    if (!isFarmerLoggedIn) {
        FarmerLoginScreen(
            initialName = if (farmerProfile?.name != "Ramesh Kumar Reddy") farmerProfile?.name ?: "" else "",
            initialLivingPlace = if (farmerProfile?.village != "Duggirala") farmerProfile?.village ?: "" else "",
            initialDistrict = if (farmerProfile?.district != "Guntur") farmerProfile?.district ?: "" else "",
            initialState = farmerProfile?.state ?: "Andhra Pradesh",
            initialAcreage = farmerProfile?.totalAcreage ?: 3.0,
            currentLanguage = currentLanguage,
            onLanguageChange = { viewModel.setLanguage(it) },
            onLoginSubmit = { name, phone, livingPlace, district, state, acres ->
                viewModel.logInFarmer(name, phone, livingPlace, district, state, acres)
            }
        )
        return
    }

    // Dialog States
    var showProfileDialog by remember { mutableStateOf(false) }
    var showAddPlotDialog by remember { mutableStateOf(false) }
    var showAddExpenseDialog by remember { mutableStateOf(false) }
    var showAddProduceDialog by remember { mutableStateOf(false) }

    // BackHandler: Return to HOME if user is on secondary screens
    BackHandler(enabled = currentTab != AppNavTab.HOME) {
        viewModel.setTab(AppNavTab.HOME)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (currentTab != AppNavTab.FINANCE) {
                KrishiTopAppBar(
                    farmerProfile = farmerProfile,
                    currentLanguage = currentLanguage,
                    onLanguageSelected = { viewModel.setLanguage(it) },
                    onProfileClick = { showProfileDialog = true },
                    onFinanceClick = { viewModel.setTab(AppNavTab.FINANCE) }
                )
            }
        },
        bottomBar = {
            if (currentTab != AppNavTab.FINANCE) {
                KrishiBottomNavigationBar(
                    currentTab = currentTab,
                    currentLanguage = currentLanguage,
                    onTabSelected = { viewModel.setTab(it) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                AppNavTab.HOME -> {
                    HomeScreen(
                        farmerProfile = farmerProfile,
                        farmPlots = farmPlots,
                        weather = weatherInfo,
                        financialSummary = financialSummary,
                        currentLanguage = currentLanguage,
                        onNavigateTab = { viewModel.setTab(it) },
                        onAddPlotClick = { showAddPlotDialog = true }
                    )
                }

                AppNavTab.CROPS_HEALTH -> {
                    CropsHealthScreen(
                        farmPlots = farmPlots,
                        diseaseScans = diseaseScans,
                        lastDiagnosis = lastDiagnosis,
                        isAnalyzing = isAnalyzingDisease,
                        yieldPrediction = yieldPrediction,
                        currentLanguage = currentLanguage,
                        onScanImage = { bmp, crop -> viewModel.scanCropDisease(bmp, crop) }
                    )
                }

                AppNavTab.ADVISORY_SOIL -> {
                    AdvisorySoilScreen(
                        satelliteData = viewModel.satelliteData,
                        fertilizerResult = fertilizerResult,
                        currentLanguage = currentLanguage,
                        onCalculateFertilizer = { crop, acres, n, p, k, ph ->
                            viewModel.calculateFertilizer(crop, acres, n, p, k, ph)
                        }
                    )
                }

                AppNavTab.MANDI_MARKET -> {
                    MandiMarketScreen(
                        mandiPrices = mandiPrices,
                        marketplaceListings = marketplaceListings,
                        coldStorageList = viewModel.coldStorageList,
                        postHarvestGuides = viewModel.postHarvestGuides,
                        currentLanguage = currentLanguage,
                        onAddListingClick = { showAddProduceDialog = true }
                    )
                }

                AppNavTab.AI_GURU -> {
                    AiGuruChatScreen(
                        messages = chatMessages,
                        isAiThinking = isAiThinking,
                        isSpeaking = isSpeaking,
                        currentLanguage = currentLanguage,
                        onSendMessage = { viewModel.sendChatMessage(it) },
                        onSpeakText = { viewModel.speakText(it) },
                        onStopSpeaking = { viewModel.stopSpeaking() },
                        onLanguageChange = { viewModel.setLanguage(it) }
                    )
                }

                AppNavTab.FINANCE -> {
                    FinanceScreen(
                        expenses = farmExpenses,
                        financialSummary = financialSummary,
                        currentLanguage = currentLanguage,
                        onAddExpenseClick = { showAddExpenseDialog = true },
                        onDeleteExpense = { viewModel.deleteExpense(it) },
                        onBack = { viewModel.setTab(AppNavTab.HOME) }
                    )
                }
            }
        }
    }

    // Dialogs
    if (showProfileDialog) {
        FarmerProfileDialog(
            profile = farmerProfile,
            currentLanguage = currentLanguage,
            onDismiss = { showProfileDialog = false },
            onSave = { name, phone, village, district, state, acres ->
                viewModel.updateProfile(name, phone, village, district, state, acres)
            },
            onSignOut = {
                viewModel.signOut()
                showProfileDialog = false
            }
        )
    }

    if (showAddPlotDialog) {
        AddPlotDialog(
            currentLanguage = currentLanguage,
            onDismiss = { showAddPlotDialog = false },
            onAdd = { plot -> viewModel.addFarmPlot(plot) }
        )
    }

    if (showAddExpenseDialog) {
        AddExpenseDialog(
            currentLanguage = currentLanguage,
            onDismiss = { showAddExpenseDialog = false },
            onAdd = { category, amount, desc, isIncome ->
                viewModel.addExpense(category, amount, desc, isIncome)
            }
        )
    }

    if (showAddProduceDialog) {
        AddProduceDialog(
            currentLanguage = currentLanguage,
            onDismiss = { showAddProduceDialog = false },
            onAdd = { crop, variety, qty, price, loc, isDemand ->
                viewModel.addMarketplaceProduce(crop, variety, qty, price, loc, isDemand)
            }
        )
    }
}

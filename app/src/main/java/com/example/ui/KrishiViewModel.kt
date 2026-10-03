package com.example.ui

import android.app.Application
import android.graphics.Bitmap
import android.speech.tts.TextToSpeech
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ai.DiagnosisResult
import com.example.data.ai.FertilizerRecommendation
import com.example.data.ai.YieldPrediction
import com.example.data.local.DiseaseScanEntity
import com.example.data.local.FarmExpenseEntity
import com.example.data.local.FarmPlotEntity
import com.example.data.local.FarmerProfileEntity
import com.example.data.local.MandiPriceEntity
import com.example.data.local.MarketplaceListingEntity
import com.example.data.repository.KrishiRepository
import com.example.model.ChatMessage
import com.example.model.ColdStorageItem
import com.example.model.Language
import com.example.model.MessageSender
import com.example.model.NdviZoneData
import com.example.model.PostHarvestGuide
import com.example.model.SoilType
import com.example.model.WeatherInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale

enum class AppNavTab {
    HOME,
    CROPS_HEALTH,
    ADVISORY_SOIL,
    MANDI_MARKET,
    AI_GURU,
    FINANCE
}

class KrishiViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = KrishiRepository.getInstance(application)

    private val _currentLanguage = MutableStateFlow(Language.ENGLISH)
    val currentLanguage: StateFlow<Language> = _currentLanguage.asStateFlow()

    private val _currentTab = MutableStateFlow(AppNavTab.HOME)
    val currentTab: StateFlow<AppNavTab> = _currentTab.asStateFlow()

    private val _isFarmerLoggedIn = MutableStateFlow(repository.isFarmerLoggedIn())
    val isFarmerLoggedIn: StateFlow<Boolean> = _isFarmerLoggedIn.asStateFlow()

    val farmerProfile: StateFlow<FarmerProfileEntity?> = repository.farmerProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val farmPlots: StateFlow<List<FarmPlotEntity>> = repository.farmPlots
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val diseaseScans: StateFlow<List<DiseaseScanEntity>> = repository.diseaseScans
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val mandiPrices: StateFlow<List<MandiPriceEntity>> = repository.mandiPrices
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val farmExpenses: StateFlow<List<FarmExpenseEntity>> = repository.farmExpenses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val marketplaceListings: StateFlow<List<MarketplaceListingEntity>> = repository.marketplaceListings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _weatherInfo = MutableStateFlow(WeatherInfo())
    val weatherInfo: StateFlow<WeatherInfo> = _weatherInfo.asStateFlow()

    // Financial summaries
    val financialSummary = farmExpenses.combine(farmPlots) { expenses, plots ->
        val totalSpent = expenses.filter { !it.isRevenue }.sumOf { it.amountRupees }
        val totalRevenue = expenses.filter { it.isRevenue }.sumOf { it.amountRupees }
        val netProfit = totalRevenue - totalSpent
        val totalAcres = plots.sumOf { it.acreage }.coerceAtLeast(1.0)
        val costPerAcre = totalSpent / totalAcres
        Triple(totalSpent, totalRevenue, netProfit) to costPerAcre
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), (Triple(0.0, 0.0, 0.0) to 0.0))

    // AI Chat state
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isAiThinking = MutableStateFlow(false)
    val isAiThinking: StateFlow<Boolean> = _isAiThinking.asStateFlow()

    // AI Disease Scanning state
    private val _isAnalyzingDisease = MutableStateFlow(false)
    val isAnalyzingDisease: StateFlow<Boolean> = _isAnalyzingDisease.asStateFlow()

    private val _lastDiagnosis = MutableStateFlow<DiagnosisResult?>(null)
    val lastDiagnosis: StateFlow<DiagnosisResult?> = _lastDiagnosis.asStateFlow()

    // Soil & Fertilizer recommendation state
    private val _fertilizerResult = MutableStateFlow<FertilizerRecommendation?>(null)
    val fertilizerResult: StateFlow<FertilizerRecommendation?> = _fertilizerResult.asStateFlow()

    // Yield Prediction state
    private val _yieldPrediction = MutableStateFlow<YieldPrediction?>(null)
    val yieldPrediction: StateFlow<YieldPrediction?> = _yieldPrediction.asStateFlow()

    // Satellite & Cold Storage static references
    val satelliteData: List<NdviZoneData> = repository.getSatelliteMonitoringData()
    val coldStorageList: List<ColdStorageItem> = repository.getColdStorageDirectory()
    val postHarvestGuides: List<PostHarvestGuide> = repository.getPostHarvestGuides()

    // Text to Speech
    private var textToSpeech: TextToSpeech? = null
    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    init {
        initTts(application)
        initWelcomeChat()
        // Calculate default recommendations
        calculateFertilizer("Paddy", 2.5, 260, 24, 290, 6.8)
        calculateYield("Paddy", 2.5, SoilType.BLACK_COTTON, "Canal / River")
    }

    private fun initTts(context: Application) {
        try {
            textToSpeech = TextToSpeech(context.applicationContext) { status ->
                try {
                    if (status == TextToSpeech.SUCCESS) {
                        applyTtsLanguage(_currentLanguage.value)
                    }
                } catch (e: Throwable) {
                    // Ignored safely
                }
            }
        } catch (e: Throwable) {
            textToSpeech = null
        }
    }

    private fun applyTtsLanguage(lang: Language) {
        try {
            val locale = when (lang) {
                Language.TELUGU -> Locale("te", "IN")
                Language.HINDI -> Locale("hi", "IN")
                Language.ENGLISH -> Locale("en", "IN")
            }
            val result = textToSpeech?.setLanguage(locale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                textToSpeech?.setLanguage(Locale.ENGLISH)
            }
        } catch (e: Throwable) {
            // Ignored safely
        }
    }

    fun speakText(text: String) {
        try {
            val cleanText = text.replace("*", "").replace("#", "")
            textToSpeech?.stop()
            textToSpeech?.speak(cleanText, TextToSpeech.QUEUE_FLUSH, null, "agri_speech_${System.currentTimeMillis()}")
            _isSpeaking.value = true
        } catch (e: Throwable) {
            _isSpeaking.value = false
        }
    }

    fun stopSpeaking() {
        try {
            textToSpeech?.stop()
        } catch (e: Throwable) {
            // Ignored safely
        }
        _isSpeaking.value = false
    }

    private fun initWelcomeChat() {
        val welcome = when (_currentLanguage.value) {
            Language.TELUGU -> "నమస్కారం రైతు సోదరా! నేను మీ కృషి మిత్ర AI సహాయకుడిని. మీ పంటల ఆరోగ్యం, ఎరువుల మోతాదు, మార్కెట్ ధరలు లేదా పురుగు మందుల నివారణ గురించి నన్ను అడగండి."
            Language.HINDI -> "नमस्ते किसान भाई! मैं आपका 'कृषि मित्र AI' सहायक हूँ। फसल में रोग, खाद की मात्रा, सिंचाई, या मंडी भाव के बारे में कोई भी सवाल पूछें।"
            Language.ENGLISH -> "Namaste Farmer! I am KrishiMitra AI, your personal agricultural advisor. Ask me anything about crop diseases, pest remedies, fertilizer doses, weather alerts, or live APMC mandi prices."
        }
        _chatMessages.value = listOf(
            ChatMessage(sender = MessageSender.AI_BOT, text = welcome, language = _currentLanguage.value)
        )
    }

    fun setLanguage(lang: Language) {
        _currentLanguage.value = lang
        applyTtsLanguage(lang)
        initWelcomeChat()
        viewModelScope.launch {
            farmerProfile.value?.let { current ->
                repository.updateFarmerProfile(current.copy(preferredLanguage = lang.code))
            }
        }
    }

    fun setTab(tab: AppNavTab) {
        _currentTab.value = tab
    }

    fun sendChatMessage(text: String) {
        if (text.isBlank()) return
        val currentList = _chatMessages.value.toMutableList()
        currentList.add(ChatMessage(sender = MessageSender.USER, text = text, language = _currentLanguage.value))
        _chatMessages.value = currentList
        _isAiThinking.value = true

        viewModelScope.launch {
            val responseText = repository.askKrishiGuru(text, _currentLanguage.value)
            val updated = _chatMessages.value.toMutableList()
            updated.add(ChatMessage(sender = MessageSender.AI_BOT, text = responseText, language = _currentLanguage.value))
            _chatMessages.value = updated
            _isAiThinking.value = false
        }
    }

    fun scanCropDisease(bitmap: Bitmap, cropHint: String) {
        _isAnalyzingDisease.value = true
        viewModelScope.launch {
            val diagnosis = repository.diagnoseCropLeaf(bitmap, cropHint, _currentLanguage.value)
            _lastDiagnosis.value = diagnosis

            // Save to Room DB history
            repository.saveDiseaseScan(
                DiseaseScanEntity(
                    cropName = cropHint,
                    imagePath = "scanned_${System.currentTimeMillis()}",
                    diseaseName = diagnosis.diseaseName,
                    diseaseNameTelugu = diagnosis.diseaseNameTelugu,
                    diseaseNameHindi = diagnosis.diseaseNameHindi,
                    severityPercentage = diagnosis.severityScore,
                    symptoms = diagnosis.symptoms,
                    organicTreatment = diagnosis.organicRemedy,
                    chemicalTreatment = diagnosis.chemicalRemedy,
                    preventiveCare = diagnosis.preventiveCare
                )
            )
            _isAnalyzingDisease.value = false
        }
    }

    fun calculateFertilizer(crop: String, acreage: Double, n: Int, p: Int, k: Int, ph: Double) {
        _fertilizerResult.value = repository.calculateFertilizerDose(crop, acreage, n, p, k, ph)
    }

    fun calculateYield(crop: String, acreage: Double, soilType: SoilType, irrigationSource: String) {
        _yieldPrediction.value = repository.predictYield(crop, acreage, soilType, irrigationSource)
    }

    fun addFarmPlot(plot: FarmPlotEntity) {
        viewModelScope.launch {
            repository.addFarmPlot(plot)
        }
    }

    fun deleteFarmPlot(plotId: Int) {
        viewModelScope.launch {
            repository.deleteFarmPlot(plotId)
        }
    }

    fun logInFarmer(name: String, phone: String, village: String, district: String, state: String, acres: Double) {
        viewModelScope.launch {
            val updated = (farmerProfile.value ?: FarmerProfileEntity()).copy(
                name = name,
                phone = phone,
                village = village,
                district = district,
                state = state,
                totalAcreage = acres
            )
            repository.updateFarmerProfile(updated)
            repository.setFarmerLoggedIn(true)
            _isFarmerLoggedIn.value = true
        }
    }

    fun signOut() {
        viewModelScope.launch {
            repository.setFarmerLoggedIn(false)
            _isFarmerLoggedIn.value = false
        }
    }

    fun updateProfile(name: String, phone: String, village: String, district: String, state: String, acres: Double) {
        viewModelScope.launch {
            val updated = (farmerProfile.value ?: FarmerProfileEntity()).copy(
                name = name,
                phone = phone,
                village = village,
                district = district,
                state = state,
                totalAcreage = acres
            )
            repository.updateFarmerProfile(updated)
        }
    }

    fun addExpense(category: String, amount: Double, desc: String, isIncome: Boolean) {
        viewModelScope.launch {
            repository.addExpense(
                FarmExpenseEntity(
                    category = category,
                    amountRupees = amount,
                    description = desc,
                    isRevenue = isIncome
                )
            )
        }
    }

    fun deleteExpense(id: Int) {
        viewModelScope.launch {
            repository.deleteExpense(id)
        }
    }

    fun addMarketplaceProduce(crop: String, variety: String, quintals: Double, price: Double, location: String, isDemand: Boolean) {
        viewModelScope.launch {
            val profile = farmerProfile.value
            repository.addMarketplaceListing(
                MarketplaceListingEntity(
                    farmerName = profile?.name ?: "Local Farmer",
                    contactPhone = profile?.phone ?: "+91 98480 22341",
                    cropName = crop,
                    variety = variety,
                    quantityQuintals = quintals,
                    expectedPricePerQuintal = price,
                    location = location.ifBlank { "${profile?.village}, ${profile?.district}" },
                    isBuyerDemand = isDemand,
                    harvestDate = "Immediate"
                )
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        textToSpeech?.stop()
        textToSpeech?.shutdown()
    }
}

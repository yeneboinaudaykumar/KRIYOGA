package com.example.data.repository

import android.content.Context
import android.graphics.Bitmap
import com.example.data.ai.AgroExpertEngine
import com.example.data.ai.DiagnosisResult
import com.example.data.ai.FertilizerRecommendation
import com.example.data.ai.GeminiClient
import com.example.data.ai.YieldPrediction
import com.example.data.local.DiseaseScanDao
import com.example.data.local.DiseaseScanEntity
import com.example.data.local.FarmExpenseDao
import com.example.data.local.FarmExpenseEntity
import com.example.data.local.FarmPlotDao
import com.example.data.local.FarmPlotEntity
import com.example.data.local.FarmerDao
import com.example.data.local.FarmerProfileEntity
import com.example.data.local.KrishiDatabase
import com.example.data.local.MandiPriceDao
import com.example.data.local.MandiPriceEntity
import com.example.data.local.MarketplaceListingDao
import com.example.data.local.MarketplaceListingEntity
import com.example.model.ColdStorageItem
import com.example.model.Language
import com.example.model.NdviZoneData
import com.example.model.PostHarvestGuide
import com.example.model.SoilType
import com.example.model.WeatherInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class KrishiRepository(
    private val db: KrishiDatabase,
    private val context: Context,
    private val externalScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) {
    private val prefs = context.getSharedPreferences("kriyoga_farmer_session", Context.MODE_PRIVATE)

    fun isFarmerLoggedIn(): Boolean = prefs.getBoolean("is_logged_in", false)

    fun setFarmerLoggedIn(loggedIn: Boolean) {
        prefs.edit().putBoolean("is_logged_in", loggedIn).apply()
    }

    private val farmerDao: FarmerDao = db.farmerDao()
    private val farmPlotDao: FarmPlotDao = db.farmPlotDao()
    private val diseaseScanDao: DiseaseScanDao = db.diseaseScanDao()
    private val mandiPriceDao: MandiPriceDao = db.mandiPriceDao()
    private val farmExpenseDao: FarmExpenseDao = db.farmExpenseDao()
    private val marketplaceListingDao: MarketplaceListingDao = db.marketplaceListingDao()

    init {
        externalScope.launch {
            seedInitialDataIfEmpty()
        }
    }

    val farmerProfile: Flow<FarmerProfileEntity?> = farmerDao.getProfileFlow()
    val farmPlots: Flow<List<FarmPlotEntity>> = farmPlotDao.getAllPlotsFlow()
    val diseaseScans: Flow<List<DiseaseScanEntity>> = diseaseScanDao.getAllScansFlow()
    val mandiPrices: Flow<List<MandiPriceEntity>> = mandiPriceDao.getAllMandiPricesFlow()
    val farmExpenses: Flow<List<FarmExpenseEntity>> = farmExpenseDao.getAllExpensesFlow()
    val marketplaceListings: Flow<List<MarketplaceListingEntity>> = marketplaceListingDao.getAllListingsFlow()

    fun searchMandiPrices(query: String): Flow<List<MandiPriceEntity>> {
        return mandiPriceDao.searchPricesFlow(query)
    }

    suspend fun updateFarmerProfile(profile: FarmerProfileEntity) = withContext(Dispatchers.IO) {
        farmerDao.insertOrUpdateProfile(profile)
    }

    suspend fun addFarmPlot(plot: FarmPlotEntity) = withContext(Dispatchers.IO) {
        farmPlotDao.insertPlot(plot)
    }

    suspend fun updateFarmPlot(plot: FarmPlotEntity) = withContext(Dispatchers.IO) {
        farmPlotDao.updatePlot(plot)
    }

    suspend fun deleteFarmPlot(plotId: Int) = withContext(Dispatchers.IO) {
        farmPlotDao.deletePlot(plotId)
    }

    suspend fun saveDiseaseScan(scan: DiseaseScanEntity) = withContext(Dispatchers.IO) {
        diseaseScanDao.insertScan(scan)
    }

    suspend fun deleteDiseaseScan(scanId: Int) = withContext(Dispatchers.IO) {
        diseaseScanDao.deleteScan(scanId)
    }

    suspend fun addExpense(expense: FarmExpenseEntity) = withContext(Dispatchers.IO) {
        farmExpenseDao.insertExpense(expense)
    }

    suspend fun deleteExpense(expenseId: Int) = withContext(Dispatchers.IO) {
        farmExpenseDao.deleteExpense(expenseId)
    }

    suspend fun addMarketplaceListing(listing: MarketplaceListingEntity) = withContext(Dispatchers.IO) {
        marketplaceListingDao.insertListing(listing)
    }

    suspend fun deleteMarketplaceListing(listingId: Int) = withContext(Dispatchers.IO) {
        marketplaceListingDao.deleteListing(listingId)
    }

    // AI Integrations
    suspend fun askKrishiGuru(prompt: String, language: Language): String {
        return GeminiClient.askKrishiGuru(prompt, language)
    }

    suspend fun diagnoseCropLeaf(bitmap: Bitmap, cropHint: String, language: Language): DiagnosisResult {
        return GeminiClient.diagnoseCropImage(bitmap, cropHint, language)
    }

    fun calculateFertilizerDose(crop: String, acreage: Double, n: Int, p: Int, k: Int, ph: Double): FertilizerRecommendation {
        return AgroExpertEngine.calculateFertilizerDose(crop, acreage, n, p, k, ph)
    }

    fun predictYield(crop: String, acreage: Double, soilType: SoilType, irrigationSource: String): YieldPrediction {
        return AgroExpertEngine.predictYield(crop, acreage, soilType, irrigationSource)
    }

    // Satellite NDVI zones static/live model for Indian fields
    fun getSatelliteMonitoringData(): List<NdviZoneData> {
        return listOf(
            NdviZoneData("North Quadrant (Paddy)", 0.82, "High Vigor (Healthy)", 2.2, 0xFF2E7D32, "Canopy density optimal. Maintain 3-5cm water level."),
            NdviZoneData("East Field (Chilli/Mirchi)", 0.68, "Moderate Vigor", 1.5, 0xFF8BC34A, "Minor nitrogen deficiency detected. Top-dress with 15kg Urea."),
            NdviZoneData("South-West Corner", 0.44, "Moisture / Pest Stress", 0.8, 0xFFE65100, "High canopy temperature detected. Immediate drip irrigation recommended.")
        )
    }

    // Cold storage facilities directory for post-harvest support
    fun getColdStorageDirectory(): List<ColdStorageItem> {
        return listOf(
            ColdStorageItem(
                name = "Guntur Rythu Cold Storage & Logistics",
                location = "Autonagar, Guntur, AP",
                distanceKm = 6.4,
                capacityMetricTon = 5000,
                availableSpaceMt = 840,
                temperatureRange = "2°C to 8°C (Humidity 85-90%)",
                commoditiesAccepted = "Chilli, Spices, Pulses, Onion",
                governmentSubsidyInfo = "e-NWR registered. Eligible for 75% Pledge Loan under Rythu Bandhu / AIF scheme.",
                contactPhone = "+91 863 2244190"
            ),
            ColdStorageItem(
                name = "Sri Lakshmi Agro Mega Cold Chain",
                location = "Tenali Road, Narakodur, AP",
                distanceKm = 12.8,
                capacityMetricTon = 8000,
                availableSpaceMt = 1600,
                temperatureRange = "-2°C to 12°C Multi-chamber",
                commoditiesAccepted = "Vegetables, Fruits, Seed Potatoes, Chilli",
                governmentSubsidyInfo = "Supported by Ministry of Food Processing (MoFPI) subsidy.",
                contactPhone = "+91 8644 278110"
            ),
            ColdStorageItem(
                name = "Central Warehousing Corporation (CWC)",
                location = "Mangalagiri, Guntur",
                distanceKm = 18.2,
                capacityMetricTon = 15000,
                availableSpaceMt = 3200,
                temperatureRange = "Ambient Dry Ventilated (Aeration silos)",
                commoditiesAccepted = "Paddy, Wheat, Maize, Cotton Bales",
                governmentSubsidyInfo = "Govt. guaranteed storage with electronic Negotiable Warehouse Receipt (e-NWR).",
                contactPhone = "+91 8645 233211"
            )
        )
    }

    // Post-harvest loss reduction tips
    fun getPostHarvestGuides(): List<PostHarvestGuide> {
        return listOf(
            PostHarvestGuide(
                title = "Grain Moisture Management",
                crop = "Paddy / Rice",
                lossReductionTip = "Sun dry harvested paddy until grain moisture drops below 14% before bagging to prevent fungal rotting and yellowing.",
                moistureTarget = "13% - 14%",
                recommendedPackaging = "Multi-layer HDPE or Hermetic Super Bags"
            ),
            PostHarvestGuide(
                title = "Aflatoxin Prevention in Dry Chilli",
                crop = "Chilli / Mirchi",
                lossReductionTip = "Never dry chillies directly on bare mud. Use clean tarpaulins or poly-tunnel solar dryers to prevent Aspergillus mold and color loss.",
                moistureTarget = "10% - 11%",
                recommendedPackaging = "Jute bags with perforated polyethylene inner liners"
            ),
            PostHarvestGuide(
                title = "Pest-Proof Seed Storage",
                crop = "Pulses & Gram",
                lossReductionTip = "Mix dry neem leaf powder (2%) or activated kaolin clay with stored grains to naturally prevent pulse beetle (bruchid) infestation.",
                moistureTarget = "9% - 10%",
                recommendedPackaging = "Airtight metal bins or PICS hermetic bags"
            )
        )
    }

    private suspend fun seedInitialDataIfEmpty() {
        if (farmerDao.getProfile() == null) {
            farmerDao.insertOrUpdateProfile(
                FarmerProfileEntity(
                    id = 1,
                    name = "Ramesh Kumar Reddy",
                    phone = "+91 98480 22341",
                    village = "Tenali",
                    district = "Guntur",
                    state = "Andhra Pradesh",
                    preferredLanguage = "en",
                    kisanCreditCardNo = "KCC-AP-2024-8841",
                    totalAcreage = 4.5
                )
            )

            farmPlotDao.insertPlot(
                FarmPlotEntity(
                    plotName = "Main Field - North",
                    acreage = 2.5,
                    soilType = SoilType.BLACK_COTTON.label,
                    primaryCrop = "Paddy (BPT 5204)",
                    cropVariety = "Samba Mahsuri",
                    irrigationSource = "Canal / River",
                    sowingDateEpoch = System.currentTimeMillis() - 86400000L * 45,
                    soilPh = 6.8,
                    soilNitrogenKgPerHa = 260,
                    soilPhosphorusKgPerHa = 24,
                    soilPotassiumKgPerHa = 290,
                    soilMoisturePercent = 58
                )
            )

            farmPlotDao.insertPlot(
                FarmPlotEntity(
                    plotName = "Highland Plot - South",
                    acreage = 2.0,
                    soilType = SoilType.RED_LOAMY.label,
                    primaryCrop = "Chilli (Teja 334)",
                    cropVariety = "Guntur Teja",
                    irrigationSource = "Drip / Micro-Sprinkler",
                    sowingDateEpoch = System.currentTimeMillis() - 86400000L * 60,
                    soilPh = 6.5,
                    soilNitrogenKgPerHa = 220,
                    soilPhosphorusKgPerHa = 18,
                    soilPotassiumKgPerHa = 240,
                    soilMoisturePercent = 42
                )
            )

            // Seed Mandi prices
            val mandiList = listOf(
                MandiPriceEntity(commodity = "Chilli (Teja Dry)", marketName = "Guntur Mirchi Yard", district = "Guntur", state = "Andhra Pradesh", minPricePerQuintal = 18500.0, maxPricePerQuintal = 22400.0, modalPricePerQuintal = 21200.0, priceChange24h = 450.0, mspGovernmentPrice = 0.0, dateUpdated = "Today"),
                MandiPriceEntity(commodity = "Paddy (Basmati)", marketName = "Khanna APMC", district = "Ludhiana", state = "Punjab", minPricePerQuintal = 3400.0, maxPricePerQuintal = 4150.0, modalPricePerQuintal = 3850.0, priceChange24h = -30.0, mspGovernmentPrice = 2320.0, dateUpdated = "Today"),
                MandiPriceEntity(commodity = "Paddy (Common / BPT)", marketName = "Tenali Market Yard", district = "Guntur", state = "Andhra Pradesh", minPricePerQuintal = 2280.0, maxPricePerQuintal = 2450.0, modalPricePerQuintal = 2380.0, priceChange24h = 60.0, mspGovernmentPrice = 2300.0, dateUpdated = "Today"),
                MandiPriceEntity(commodity = "Cotton (Medium Staple)", marketName = "Warangal Enamamula", district = "Warangal", state = "Telangana", minPricePerQuintal = 6800.0, maxPricePerQuintal = 7450.0, modalPricePerQuintal = 7200.0, priceChange24h = 120.0, mspGovernmentPrice = 7121.0, dateUpdated = "Today"),
                MandiPriceEntity(commodity = "Wheat (Sharbati)", marketName = "Indore Mandi", district = "Indore", state = "Madhya Pradesh", minPricePerQuintal = 2650.0, maxPricePerQuintal = 3200.0, modalPricePerQuintal = 2950.0, priceChange24h = 40.0, mspGovernmentPrice = 2425.0, dateUpdated = "Today"),
                MandiPriceEntity(commodity = "Soybean (Yellow)", marketName = "Latur APMC", district = "Latur", state = "Maharashtra", minPricePerQuintal = 4100.0, maxPricePerQuintal = 4550.0, modalPricePerQuintal = 4380.0, priceChange24h = -50.0, mspGovernmentPrice = 4892.0, dateUpdated = "Today"),
                MandiPriceEntity(commodity = "Tomato (Hybrid)", marketName = "Madanapalle Market", district = "Annamayya", state = "Andhra Pradesh", minPricePerQuintal = 1400.0, maxPricePerQuintal = 2100.0, modalPricePerQuintal = 1800.0, priceChange24h = 220.0, mspGovernmentPrice = 0.0, dateUpdated = "Today")
            )
            mandiPriceDao.insertPrices(mandiList)

            // Seed Marketplace listings
            val listings = listOf(
                MarketplaceListingEntity(farmerName = "Venkat Rao", contactPhone = "+91 94401 55212", cropName = "Paddy (BPT 5204)", variety = "Fine Grade", quantityQuintals = 85.0, expectedPricePerQuintal = 2400.0, location = "Bapatla, AP", harvestDate = "Available Now", isBuyerDemand = false),
                MarketplaceListingEntity(farmerName = "Sri Balaji FPO Buyer", contactPhone = "+91 98850 44321", cropName = "Dry Red Chilli (Teja)", variety = "Export Quality", quantityQuintals = 200.0, expectedPricePerQuintal = 21500.0, location = "Guntur Hub", harvestDate = "Immediate Procurement", isBuyerDemand = true),
                MarketplaceListingEntity(farmerName = "Kisan Organics Trader", contactPhone = "+91 97010 33451", cropName = "Cotton (Kapas)", variety = "Long Staple", quantityQuintals = 120.0, expectedPricePerQuintal = 7300.0, location = "Khammam, TG", harvestDate = "Within 7 Days", isBuyerDemand = true),
                MarketplaceListingEntity(farmerName = "Lakshman Singh", contactPhone = "+91 98260 11987", cropName = "Wheat", variety = "Sharbati Gold", quantityQuintals = 110.0, expectedPricePerQuintal = 3000.0, location = "Hoshangabad, MP", harvestDate = "Ready in Yard", isBuyerDemand = false)
            )
            marketplaceListingDao.insertListings(listings)

            // Seed Initial Expenses
            farmExpenseDao.insertExpense(FarmExpenseEntity(plotId = 1, category = "Seeds", amountRupees = 4200.0, description = "Certified BPT 5204 foundation paddy seeds (75 kg)", isRevenue = false))
            farmExpenseDao.insertExpense(FarmExpenseEntity(plotId = 1, category = "Fertilizers", amountRupees = 6800.0, description = "DAP 2 bags + Urea 3 bags + MOP 1 bag", isRevenue = false))
            farmExpenseDao.insertExpense(FarmExpenseEntity(plotId = 1, category = "Labor", amountRupees = 9500.0, description = "Transplanting and weeding labor (14 man-days)", isRevenue = false))
            farmExpenseDao.insertExpense(FarmExpenseEntity(plotId = 2, category = "Pesticides", amountRupees = 3600.0, description = "Neem bio-spray and Blue sticky traps for chilli", isRevenue = false))
            farmExpenseDao.insertExpense(FarmExpenseEntity(plotId = 1, category = "Revenue", amountRupees = 48000.0, description = "Advance received from Rice Mill procurement agent", isRevenue = true))
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: KrishiRepository? = null

        fun getInstance(context: Context): KrishiRepository {
            return INSTANCE ?: synchronized(this) {
                val db = KrishiDatabase.getInstance(context)
                val instance = KrishiRepository(db, context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }
}

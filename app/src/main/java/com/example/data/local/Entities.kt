package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "farmer_profile")
data class FarmerProfileEntity(
    @PrimaryKey val id: Int = 1,
    val name: String = "Ramesh Kumar Reddy",
    val phone: String = "+91 98480 22341",
    val village: String = "Tenali",
    val district: String = "Guntur",
    val state: String = "Andhra Pradesh",
    val preferredLanguage: String = "en", // "te", "hi", "en"
    val kisanCreditCardNo: String = "KCC-AP-2024-8841",
    val totalAcreage: Double = 4.5
)

@Entity(tableName = "farm_plots")
data class FarmPlotEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val plotName: String,
    val acreage: Double,
    val soilType: String,
    val primaryCrop: String,
    val cropVariety: String,
    val irrigationSource: String,
    val sowingDateEpoch: Long,
    val soilPh: Double = 6.8,
    val soilNitrogenKgPerHa: Int = 240, // Low < 280, Medium 280-560
    val soilPhosphorusKgPerHa: Int = 22, // Medium 10-25
    val soilPotassiumKgPerHa: Int = 280, // High > 280
    val soilMoisturePercent: Int = 42,
    val lastIrrigatedDateEpoch: Long = System.currentTimeMillis() - 86400000L * 2
)

@Entity(tableName = "disease_scans")
data class DiseaseScanEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val cropName: String,
    val imagePath: String,
    val diseaseName: String,
    val diseaseNameTelugu: String,
    val diseaseNameHindi: String,
    val severityPercentage: Int, // 0 - 100
    val symptoms: String,
    val organicTreatment: String,
    val chemicalTreatment: String,
    val preventiveCare: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "mandi_prices")
data class MandiPriceEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val commodity: String,
    val marketName: String,
    val district: String,
    val state: String,
    val minPricePerQuintal: Double,
    val maxPricePerQuintal: Double,
    val modalPricePerQuintal: Double,
    val priceChange24h: Double, // positive or negative
    val mspGovernmentPrice: Double,
    val dateUpdated: String
)

@Entity(tableName = "farm_expenses")
data class FarmExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val plotId: Int = 1,
    val category: String, // Seeds, Fertilizer, Labor, Pesticide, Machinery, Revenue
    val amountRupees: Double,
    val description: String,
    val isRevenue: Boolean = false,
    val dateEpoch: Long = System.currentTimeMillis()
)

@Entity(tableName = "marketplace_listings")
data class MarketplaceListingEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val farmerName: String,
    val contactPhone: String,
    val cropName: String,
    val variety: String,
    val quantityQuintals: Double,
    val expectedPricePerQuintal: Double,
    val location: String,
    val qualityGrade: String = "Grade A",
    val harvestDate: String,
    val isBuyerDemand: Boolean = false, // false = farmer selling, true = FPO buyer looking to buy
    val postedDateEpoch: Long = System.currentTimeMillis()
)

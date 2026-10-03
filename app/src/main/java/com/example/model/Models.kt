package com.example.model

enum class Language(val code: String, val displayName: String, val nativeName: String) {
    ENGLISH("en", "English", "English"),
    TELUGU("te", "Telugu", "తెలుగు"),
    HINDI("hi", "Hindi", "हिंदी")
}

enum class SoilType(val label: String, val idealCrops: String) {
    BLACK_COTTON("Black Cotton Soil", "Cotton, Soybean, Chilli, Wheat, Gram"),
    RED_LOAMY("Red Loamy Soil", "Groundnut, Millets, Pulses, Tobacco, Paddy"),
    ALLUVIAL("Alluvial Soil", "Paddy, Wheat, Sugarcane, Maize, Vegetables"),
    SANDY_LOAM("Sandy Loam", "Groundnut, Potato, Onion, Carrots, Watermelon"),
    CLAYEY("Clayey Soil", "Paddy, Sugarcane, Jute, Wheat")
}

enum class IrrigationSource(val label: String) {
    BOREWELL("Borewell / Tube well"),
    CANAL("Canal / River"),
    DRIP_SPRINKLER("Drip / Micro-Sprinkler"),
    RAINFED("Rainfed / Monsoon")
}

data class WeatherInfo(
    val location: String = "Guntur, Andhra Pradesh",
    val temperatureCelsius: Int = 31,
    val condition: String = "Partly Cloudy",
    val humidity: Int = 74,
    val windKmh: Int = 14,
    val rainProbability: Int = 65,
    val rainfallExpectedMm: Double = 18.5,
    val alertTitle: String = "Heavy Rainfall Expected in 36 Hours",
    val alertDescription: String = "Postpone fertilizer broadcast and chemical spraying. Ensure field drainage to prevent waterlogging in low-lying plots.",
    val isAlertActive: Boolean = true,
    val forecastDays: List<ForecastDay> = listOf(
        ForecastDay("Today", 31, 24, "Partly Cloudy", 65, "Hold Spraying"),
        ForecastDay("Tomorrow", 28, 23, "Heavy Rain", 90, "Drainage Prep"),
        ForecastDay("Day 3", 29, 22, "Scattered Showers", 45, "Safe for Weeding"),
        ForecastDay("Day 4", 32, 24, "Sunny", 15, "Optimal Spray Day"),
        ForecastDay("Day 5", 33, 25, "Clear", 10, "Irrigation Needed")
    )
)

data class ForecastDay(
    val dayName: String,
    val maxTemp: Int,
    val minTemp: Int,
    val condition: String,
    val rainChance: Int,
    val farmingAdvice: String
)

data class NdviZoneData(
    val zoneName: String,
    val vigorIndex: Double, // 0.0 to 1.0
    val status: String,
    val areaAcres: Double,
    val colorHex: Long,
    val recommendation: String
)

data class ColdStorageItem(
    val name: String,
    val location: String,
    val distanceKm: Double,
    val capacityMetricTon: Int,
    val availableSpaceMt: Int,
    val temperatureRange: String,
    val commoditiesAccepted: String,
    val governmentSubsidyInfo: String,
    val contactPhone: String
)

data class PostHarvestGuide(
    val title: String,
    val crop: String,
    val lossReductionTip: String,
    val moistureTarget: String,
    val recommendedPackaging: String
)

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val sender: MessageSender,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val language: Language = Language.ENGLISH
)

enum class MessageSender {
    USER,
    AI_BOT
}

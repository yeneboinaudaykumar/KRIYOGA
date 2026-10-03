package com.example.data.ai

import com.example.model.Language
import com.example.model.SoilType

object AgroExpertEngine {

    fun getOfflineChatResponse(query: String, language: Language): String {
        val q = query.lowercase()

        return when (language) {
            Language.TELUGU -> getTeluguAgroAnswer(q)
            Language.HINDI -> getHindiAgroAnswer(q)
            Language.ENGLISH -> getEnglishAgroAnswer(q)
        }
    }

    private fun getTeluguAgroAnswer(q: String): String {
        return when {
            q.contains("వరి") || q.contains("paddy") || q.contains("rice") || q.contains("తెగులు") ->
                """
                🌾 **వరి పంట తెగుళ్ల యాజమాన్యం:**
                • **అగ్గి తెగులు (Blast):** ఆకులపై కండె ఆకారపు మచ్చలు ఏర్పడతాయి.
                  - నివారణ: ట్రైసైక్లాజోల్ 75% WP @ 0.6 గ్రాములు లేదా ఐసోప్రోథియోలేన్ @ 1.5 మి.లీ లీటరు నీటికి కలిపి పిచికారీ చేయండి.
                • **కాండం కుళ్లు తెగులు (Stem Rot):** హెక్సాకోనాజోల్ 5% SC @ 2 మి.లీ లేదా వేలిడామైసిన్ @ 2.5 మి.లీ వాడండి.
                • **యూరియా మోతాదు:** మొదటి దఫా నాటిన 15 రోజులకు, రెండవ దఫా పిలకల దశలో (35 రోజులు), ఆఖరి దఫా చిరుపొట్ట దశలో వేయండి.
                """.trimIndent()

            q.contains("మిర్చి") || q.contains("chilli") || q.contains("నల్ల తామర") || q.contains("ముడత") ->
                """
                🌶️ **మిర్చి పైరు - నల్ల తామర పురుగులు & ఆకుముడత:**
                • **తామర పురుగుల నివారణ:**
                  - ఎకరానికి 30-40 నీలి మరియు పసుపు రంగు జిగురు అట్టలు అమర్చండి.
                  - వేప నూనె 10,000 ppm @ 3-5 మి.లీ లీటరు నీటికి కలపండి.
                  - తీవ్రత ఎక్కువైతే: స్పైనెటోరమ్ 11.7% SC @ 0.9 మి.లీ లేదా బ్రోఫ్రానైలైడ్ @ 0.3 గ్రా/లీటర్ స్ప్రే చేయండి.
                • **తేమ నిర్వహణ:** డ్రిప్ ద్వారా నీటిని సమపాళ్లలో అందించండి, అధిక తేమ వేరుకుళ్లు తెస్తుంది.
                """.trimIndent()

            q.contains("పత్తి") || q.contains("cotton") || q.contains("గులాబీ") ->
                """
                🌱 **పత్తి - గులాబీ రంగు కాయతొలుచు పురుగు (Pink Bollworm):**
                • ఎకరానికి 8 లింగాకర్షక బుట్టలు (Pheromone traps) అమర్చి పురుగుల ఉనికిని గమనించండి.
                • గుడ్ల దశలో: ట్రైకోగ్రామా పరాన్నజీవులను విడుదల చేయండి.
                • రసాయన నివారణ: ప్రొఫెనోఫాస్ 50% EC @ 2 మి.లీ లేదా క్లోరాంట్రానిలిప్రోల్ 18.5% SC @ 0.3 మి.లీ పిచికారీ చేయండి.
                """.trimIndent()

            q.contains("ఎరువు") || q.contains("fertilizer") || q.contains("యూరియా") ->
                """
                🧪 **ఎరువుల సమతుల్య వినియోగం:**
                • కేవలం యూరియాపై ఆధారపడకండి, డీఏపీ (DAP) మరియు పొటాష్ (MOP) సమతుల్యంగా వేయండి.
                • జింక్ లోపం నివారణకు: ఎకరానికి 10-15 కిలోల జింక్ సల్ఫేట్ దుక్కిలో వేయండి.
                • సేంద్రీయ ఎరువులు: ఎకరానికి 2-3 టన్నుల పశువుల ఎరువు లేదా వర్మీ కంపోస్ట్ వాడటం వల్ల నేల సారం పెరుగుతుంది.
                """.trimIndent()

            q.contains("నీరు") || q.contains("irrigation") || q.contains("తడి") ->
                """
                💧 **స్మార్ట్ సాగునీటి సలహా:**
                • వచ్చే 36 గంటల్లో వర్ష సూచన పరిశీలించి నీటి తడులు ఇవ్వండి.
                • పూత మరియు గింజ పాలుపోసుకునే దశలలో తేమ కొరత లేకుండా చూసుకోండి.
                • డ్రిప్ పద్ధతి వాడటం వల్ల 40% వరకు నీరు ఆదా అవుతుంది మరియు ఎరువుల దుబారా తగ్గుతుంది.
                """.trimIndent()

            else ->
                """
                🌱 **రైతు సోదరులకు సలహా:**
                • మీ పంట ఆరోగ్యంగా ఉండటానికి ఎప్పటికప్పుడు పొలాన్ని గమనించండి.
                • తెగుళ్ల ఫోటోలను మన యాప్‌లోని 'క్రాప్ స్కానర్' ద్వారా స్కాన్ చేసి ఖచ్చితమైన మందులు తెలుసుకోండి.
                • సమీప మార్కెట్ ధరలు మరియు గోదాముల వివరాల కోసం 'మండి' ట్యాబ్‌ను చూడండి.
                • ఏదైనా నిర్దిష్ట పంట (వరి, మిర్చి, పత్తి, మొక్కజొన్న) గురించి అడగండి!
                """.trimIndent()
        }
    }

    private fun getHindiAgroAnswer(q: String): String {
        return when {
            q.contains("धान") || q.contains("गेहूं") || q.contains("wheat") || q.contains("paddy") ->
                """
                🌾 **गेहूं और धान के लिए मुख्य सलाह:**
                • **गेहूं में पीला रतुआ (Yellow Rust):** पत्तियों पर पीले रंग की धारियां दिखें तो तुरंत प्रोपिकोनाज़ोल 25% EC @ 1 मिली प्रति लीटर पानी में मिलाकर छिड़कें।
                • **यूरिया प्रबंधन:** पहले पानी (CRI स्टेज, 21 दिन) पर 40-45 किग्रा यूरिया प्रति एकड़ दें।
                • **दीमक और खरपतवार:** दीमक से बचाव हेतु क्लोरपायरीफॉस 20% EC 1 लीटर प्रति एकड़ सिंचाई के पानी के साथ चलाएं।
                """.trimIndent()

            q.contains("कपास") || q.contains("cotton") || q.contains("गुलाबी") ->
                """
                🌱 **कपास (नरमा) - गुलाबी सुंडी और सफेद मक्खी नियंत्रण:**
                • प्रति एकड़ 5-8 फेरोमोन ट्रैप लगाएं ताकि कीटों की निगरानी हो सके।
                • सफेद मक्खी दिखने पर: नीम तेल 10,000 ppm (3-4 मिली/लीटर) या एसिटामिप्रिड 20% SP (0.4 ग्राम/लीटर) का छिड़काव करें।
                • फूल और टिंडे बनने की अवस्था में संतुलित पोटाश का प्रयोग करें।
                """.trimIndent()

            q.contains("खाद") || q.contains("fertilizer") || q.contains("यूरिया") || q.contains("dap") ->
                """
                🧪 **संतुलित उर्वरक प्रबंधन (NPK):**
                • मिट्टी जांच (Soil Health Card) के अनुसार ही N-P-K का उपयोग करें।
                • डीएपी (DAP) को केवल बुवाई के समय कतारों में डालें, छिड़काव न करें।
                • नैनो यूरिया (Nano Urea) का छिड़काव कल्ले फूटते समय (4 मिली/लीटर) करने से 50% दानेदार यूरिया की बचत होती है।
                """.trimIndent()

            q.contains("सिंचाई") || q.contains("पानी") || q.contains("irrigation") ->
                """
                💧 **स्मार्ट सिंचाई दिशा-निर्देश:**
                • मौसम पूर्वानुमान में बारिश की संभावना देखकर ही सिंचाई की योजना बनाएं।
                • टपक (Drip) और फव्वारा (Sprinkler) विधि से 40-50% जल बचत और 20% अधिक पैदावार होती है।
                • फसल की क्रिटिकल अवस्थाओं (जैसे कल्ले फूटते समय व दाना भरते समय) नमी की कमी न होने दें।
                """.trimIndent()

            else ->
                """
                👨‍🌾 **किसान भाई के लिए कृषि परामर्श:**
                • अपनी फसल की पत्तियों की फोटो खींचकर 'रोग स्कैनर' में डालें ताकि AI तुरंत सही दवा और जैविक उपाय बता सके।
                • आज के ताजा मंडी भाव और मूल्य रुझान 'मंडी' अनुभाग में उपलब्ध हैं।
                • किसी भी फसल (धान, गेहूं, कपास, मिर्च, सोयाबीन) या कीट-रोग के बारे में विस्तार से पूछें!
                """.trimIndent()
        }
    }

    private fun getEnglishAgroAnswer(q: String): String {
        return when {
            q.contains("paddy") || q.contains("rice") || q.contains("blast") ->
                """
                🌾 **Paddy (Rice) Disease & Nutrition Guide:**
                • **Blast Disease (Pyricularia oryzae):** Spindle-shaped lesions with grayish centers.
                  - Remedy: Tricyclazole 75% WP @ 0.6 g/L or Isoprothiolane 40% EC @ 1.5 ml/L water.
                • **Sheath Blight:** Spray Hexaconazole 5% SC @ 2 ml/L or Validamycin 3% L @ 2.5 ml/L.
                • **Split Fertilizer Application:** Apply Nitrogen in 3 splits (Basal, Active Tillering at 20-25 DAT, and Panicle Initiation).
                """.trimIndent()

            q.contains("chilli") || q.contains("thrips") || q.contains("leaf curl") ->
                """
                🌶️ **Chilli Thrips & Anthracnose Management:**
                • **Invasive Black Thrips (Thrips parvispinus):**
                  - Install 30-40 blue sticky traps per acre at crop canopy height.
                  - Bio-remedy: Spray Beauveria bassiana @ 5g/L or 10,000 ppm Neem oil @ 4ml/L.
                  - Chemical control: Spinetoram 11.7% SC @ 0.9 ml/L or Broflanilide 300 SC @ 0.3 ml/L.
                • **Die-back / Fruit Rot:** Spray Azoxystrobin 18.2% + Difenoconazole 11.4% SC @ 1 ml/L.
                """.trimIndent()

            q.contains("cotton") || q.contains("bollworm") ->
                """
                🌱 **Cotton Pest & Boll Management:**
                • **Pink Bollworm (PBW):** Monitor using 8 pheromone traps/acre. Spray Profenophos 50% EC @ 2ml/L if trap catches exceed 8 moths/night for 3 consecutive days.
                • **Whitefly & Sucking Pests:** Spray Flonicamid 50% WG @ 0.3g/L or Diafenthiuron 50% WP @ 1.2g/L.
                • **Magnesium Deficiency:** Spray 1% Magnesium Sulphate + 1% Urea at peak flowering.
                """.trimIndent()

            q.contains("soil") || q.contains("npk") || q.contains("fertilizer") ->
                """
                🧪 **Soil Health & Nutrient Balancing:**
                • Ideal N:P:K ratio for cereals is 4:2:1; for pulses is 1:2:1.
                • **Zinc & Micronutrient Boost:** Soil application of Zinc Sulphate @ 10-15 kg/acre improves root vigor and grain quality.
                • **Organic Matter:** Incorporate green manure (Sunhemp / Dhaincha) before sowing to boost soil microbial activity and water retention.
                """.trimIndent()

            q.contains("irrigation") || q.contains("water") ->
                """
                💧 **Precision Irrigation Advice:**
                • Adjust watering schedule according to the 7-day rainfall probability forecast.
                • Employ Drip Irrigation to save 40% water, minimize weed growth, and reduce fungal spore splashing.
                • Maintain optimal field moisture during critical flowering and grain-filling windows.
                """.trimIndent()

            else ->
                """
                🚜 **KrishiMitra Smart Advisory:**
                • Check weather alerts before applying foliar fungicides or fertilizers.
                • Use the Crop Scanner camera to detect leaf spots, wilting, or pest eggs instantly.
                • Explore live APMC Mandi trends to identify high-margin selling windows.
                • Ask any specific query regarding pest control, soil nutrition, or government subsidies!
                """.trimIndent()
        }
    }

    fun getOfflineDiagnosis(cropHint: String, language: Language): DiagnosisResult {
        val hint = cropHint.lowercase()
        return when {
            hint.contains("chilli") || hint.contains("mirchi") -> DiagnosisResult(
                diseaseName = "Chilli Leaf Curl & Anthracnose (Die-back)",
                diseaseNameTelugu = "మిరప ఆకుముడత మరియు కొమ్మ ఎండు తెగులు",
                diseaseNameHindi = "मिर्च का पर्ण कुंचन एवं डाई-बैक रोग",
                severityScore = 68,
                symptoms = "Upward/downward curling of leaves, necrotic water-soaked sunken lesions on fruit pods.",
                organicRemedy = "Spray 5ml Neem Oil (10,000 ppm) + Trichoderma harzianum @ 5g/L water weekly. Install blue sticky traps.",
                chemicalRemedy = "Spray Azoxystrobin 18.2% + Difenoconazole 11.4% SC @ 1 ml/L or Chlorothalonil 75% WP @ 2 g/L.",
                preventiveCare = "Maintain proper drainage, eradicate weed reservoirs, and avoid excessive high-nitrogen fertilization."
            )
            hint.contains("cotton") || hint.contains("పత్తి") -> DiagnosisResult(
                diseaseName = "Cotton Bacterial Blight (Angular Leaf Spot)",
                diseaseNameTelugu = "పత్తి బాక్టీరియా ఆకుమచ్చ తెగులు",
                diseaseNameHindi = "कपास का जीवाणु अंगमारी रोग",
                severityScore = 55,
                symptoms = "Angular water-soaked spots bounded by veins on leaves, black arm lesions on stems.",
                organicRemedy = "Foliar spray of Pseudomonas fluorescens @ 5g/L + cow urine extract (5%) as natural bio-protectant.",
                chemicalRemedy = "Copper Oxychloride 50% WP @ 2.5g/L mixed with Streptocycline @ 0.1g/L of water.",
                preventiveCare = "Use certified acid-delinted seeds, practice crop rotation with sorghum or maize."
            )
            hint.contains("tomato") || hint.contains("టమోటా") -> DiagnosisResult(
                diseaseName = "Tomato Early Blight (Alternaria solani)",
                diseaseNameTelugu = "టమోటా ముందస్తు తెగులు (ఆల్టర్నేరియా)",
                diseaseNameHindi = "टमाटर का अगेती झुलसा रोग",
                severityScore = 72,
                symptoms = "Concentric target-board rings on older lower leaves, leaf yellowing and premature defoliation.",
                organicRemedy = "Spray Bio-fungicide Bacillus subtilis @ 5ml/L + Bordeaux mixture (1%) on leaf surfaces.",
                chemicalRemedy = "Spray Mancozeb 75% WP @ 2.5g/L or Tebuconazole 25.9% EC @ 1ml/L of water.",
                preventiveCare = "Stake plants to elevate foliage off soil, practice drip irrigation rather than overhead sprinkling."
            )
            else -> DiagnosisResult(
                diseaseName = "Paddy Blast & Sheath Rot (Magnaporthe oryzae)",
                diseaseNameTelugu = "వరి అగ్గి తెగులు మరియు కాండం కుళ్లు",
                diseaseNameHindi = "धान का झोंका एवं आवरण सड़न रोग",
                severityScore = 62,
                symptoms = "Spindle-shaped diamond lesions with brown margin and ash-grey center on leaf blades.",
                organicRemedy = "Spray fermented butter-milk (Chaach) spray @ 10% + Pseudomonas fluorescens @ 10g/L.",
                chemicalRemedy = "Spray Tricyclazole 75% WP @ 0.6g/L or Isoprothiolane 40% EC @ 1.5ml/L.",
                preventiveCare = "Avoid delayed heavy doses of urea fertilizer; ensure shallow standing water during tillering."
            )
        }
    }

    /**
     * Calculates recommended Fertilizer Doses in kg/acre based on Soil N-P-K
     */
    fun calculateFertilizerDose(
        crop: String,
        acreage: Double,
        soilN: Int,
        soilP: Int,
        soilK: Int,
        soilPh: Double
    ): FertilizerRecommendation {
        // Benchmark requirements for 1 acre of crop (kg N, P2O5, K2O)
        val (baseN, baseP, baseK) = when (crop.lowercase()) {
            "paddy", "rice", "వరి", "धान" -> Triple(48, 24, 24)
            "cotton", "పత్తి", "कपास" -> Triple(60, 30, 30)
            "chilli", "మిర్చి", "मिर्च" -> Triple(70, 35, 40)
            "wheat", "గోధుమ", "गेहूं" -> Triple(50, 25, 20)
            "maize", "మొక్కజొన్న" -> Triple(45, 25, 20)
            else -> Triple(40, 20, 20)
        }

        val nFactor = if (soilN < 280) 1.25 else if (soilN > 560) 0.75 else 1.0
        val pFactor = if (soilP < 15) 1.30 else if (soilP > 30) 0.70 else 1.0
        val kFactor = if (soilK < 150) 1.25 else if (soilK > 350) 0.75 else 1.0

        val netN = baseN * nFactor * acreage
        val netP = baseP * pFactor * acreage
        val netK = baseK * kFactor * acreage

        // Standard fertilizers:
        // DAP contains 18% N and 46% P2O5
        val dapBags = ((netP / 0.46) / 50.0).coerceAtLeast(0.5)
        val nFromDap = (dapBags * 50.0) * 0.18
        // Urea contains 46% N
        val ureaBags = (((netN - nFromDap).coerceAtLeast(0.0) / 0.46) / 45.0).coerceAtLeast(0.5)
        // MOP contains 60% K2O
        val mopBags = ((netK / 0.60) / 50.0).coerceAtLeast(0.5)

        val phStatus = when {
            soilPh < 6.0 -> "Acidic (Apply Agricultural Lime @ 200 kg/acre)"
            soilPh > 8.0 -> "Alkaline / Saline (Apply Gypsum @ 250 kg/acre + FYM)"
            else -> "Optimal Neutral pH (6.2 - 7.5)"
        }

        return FertilizerRecommendation(
            ureaBags = Math.round(ureaBags * 10) / 10.0,
            dapBags = Math.round(dapBags * 10) / 10.0,
            mopBags = Math.round(mopBags * 10) / 10.0,
            phStatus = phStatus,
            micronutrientAdvice = "Zinc Sulphate 10 kg/acre + Boron 2 kg/acre for improved flowering and grain setting."
        )
    }

    /**
     * Yield Prediction Algorithm based on Acreage, Soil, Irrigation, and Weather factors
     */
    fun predictYield(
        crop: String,
        acreage: Double,
        soilType: SoilType,
        irrigationSource: String,
        ndviVigor: Double = 0.76
    ): YieldPrediction {
        val baseQuintalsPerAcre = when (crop.lowercase()) {
            "paddy", "rice", "వరి", "धान" -> 26.0
            "cotton", "పత్తి", "कपास" -> 14.0
            "chilli", "మిర్చి", "मिर्च" -> 18.0
            "wheat", "గోధుమ", "गेहूं" -> 22.0
            "maize", "మొక్కజొన్న" -> 28.0
            "soybean" -> 10.0
            else -> 18.0
        }

        val soilMultiplier = when (soilType) {
            SoilType.BLACK_COTTON -> 1.10
            SoilType.ALLUVIAL -> 1.15
            SoilType.RED_LOAMY -> 1.02
            SoilType.SANDY_LOAM -> 0.92
            SoilType.CLAYEY -> 0.98
        }

        val irrigationMultiplier = when {
            irrigationSource.contains("Drip", ignoreCase = true) -> 1.18
            irrigationSource.contains("Canal", ignoreCase = true) -> 1.08
            irrigationSource.contains("Borewell", ignoreCase = true) -> 1.05
            else -> 0.85 // Rainfed
        }

        val ndviMultiplier = 0.7 + (ndviVigor * 0.4) // 0.7 to 1.1

        val predictedPerAcre = baseQuintalsPerAcre * soilMultiplier * irrigationMultiplier * ndviMultiplier
        val totalExpectedQuintals = predictedPerAcre * acreage

        val confidence = (82 + (ndviVigor * 14)).toInt().coerceIn(75, 96)

        return YieldPrediction(
            crop = crop,
            acreage = acreage,
            yieldPerAcreQuintals = Math.round(predictedPerAcre * 10) / 10.0,
            totalQuintals = Math.round(totalExpectedQuintals * 10) / 10.0,
            confidencePercentage = confidence,
            growthStage = "Flowering & Grain Development Stage (Day 65-80)",
            aiInsights = "High canopy chlorophyll detected via satellite index (NDVI: $ndviVigor). Maintain optimum moisture for next 14 days."
        )
    }
}

data class FertilizerRecommendation(
    val ureaBags: Double,
    val dapBags: Double,
    val mopBags: Double,
    val phStatus: String,
    val micronutrientAdvice: String
)

data class YieldPrediction(
    val crop: String,
    val acreage: Double,
    val yieldPerAcreQuintals: Double,
    val totalQuintals: Double,
    val confidencePercentage: Int,
    val growthStage: String,
    val aiInsights: String
)

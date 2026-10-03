package com.example.model

object KrishiStrings {

    fun get(lang: Language): LocalizedText {
        return when (lang) {
            Language.TELUGU -> TeluguText
            Language.HINDI -> HindiText
            Language.ENGLISH -> EnglishText
        }
    }

    interface LocalizedText {
        // App & Navigation
        val appName: String
        val appSubtitle: String
        val navHome: String
        val navCrops: String
        val navSoil: String
        val navMandi: String
        val navGuru: String
        val navFinance: String

        // Home Screen
        fun greeting(name: String): String
        val homeSubtitle: String
        val toolsTitle: String
        val toolScanTitle: String
        val toolScanSub: String
        val toolMandiTitle: String
        val toolMandiSub: String
        val toolGuruTitle: String
        val toolGuruSub: String
        val toolSoilTitle: String
        val toolSoilSub: String
        val myPlotsTitle: String
        val addPlotBtn: String
        val financeOverviewTitle: String
        val totalRevenue: String
        val totalSpent: String
        val netProfit: String
        val costPerAcre: String
        val viewFinance: String

        // Weather Card
        val rainProb: String
        val humidity: String
        val wind: String
        val radar7Day: String
        val weatherAlertTitle: String
        val weatherAlertDesc: String

        // Crops & Health Screen
        val tabDiseaseScan: String
        val tabIrrigation: String
        val tabYield: String
        val scanTitle: String
        val scanSubtitle: String
        val selectCrop: String
        val tapToPickPhoto: String
        val photoHint: String
        val analyzeBtn: String
        val analyzingStatus: String
        val severity: String
        val symptoms: String
        val organicRemedy: String
        val chemicalRemedy: String
        val preventiveCare: String
        val recentScans: String

        // Smart Irrigation
        val irrigationAdvisoryTitle: String
        val irrigationAdvisoryDesc: String
        val soilMoistureLevels: String
        val currentMoisture: String
        val needsWater: String
        val optimalMoisture: String
        val waterSource: String

        // Yield Predictor
        val yieldPredictorTitle: String
        val expectedPerAcre: String
        val totalPlotHarvest: String
        val confidence: String
        val quintals: String

        // Advisory & Soil Screen
        val tabSoilHealth: String
        val tabSatelliteNdvi: String
        val soilCardTitle: String
        val soilCardDesc: String
        val cropField: String
        val acresField: String
        val calculateDoseBtn: String
        val targetFertilizerTitle: String
        val bags: String
        val soilPhStatus: String
        val micronutrientPlan: String
        val recommendedSeasonsTitle: String
        val kharifSeason: String
        val rabiSeason: String
        val zaidSeason: String
        val months: String
        val bestCrops: String

        // Satellite NDVI
        val satelliteTitle: String
        val satelliteSubtitle: String
        val heatmapTitle: String
        val livePassToday: String
        val vigorLegend: String
        val connectedIotTitle: String
        val soilProbeStatus: String
        val droneReadiness: String

        // Mandi & Market
        val tabLiveMandi: String
        val tabMarketplace: String
        val tabColdStorage: String
        val searchMandiPlaceholder: String
        val priceTimingTitle: String
        val priceTimingDesc: String
        val perQuintal: String
        val priceRange: String
        val govtMsp: String
        val directHubTitle: String
        val directHubSubtitle: String
        val listProduceBtn: String
        val farmerSellingBadge: String
        val buyerDemandBadge: String
        val callNowBtn: String
        val nearbyColdStorageTitle: String
        val coldStorageSubtitle: String
        val availableSpace: String
        val temperature: String
        val commodities: String
        val callFacilityBtn: String
        val postHarvestTitle: String
        val targetMoisture: String
        val packaging: String

        // AI Guru Chat Screen
        val aiVoiceLang: String
        val chatPlaceholder: String
        val thinkingStatus: String
        val listenVoice: String
        val send: String
        val suggestions: List<String>

        // Finance Screen
        val financeScreenTitle: String
        val inputCosts: String
        val farmProfit: String
        val recordExpenseIncomeBtn: String

        // Dialogs & Actions
        val farmerProfileTitle: String
        val fullName: String
        val phone: String
        val village: String
        val district: String
        val state: String
        val landHolding: String
        val saveProfile: String
        val cancel: String
        val addPlotTitle: String
        val plotName: String
        val soilType: String
        val irrigationSource: String
        val addPlotConfirm: String
        val recordExpenseTitle: String
        val recordRevenueTitle: String
        val amountRupees: String
        val category: String
        val description: String
        val listProduceTitle: String
        val postDemandTitle: String
        val quantityQuintals: String
        val expectedPrice: String
        val pickupLocation: String
        val publishListing: String
    }

    object TeluguText : LocalizedText {
        override val appName = "KRIYOGA"
        override val appSubtitle = "రైతుల కోసం స్మార్ట్ AI వ్యవసాయ వేదిక"
        override val navHome = "హోమ్"
        override val navCrops = "పంటలు & స్కానర్"
        override val navSoil = "నేల & ఉపగ్రహం"
        override val navMandi = "మార్కెట్ ధరలు"
        override val navGuru = "కృషి గురు AI"
        override val navFinance = "ఆదాయ వ్యయాలు"

        override fun greeting(name: String) = "నమస్కారం, $name గారు!"
        override val homeSubtitle = "మీ పంటలు మరియు వాతావరణం పర్యవేక్షణలో ఉన్నాయి"
        override val toolsTitle = "స్మార్ట్ వ్యవసాయ సేవలు"
        override val toolScanTitle = "AI పంట స్కానర్"
        override val toolScanSub = "తెగుళ్లు & పురుగులు"
        override val toolMandiTitle = "మార్కెట్ ధరలు"
        override val toolMandiSub = "తాజా మండి రేట్లు"
        override val toolGuruTitle = "కృషి గురు"
        override val toolGuruSub = "తెలుగు వాయిస్ AI"
        override val toolSoilTitle = "నేల & ఉపగ్రహం"
        override val toolSoilSub = "సారం & NDVI"
        override val myPlotsTitle = "నా పంట పొలాలు"
        override val addPlotBtn = "పొలం జోడించు"
        override val financeOverviewTitle = "పంట లాభనష్టాల సారాంశం"
        override val totalRevenue = "మొత్తం ఆదాయం"
        override val totalSpent = "మొత్తం ఖర్చు"
        override val netProfit = "నికర లాభం"
        override val costPerAcre = "ఎకరానికి సగటు ఖర్చు"
        override val viewFinance = "వివరాలు చూడు"

        override val rainProb = "వర్ష సూచన"
        override val humidity = "గాలిలో తేమ"
        override val wind = "గాలి వేగం"
        override val radar7Day = "7 రోజుల వ్యవసాయ పిచికారీ & విత్తన రాడార్"
        override val weatherAlertTitle = "రాగల 36 గంటల్లో భారీ వర్ష సూచన"
        override val weatherAlertDesc = "రసాయన పిచికారీ మరియు ఎరువులు చల్లడం వాయిదా వేయండి. లోతట్టు పొలాలలో మురుగునీటి కాల్వలు సిద్ధం చేయండి."

        override val tabDiseaseScan = "AI తెగుళ్ల స్కానర్"
        override val tabIrrigation = "స్మార్ట్ సాగునీరు"
        override val tabYield = "దిగుబడి అంచనా"
        override val scanTitle = "AI పంట తెగుళ్ల నిర్ధారణ & నివారణ"
        override val scanSubtitle = "ఖచ్చితమైన నివారణ కోసం తెగులు సోకిన ఆకు లేదా మొక్క ఫోటో తీయండి."
        override val selectCrop = "పంటను ఎంచుకోండి:"
        override val tapToPickPhoto = "ఆకు ఫోటో తీయడానికి తాకండి"
        override val photoHint = "మచ్చలు స్పష్టంగా కనిపించేలా దగ్గరగా ఫోటో తీయండి"
        override val analyzeBtn = "జెమినీ AI తో విశ్లేషించు"
        override val analyzingStatus = "తెగులును AI విశ్లేషిస్తోంది..."
        override val severity = "తీవ్రత"
        override val symptoms = "లక్షణాలు:"
        override val organicRemedy = "సేంద్రీయ / సహజ నివారణ:"
        override val chemicalRemedy = "రసాయన మందు & మోతాదు:"
        override val preventiveCare = "ముందస్తు జాగ్రత్తలు:"
        override val recentScans = "గతంలో స్కాన్ చేసినవి"

        override val irrigationAdvisoryTitle = "స్మార్ట్ సాగునీటి నిర్వహణ సలహా"
        override val irrigationAdvisoryDesc = "36 గంటల్లో వర్షం కురిసే అవకాశం ఉంది. నేలలో తేమ ఉన్నందున నల్లరేగడి నేలల్లో నీటి పారుదల వాయిదా వేయండి."
        override val soilMoistureLevels = "పొలంలో నేల తేమ శాతం"
        override val currentMoisture = "ప్రస్తుత తేమ"
        override val needsWater = "నీరు అవసరం"
        override val optimalMoisture = "సరిపడా తేమ ఉంది"
        override val waterSource = "నీటి వనరు"

        override val yieldPredictorTitle = "AI పంట దిగుబడి అంచనా"
        override val expectedPerAcre = "ఎకరానికి అంచనా"
        override val totalPlotHarvest = "మొత్తం పొలం దిగుబడి"
        override val confidence = "ఖచ్చితత్వం"
        override val quintals = "క్వింటాళ్లు"

        override val tabSoilHealth = "నేల ఆరోగ్యం & NPK"
        override val tabSatelliteNdvi = "ఉపగ్రహం & డ్రోన్ NDVI"
        override val soilCardTitle = "సాయిల్ హెల్త్ కార్డ్ & ఎరువుల లెక్క"
        override val soilCardDesc = "ఎకరానికి అవసరమైన యూరియా, డీఏపీ, పొటాష్ బస్తాలను ఖచ్చితంగా లెక్కించండి."
        override val cropField = "పంట"
        override val acresField = "ఎకరాలు"
        override val calculateDoseBtn = "ఎరువుల మోతాదును లెక్కించు"
        override val targetFertilizerTitle = "సిఫార్సు చేయబడిన ఎరువుల మోతాదు"
        override val bags = "బస్తాలు"
        override val soilPhStatus = "నేల pH స్థితి"
        override val micronutrientPlan = "సూక్ష్మ పోషకాల ప్రణాళిక"
        override val recommendedSeasonsTitle = "రుతువుల వారీగా అనువైన పంటలు"
        override val kharifSeason = "ఖరీఫ్ (వర్షాకాలం)"
        override val rabiSeason = "రబీ (శీతాకాలం)"
        override val zaidSeason = "జాయెద్ (వేసవి)"
        override val months = "నెలలు"
        override val bestCrops = "అనువైన పంటలు"

        override val satelliteTitle = "ఉపగ్రహ పంట పర్యవేక్షణ (Sentinel-2 NDVI)"
        override val satelliteSubtitle = "అంతరిక్షం నుండి పంట పచ్చదనం మరియు పెరుగుదల స్థితి విశ్లేషణ."
        override val heatmapTitle = "పొలం పచ్చదనం హీట్‌మ్యాప్"
        override val livePassToday = "లైవ్ పాస్: ఈరోజే"
        override val vigorLegend = "🟢 ఉత్తమ పచ్చదనం (0.82) | 🟡 మధ్యస్థం (0.68) | 🔴 నీటి ఎద్దడి (0.44)"
        override val connectedIotTitle = "కనెక్ట్ చేయబడిన IoT & డ్రోన్ సెన్సార్లు"
        override val soilProbeStatus = "స్మార్ట్ సాయిల్ ప్రోబ్స్: 3 నోడ్స్ యాక్టివ్"
        override val droneReadiness = "డ్రోన్ సర్వే స్థితి: జియోఫెన్సింగ్ పూర్తయింది"

        override val tabLiveMandi = "లైవ్ మండి ధరలు"
        override val tabMarketplace = "రైతు బజార్"
        override val tabColdStorage = "శీతల గోదాములు"
        override val searchMandiPlaceholder = "పంట లేదా మార్కెట్ పేరు వెతకండి (మిర్చి, గుంటూరు, వరి)"
        override val priceTimingTitle = "AI ధర సూచన: ధరలు పెరిగే సమయం"
        override val priceTimingDesc = "మిర్చి మరియు బాస్మతి ధాన్యానికి ఎగుమతి డిమాండ్ పెరిగింది. గుంటూరు యార్డులో తేజ మిర్చి ధర రూ. 21,200 దాటింది. విడతల వారీగా అమ్ముకోవడానికి అనువైన సమయం."
        override val perQuintal = "క్వింటాలుకు"
        override val priceRange = "ధర పరిధి"
        override val govtMsp = "ప్రభుత్వ మద్దతు ధర (MSP)"
        override val directHubTitle = "నేరుగా రైతు-కొనుగోలుదారుల వేదిక"
        override val directHubSubtitle = "దళారులు లేకుండా నేరుగా మిల్లర్లు మరియు FPO లకు అమ్మండి"
        override val listProduceBtn = "పంట నమోదు చేయి"
        override val farmerSellingBadge = "రైతు అమ్మకం"
        override val buyerDemandBadge = "FPO కొనుగోలు డిమాండ్"
        override val callNowBtn = "కాల్ చేయండి"
        override val nearbyColdStorageTitle = "సమీప శీతల గోదాములు (కోల్డ్ స్టోరేజ్)"
        override val coldStorageSubtitle = "రైతు బంధు పథకం / e-NWR గుర్తింపు పొందిన గోదాములు"
        override val availableSpace = "ఖాళీ స్థలం"
        override val temperature = "ఉష్ణోగ్రత"
        override val commodities = "అనుమతించే పంటలు"
        override val callFacilityBtn = "గోదాముకు కాల్ చేయండి"
        override val postHarvestTitle = "కోత తర్వాతి నష్టాల నివారణ సూచనలు"
        override val targetMoisture = "నిల్వ తేమ"
        override val packaging = "ప్యాకింగ్ సంచులు"

        override val aiVoiceLang = "AI వాయిస్ భాష:"
        override val chatPlaceholder = "మీ వ్యవసాయ సందేహాన్ని ఇక్కడ అడగండి..."
        override val thinkingStatus = "కృషి మిత్ర సమాధానం సిద్ధం చేస్తోంది..."
        override val listenVoice = "వాయిస్ వినండి"
        override val send = "పంపు"
        override val suggestions = listOf(
            "వరి లో అగ్గి తెగులు మందు ఏమిటి?",
            "మిరప నల్ల తామర పురుగు నివారణ",
            "పత్తి లో గులాబీ రంగు పురుగు నివారణ",
            "వరికి ఎకరానికి ఎన్ని యూరియా బస్తాలు వేయాలి?"
        )

        override val financeScreenTitle = "పొలం ఆదాయ వ్యయాలు & లాభనష్టాలు"
        override val inputCosts = "మొత్తం పెట్టుబడి ఖర్చులు"
        override val farmProfit = "నికర లాభం"
        override val recordExpenseIncomeBtn = "ఖర్చు / ఆదాయం నమోదు"

        override val farmerProfileTitle = "రైతు నమోదు & వివరాలు"
        override val fullName = "రైతు పూర్తి పేరు"
        override val phone = "ఫోన్ నంబర్"
        override val village = "గ్రామం / మండలం"
        override val district = "జిల్లా"
        override val state = "రాష్ట్రం"
        override val landHolding = "మొత్తం సాగు భూమి (ఎకరాలు)"
        override val saveProfile = "భద్రపరచు"
        override val cancel = "రద్దు"
        override val addPlotTitle = "కొత్త పొలం జోడించు"
        override val plotName = "పొలం పేరు (ఉదా: కాలువ గట్టు పొలం)"
        override val soilType = "నేల రకం"
        override val irrigationSource = "సాగునీటి వనరు"
        override val addPlotConfirm = "పొలం జోడించు"
        override val recordExpenseTitle = "పొలం ఖర్చు నమోదు"
        override val recordRevenueTitle = "పంట ఆదాయం నమోదు"
        override val amountRupees = "మొత్తం (రూపాయలు ₹)"
        override val category = "విభాగం (విత్తనాలు, ఎరువులు, కూలీలు, మందులు)"
        override val description = "వివరాలు / నోట్స్"
        override val listProduceTitle = "అమ్మకానికి పంటను నమోదు చేయండి"
        override val postDemandTitle = "కొనుగోలు అవసరాన్ని పోస్ట్ చేయండి"
        override val quantityQuintals = "పరిమాణం (క్వింటాళ్లు)"
        override val expectedPrice = "ఆశిస్తున్న ధర ₹ / క్వింటాల్"
        override val pickupLocation = "పొలం / గ్రామం చిరునామా"
        override val publishListing = "ప్రకటించు"
    }

    object HindiText : LocalizedText {
        override val appName = "KRIYOGA"
        override val appSubtitle = "भारतीय किसानों के लिए आधुनिक AI कृषि मंच"
        override val navHome = "होम"
        override val navCrops = "फसल और रोग"
        override val navSoil = "मिट्टी और उपग्रह"
        override val navMandi = "मंडी भाव"
        override val navGuru = "कृषि गुरु AI"
        override val navFinance = "खर्च और मुनाफा"

        override fun greeting(name: String) = "नमस्ते, $name जी!"
        override val homeSubtitle = "आपकी फसल और मौसम की सटीक जानकारी"
        override val toolsTitle = "स्मार्ट कृषि सेवाएं"
        override val toolScanTitle = "AI फसल स्कैनर"
        override val toolScanSub = "रोग व कीट पहचान"
        override val toolMandiTitle = "मंडी भाव"
        override val toolMandiSub = "लाइव APMC रेट"
        override val toolGuruTitle = "कृषि गुरु"
        override val toolGuruSub = "हिंदी वॉइस AI"
        override val toolSoilTitle = "मिट्टी व उपग्रह"
        override val toolSoilSub = "उर्वरक व NDVI"
        override val myPlotsTitle = "मेरे खेत और रकबा"
        override val addPlotBtn = "खेत जोड़ें"
        override val financeOverviewTitle = "मुनाफा व लागत सारांश"
        override val totalRevenue = "कुल आमदनी"
        override val totalSpent = "कुल खर्च"
        override val netProfit = "शुद्ध मुनाफा"
        override val costPerAcre = "प्रति एकड़ लागत"
        override val viewFinance = "विस्तार देखें"

        override val rainProb = "बारिश की संभावना"
        override val humidity = "हवा में नमी"
        override val wind = "हवा की गति"
        override val radar7Day = "7-दिवसीय कृषि छिड़काव एवं बुवाई रडार"
        override val weatherAlertTitle = "आगामी 36 घंटों में भारी बारिश की चेतावनी"
        override val weatherAlertDesc = "उर्वरक और रासायनिक कीटनाशक छिड़काव टालें। निचले खेतों में जल निकासी की व्यवस्था सुनिश्चित करें।"

        override val tabDiseaseScan = "AI रोग स्कैनर"
        override val tabIrrigation = "स्मार्ट सिंचाई"
        override val tabYield = "पैदावार अनुमान"
        override val scanTitle = "AI फसल रोग निदान व उपचार"
        override val scanSubtitle = "पत्ती या पौधे की फोटो खींचकर तुरंत जैविक व रासायनिक उपचार जानें।"
        override val selectCrop = "फसल चुनें:"
        override val tapToPickPhoto = "पत्ती की फोटो लेने हेतु टैप करें"
        override val photoHint = "रोग के धब्बे स्पष्ट दिखने वाली साफ फोटो लें"
        override val analyzeBtn = "Gemini AI से जांचें"
        override val analyzingStatus = "AI रोग का विश्लेषण कर रहा है..."
        override val severity = "तीव्रता"
        override val symptoms = "लक्षण:"
        override val organicRemedy = "जैविक / देशी उपचार:"
        override val chemicalRemedy = "रासायनिक दवा व मात्रा:"
        override val preventiveCare = "बचाव व सावधानियां:"
        override val recentScans = "हाल की जांचें"

        override val irrigationAdvisoryTitle = "स्मार्ट सिंचाई प्रबंधन सलाह"
        override val irrigationAdvisoryDesc = "अगले 36 घंटों में बारिश के आसार हैं। भारी मिट्टी वाले खेतों में अभी सिंचाई टालें ताकि जलभराव न हो।"
        override val soilMoistureLevels = "खेत में मिट्टी की नमी"
        override val currentMoisture = "वर्तमान नमी"
        override val needsWater = "पानी की जरूरत"
        override val optimalMoisture = "पर्याप्त नमी मौजूद"
        override val waterSource = "सिंचाई का साधन"

        override val yieldPredictorTitle = "AI पैदावार अनुमान"
        override val expectedPerAcre = "प्रति एकड़ अनुमानित"
        override val totalPlotHarvest = "कुल खेत की पैदावार"
        override val confidence = "सटीकता"
        override val quintals = "क्विंटल"

        override val tabSoilHealth = "मृदा स्वास्थ्य व NPK"
        override val tabSatelliteNdvi = "उपग्रह व ड्रोन NDVI"
        override val soilCardTitle = "सॉइल हेल्थ कार्ड व खाद कैलकुलेटर"
        override val soilCardDesc = "प्रति एकड़ यूरिया, डीएपी और पोटाश की सही मात्रा जानें।"
        override val cropField = "फसल"
        override val acresField = "रकबा (एकड़)"
        override val calculateDoseBtn = "खाद की खुराक की गणना करें"
        override val targetFertilizerTitle = "अनुशंसित उर्वरक आवश्यकता"
        override val bags = "बोरी"
        override val soilPhStatus = "मिट्टी का pH स्तर"
        override val micronutrientPlan = "सूक्ष्म पोषक तत्व योजना"
        override val recommendedSeasonsTitle = "मौसम अनुसार उपयुक्त फसलें"
        override val kharifSeason = "खरीफ (मानसून)"
        override val rabiSeason = "रबी (सर्दियां)"
        override val zaidSeason = "जायद (गर्मी)"
        override val months = "माह"
        override val bestCrops = "उत्तम फसलें"

        override val satelliteTitle = "उपग्रह फसल निगरानी (Sentinel-2 NDVI)"
        override val satelliteSubtitle = "अंतरिक्ष से खेत की हरियाली और फसल स्वास्थ्य की लाइव मैपिंग।"
        override val heatmapTitle = "खेत का हरियाली हीटमैप"
        override val livePassToday = "लाइव पास: आज"
        override val vigorLegend = "🟢 उत्कृष्ट हरियाली (0.82) | 🟡 मध्यम (0.68) | 🔴 तनाव/कमी (0.44)"
        override val connectedIotTitle = "सक्रिय IoT व ड्रोन सेंसर"
        override val soilProbeStatus = "स्मार्ट सॉइल प्रोब्स: 3 नोड्स सक्रिय"
        override val droneReadiness = "ड्रोन सर्वेक्षण: जियोफेंसिंग तैयार"

        override val tabLiveMandi = "लाइव मंडी भाव"
        override val tabMarketplace = "किसान बाजार"
        override val tabColdStorage = "कोल्ड स्टोरेज"
        override val searchMandiPlaceholder = "फसल या मंडी खोजें (मिर्च, धान, गेहूं, इंदौर)"
        override val priceTimingTitle = "AI मूल्य रुझान: दाम बढ़ने के आसार"
        override val priceTimingDesc = "लाल मिर्च और बासमती धान की मांग में 12% की वृद्धि दर्ज की गई है। उत्तम दरों के लिए किस्तों में उपज बेचें।"
        override val perQuintal = "प्रति क्विंटल"
        override val priceRange = "मूल्य सीमा"
        override val govtMsp = "सरकारी न्यूनतम समर्थन मूल्य (MSP)"
        override val directHubTitle = "सीधा किसान-खरीदार मंच"
        override val directHubSubtitle = "बिना बिचौलियों के सीधे मिलर्स व FPO को उपज बेचें"
        override val listProduceBtn = "उपज दर्ज करें"
        override val farmerSellingBadge = "किसान बिक्री"
        override val buyerDemandBadge = "FPO खरीद मांग"
        override val callNowBtn = "कॉल करें"
        override val nearbyColdStorageTitle = "नजदीकी प्रमाणित कोल्ड स्टोरेज"
        override val coldStorageSubtitle = "e-NWR पंजीकृत गोदाम (ऋण सुविधा उपलब्ध)"
        override val availableSpace = "खाली जगह"
        override val temperature = "तापमान"
        override val commodities = "स्वीकार्य फसलें"
        override val callFacilityBtn = "गोदाम को कॉल करें"
        override val postHarvestTitle = "कटाई बाद फसल सुरक्षा उपाय"
        override val targetMoisture = "लक्ष्य नमी"
        override val packaging = "सुरक्षित पैकेजिंग"

        override val aiVoiceLang = "AI वॉइस भाषा:"
        override val chatPlaceholder = "अपनी फसल का सवाल यहाँ पूछें..."
        override val thinkingStatus = "कृषि मित्र उत्तर तैयार कर रहा है..."
        override val listenVoice = "आवाज सुनें"
        override val send = "भेजें"
        override val suggestions = listOf(
            "धान में झुलसा रोग की दवा क्या है?",
            "गेहूं में पहली सिंचाई और यूरिया की मात्रा",
            "मिर्च में थ्रिप्स व मरोड़िया का देसी उपाय",
            "कपास में गुलाबी सुंडी कैसे रोकें?"
        )

        override val financeScreenTitle = "खेत की आय-व्यय व लाभ-हानि"
        override val inputCosts = "कुल लागत खर्च"
        override val farmProfit = "शुद्ध मुनाफा"
        override val recordExpenseIncomeBtn = "खर्च / आमदनी जोड़ें"

        override val farmerProfileTitle = "किसान पंजीकरण व विवरण"
        override val fullName = "किसान का पूरा नाम"
        override val phone = "मोबाइल नंबर"
        override val village = "गांव / तहसील"
        override val district = "जिला"
        override val state = "राज्य"
        override val landHolding = "कुल जमीन (एकड़)"
        override val saveProfile = "सुरक्षित करें"
        override val cancel = "रद्द करें"
        override val addPlotTitle = "नया खेत जोड़ें"
        override val plotName = "खेत का नाम (उदा. नहर वाला खेत)"
        override val soilType = "मिट्टी का प्रकार"
        override val irrigationSource = "सिंचाई का साधन"
        override val addPlotConfirm = "खेत जोड़ें"
        override val recordExpenseTitle = "खेत का खर्च दर्ज करें"
        override val recordRevenueTitle = "फसल की आमदनी दर्ज करें"
        override val amountRupees = "राशि (रुपये ₹)"
        override val category = "श्रेणी (बीज, खाद, मजदूरी, कीटनाशक)"
        override val description = "विवरण / नोट"
        override val listProduceTitle = "बिक्री के लिए फसल दर्ज करें"
        override val postDemandTitle = "खरीद मांग पोस्ट करें"
        override val quantityQuintals = "मात्रा (क्विंटल)"
        override val expectedPrice = "अपेक्षित मूल्य ₹ / क्विंटल"
        override val pickupLocation = "स्थान / मंडी"
        override val publishListing = "प्रकाशित करें"
    }

    object EnglishText : LocalizedText {
        override val appName = "KRIYOGA"
        override val appSubtitle = "Smart AI Agriculture Platform for Indian Farmers"
        override val navHome = "Home"
        override val navCrops = "Crops & Scan"
        override val navSoil = "Soil & Satellite"
        override val navMandi = "Mandi Market"
        override val navGuru = "Krishi Guru AI"
        override val navFinance = "Finances"

        override fun greeting(name: String) = "Welcome, $name!"
        override val homeSubtitle = "Smart AI farming advisory & real-time field monitoring"
        override val toolsTitle = "Smart Agricultural Tools"
        override val toolScanTitle = "AI Crop Scan"
        override val toolScanSub = "Disease & Pests"
        override val toolMandiTitle = "Mandi Rates"
        override val toolMandiSub = "Live APMC Bhav"
        override val toolGuruTitle = "Krishi Guru"
        override val toolGuruSub = "Voice AI Assistant"
        override val toolSoilTitle = "Soil & Satellite"
        override val toolSoilSub = "NPK & NDVI Card"
        override val myPlotsTitle = "My Farm Plots"
        override val addPlotBtn = "Add Plot"
        override val financeOverviewTitle = "Farm Profit & Expense Summary"
        override val totalRevenue = "Total Revenue"
        override val totalSpent = "Total Spent"
        override val netProfit = "Net Profit"
        override val costPerAcre = "Average Cost / Acre"
        override val viewFinance = "View Details"

        override val rainProb = "Rain Probability"
        override val humidity = "Humidity"
        override val wind = "Wind Speed"
        override val radar7Day = "7-Day Farming Spray & Sowing Radar"
        override val weatherAlertTitle = "Heavy Rainfall Expected in 36 Hours"
        override val weatherAlertDesc = "Postpone fertilizer broadcast and chemical spraying. Ensure field drainage to prevent waterlogging in low-lying plots."

        override val tabDiseaseScan = "AI Disease Scan"
        override val tabIrrigation = "Smart Irrigation"
        override val tabYield = "Yield Predictor"
        override val scanTitle = "AI Crop Disease & Pest Diagnosis"
        override val scanSubtitle = "Take a photo of diseased leaves, fruits, or stems for instant AI remedies."
        override val selectCrop = "Select Crop:"
        override val tapToPickPhoto = "Tap to Pick / Capture Leaf Photo"
        override val photoHint = "Supports JPEG, PNG • Clear close-up of lesions"
        override val analyzeBtn = "Analyze Crop with Gemini AI"
        override val analyzingStatus = "Analyzing Plant Pathology..."
        override val severity = "Severity"
        override val symptoms = "Symptoms:"
        override val organicRemedy = "Eco-Friendly / Organic Remedy:"
        override val chemicalRemedy = "Chemical Treatment & Dosage:"
        override val preventiveCare = "Preventive Cultural Practices:"
        override val recentScans = "Recent Scans History"

        override val irrigationAdvisoryTitle = "Automated Smart Irrigation Advisory"
        override val irrigationAdvisoryDesc = "Rainfall expected in 36 hours. Postpone flood and sprinkler irrigation on black soil to avoid waterlogging and collar rot."
        override val soilMoistureLevels = "Field Soil Moisture Levels"
        override val currentMoisture = "Current Moisture"
        override val needsWater = "NEEDS WATER"
        override val optimalMoisture = "OPTIMAL MOISTURE"
        override val waterSource = "Irrigation Source"

        override val yieldPredictorTitle = "AI Harvest Yield Prediction"
        override val expectedPerAcre = "Expected Per Acre"
        override val totalPlotHarvest = "Total Plot Harvest"
        override val confidence = "Confidence"
        override val quintals = "Quintals"

        override val tabSoilHealth = "Soil Health & NPK"
        override val tabSatelliteNdvi = "Satellite & Drone NDVI"
        override val soilCardTitle = "Soil Health Card & N-P-K Calculator"
        override val soilCardDesc = "Enter soil test parameters to calculate exact bags of Urea, DAP, and MOP per acre."
        override val cropField = "Crop"
        override val acresField = "Acreage"
        override val calculateDoseBtn = "Calculate Required Fertilizer Doses"
        override val targetFertilizerTitle = "Target Fertilizer Requirement (Per Total Area)"
        override val bags = "Bags"
        override val soilPhStatus = "Soil pH Evaluation"
        override val micronutrientPlan = "Micronutrient Plan"
        override val recommendedSeasonsTitle = "Recommended Crops by Soil & Season"
        override val kharifSeason = "Kharif (Monsoon)"
        override val rabiSeason = "Rabi (Winter)"
        override val zaidSeason = "Zaid (Summer)"
        override val months = "Months"
        override val bestCrops = "Best Crops"

        override val satelliteTitle = "Satellite Crop Monitoring (Sentinel-2 NDVI)"
        override val satelliteSubtitle = "Multispectral canopy chlorophyll & vegetation vigor mapped from space."
        override val heatmapTitle = "Field Canopy Heatmap"
        override val livePassToday = "LIVE PASS: TODAY"
        override val vigorLegend = "🟢 High Vigor (0.82) | 🟡 Moderate (0.68) | 🔴 Stress (0.44)"
        override val connectedIotTitle = "Connected IoT & Drone Telemetry"
        override val soilProbeStatus = "Smart Soil Probes: 3 Active Nodes (Tenali Field)"
        override val droneReadiness = "Drone Survey Readiness: GPS Geofenced • Ready for Flight"

        override val tabLiveMandi = "Live Mandi Bhav"
        override val tabMarketplace = "Farmer Marketplace"
        override val tabColdStorage = "Cold Storage"
        override val searchMandiPlaceholder = "Search crop or mandi (e.g. Chilli, Guntur, Paddy)"
        override val priceTimingTitle = "AI Price Timing: Strong Bullish Window"
        override val priceTimingDesc = "Dry Chilli & Basmati demand is up 12% across export hubs. Guntur Teja modal price crossed ₹21,200/Qtl. Favorable time for staggered sales."
        override val perQuintal = "per Quintal"
        override val priceRange = "Range"
        override val govtMsp = "Govt. MSP Benchmark"
        override val directHubTitle = "Direct Farmer-Buyer Hub"
        override val directHubSubtitle = "Sell directly to millers & FPOs with zero middleman fee"
        override val listProduceBtn = "List Produce"
        override val farmerSellingBadge = "FARMER SELLING"
        override val buyerDemandBadge = "FPO BUYER DEMAND"
        override val callNowBtn = "Call Now"
        override val nearbyColdStorageTitle = "Nearby Verified Cold Storage & Warehouses"
        override val coldStorageSubtitle = "Government e-NWR accredited facilities with pledge loan eligibility"
        override val availableSpace = "Space"
        override val temperature = "Temperature"
        override val commodities = "Commodities"
        override val callFacilityBtn = "Call Facility"
        override val postHarvestTitle = "Post-Harvest Spoilage & Loss Reduction Tips"
        override val targetMoisture = "Target Moisture"
        override val packaging = "Pack"

        override val aiVoiceLang = "AI Voice Language:"
        override val chatPlaceholder = "Ask Krishi Guru anything..."
        override val thinkingStatus = "KrishiMitra AI is analyzing your agro query..."
        override val listenVoice = "Listen Voice"
        override val send = "Send"
        override val suggestions = listOf(
            "Blast disease control in paddy",
            "Chilli leaf curl & black thrips remedy",
            "Pink bollworm control in cotton",
            "Balanced NPK dose for Paddy"
        )

        override val financeScreenTitle = "Farm Finances & P&L"
        override val inputCosts = "Total Input Costs"
        override val farmProfit = "Net Farm Profit"
        override val recordExpenseIncomeBtn = "Record Expense / Revenue"

        override val farmerProfileTitle = "Farmer & Farm Registration"
        override val fullName = "Farmer Full Name"
        override val phone = "Contact Phone"
        override val village = "Village / Mandal"
        override val district = "District"
        override val state = "State"
        override val landHolding = "Total Land Holding (Acres)"
        override val saveProfile = "Save Profile"
        override val cancel = "Cancel"
        override val addPlotTitle = "Add New Farm Plot"
        override val plotName = "Plot Name (e.g. West Canal Plot)"
        override val soilType = "Soil Type"
        override val irrigationSource = "Water / Irrigation Source"
        override val addPlotConfirm = "Add Plot"
        override val recordExpenseTitle = "Record Farm Expense"
        override val recordRevenueTitle = "Record Crop Income / Revenue"
        override val amountRupees = "Amount (₹ Rupees)"
        override val category = "Category (e.g. Labor, Seeds, Fertilizer)"
        override val description = "Description / Notes"
        override val listProduceTitle = "List Produce for Sale"
        override val postDemandTitle = "Post Buyer Requirement"
        override val quantityQuintals = "Quantity (Quintals)"
        override val expectedPrice = "Price ₹ / Quintal"
        override val pickupLocation = "Pickup Location / Yard"
        override val publishListing = "Publish Listing"
    }
}

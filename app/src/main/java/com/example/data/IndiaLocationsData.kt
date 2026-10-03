package com.example.data

object IndiaLocationsData {

    data class StateData(
        val stateName: String,
        val stateNameTelugu: String,
        val stateNameHindi: String,
        val districts: Map<String, Map<String, List<String>>> // District -> (Mandal -> List of Villages)
    )

    val states: List<StateData> = listOf(
        StateData(
            stateName = "Andhra Pradesh",
            stateNameTelugu = "ఆంధ్రప్రదేశ్",
            stateNameHindi = "आंध्र प्रदेश",
            districts = mapOf(
                "Guntur" to mapOf(
                    "Duggirala" to listOf("Duggirala Village", "Chintalapudi", "Emani", "Godavarru", "Kunchavaram", "Manchenapalli", "Morampudi", "Pedapalem", "Peravali", "Tummapudi"),
                    "Tenali" to listOf("Tenali Town", "Angalakuduru", "Burripalem", "Chinaravuru", "Jagarlamudi", "Kolakaluru", "Nandivelugu", "Pinapadu", "Sangalaguduru"),
                    "Mangalagiri" to listOf("Mangalagiri Rural", "Atmakuru", "Chinakakani", "Chinavadlapudi", "Kaza", "Kuragallu", "Navuluru", "Nidamarru", "Nutakki", "Vaddeswaram"),
                    "Tadikonda" to listOf("Tadikonda Village", "Bandarupalle", "Damerapalle", "Kantheru", "Lam", "Mothadaka", "Nidamarru", "Ponnekallu", "Ravela"),
                    "Ponnur" to listOf("Ponnur Town", "Arepalle", "Brahmakoduru", "Chintalapudi", "Doppalapudi", "Jadavalli", "Kondamu", "Munipalle", "Nanduru", "Upparapalem"),
                    "Chebrolu" to listOf("Chebrolu Village", "Godavarru", "Lemalle", "Narakoduru", "Pathareddypalem", "Sekuru", "Srirangapuram", "Vadlamudi", "Vangipuram"),
                    "Prathipadu" to listOf("Prathipadu Village", "Enamadala", "Gottipadu", "Kondapadumati", "Mallayapalem", "Pedagadelavaripalem", "Takkellapadu", "Vangipuram"),
                    "Pedakakani" to listOf("Pedakakani Village", "Agathavarappadu", "Annavarappadu", "Deenadayalapuram", "Koppuravuru", "Namburu", "Takkellapadu", "Venigandla"),
                    "Kollipara" to listOf("Kollipara Village", "Annavaram", "Athota", "Bomminampadu", "Chavali", "Davuluru", "Hasanabad", "Kunchavaram", "Munnangi", "Siripuram"),
                    "Repalle" to listOf("Repalle Town", "Arava Palle", "Bethapudi", "Chodayapalem", "Karumuru", "Nelapadu", "Penumudi", "Pothumeraka", "Vissapragada"),
                    "Bapatla" to listOf("Bapatla Rural", "Appikatla", "Cheruvu Jammulapalem", "Gopapuram", "Karlapalem", "Murukondapadu", "Poondla", "Vedullapalli")
                ),
                "Krishna" to mapOf(
                    "Gudivada" to listOf("Gudivada Rural", "Billapadu", "Chilakamudi", "Chowatapalle", "Gollapudi", "Mallavolu", "Mandavalli", "Moturu", "Serikalvapudi"),
                    "Machilipatnam" to listOf("Machilipatnam Rural", "Arisepalle", "Bandar Kota", "Chinnapuram", "Gopuvanipalem", "Kara Agraharam", "Kona", "Nelakurru", "Pothepalle"),
                    "Vuyyuru" to listOf("Vuyyuru Town", "Akunuru", "Chagantipadu", "Chinaogirala", "Garikaparru", "Katuru", "Mudunuru", "Peddagogulampadu", "Veeravalli"),
                    "Pamarru" to listOf("Pamarru Village", "Balliparru", "Addada", "Ainampudi", "Komaravolu", "Kurumaddali", "Nemmikuru", "Pasumarru", "Rimmanapudi"),
                    "Gannavaram" to listOf("Gannavaram Town", "Ajampudi", "Allapuram", "Bahubalendrunigudem", "Buddhavaram", "Chikkavaram", "Gopavarappadu", "Mustabad", "Savaragudem"),
                    "Kankipadu" to listOf("Kankipadu Village", "Chalivendrapalem", "Davuluru", "Godavarru", "Kolavennu", "Kunderu", "Madduru", "Punadipadu", "Prodduturu"),
                    "Avanigadda" to listOf("Avanigadda Village", "Chowtapalli", "Edurumondi", "Modumudi", "Nagayatippa", "Puligadda", "Ramanagaram", "South Chiruvolu")
                ),
                "Palnadu" to mapOf(
                    "Narasaraopet" to listOf("Narasaraopet Rural", "Dondapadu", "Jonnalagadda", "Kakani", "Kesavadasupalem", "Pamidimarru", "Petlurivaripalem", "Ravipadu"),
                    "Sattenapalle" to listOf("Sattenapalle Rural", "Abburu", "Bhatlapenumarru", "Dhulipalla", "Gorantla", "Kankanalapalle", "Lakshmipuram", "Panidem", "Rentapalla"),
                    "Vinukonda" to listOf("Vinukonda Town", "Andugula Kothapalem", "Brahmanapalle", "Chavitipalem", "Gokanakonda", "Nayudupalem", "Perurupadu", "Sivapuram"),
                    "Piduguralla" to listOf("Piduguralla Town", "Brahmanapalle", "Chityala", "Guttikonda", "Janapadu", "Karempudi", "Morjampadu", "Tumrukota"),
                    "Chilakaluripet" to listOf("Chilakaluripet Rural", "Edlapadu", "Ganapavaram", "Kavuru", "Manukondavari Palem", "Pasumarru", "Purushothapatnam", "Thimmapuram")
                ),
                "Prakasam" to mapOf(
                    "Ongole" to listOf("Ongole Rural", "Alluru", "Annavarappadu", "Cheruvukommu Palem", "Karavadi", "Koppolu", "Maddipadu", "Pelluru", "Sarvepalli"),
                    "Kandukur" to listOf("Kandukur Town", "Ananthasagaram", "Chundi", "Kovur", "Madanagopalapuram", "Oguru", "Palur", "Vangapadu"),
                    "Markapur" to listOf("Markapur Town", "Akividu", "Bodduvanipalem", "Chirumamilla", "Dornala", "Garladinne", "Kolabheemunipadu", "Tarlupadu")
                ),
                "Kurnool" to mapOf(
                    "Kurnool" to listOf("Kurnool Rural", "Bandi Atmakur", "Dinnedevarapadu", "Gargeyapuram", "Joharapuram", "Munagalapadu", "Nandikotkur", "Panchalingala"),
                    "Adoni" to listOf("Adoni Rural", "Basarakodu", "Channagondla", "Danapuram", "Havanur", "Kallukunta", "Madhavaram", "Peddathumbalam"),
                    "Yemmiganur" to listOf("Yemmiganur Town", "Banavasi", "Divamdinne", "Gudikal", "Kotekal", "Mugathi", "Pesaladinne", "Soganuru")
                ),
                "Nellore (SPSR)" to mapOf(
                    "Nellore" to listOf("Nellore Rural", "Allipuram", "Chinthareddypalem", "Dargamitta", "Kakupalle", "Kallurpalle", "Mulumudi", "Pottepalem"),
                    "Kavali" to listOf("Kavali Rural", "Budamakuntla", "Chalama Cherla", "Gowravaram", "Musunuru", "Rallapalle", "Rudrakota", "Tallapalem"),
                    "Gudur" to listOf("Gudur Rural", "Chennuru", "Kandra", "Mekanuru", "Nellaturu", "Palicherla", "Vendodu", "Yellasiri")
                )
            )
        ),
        StateData(
            stateName = "Telangana",
            stateNameTelugu = "తెలంగాణ",
            stateNameHindi = "तेलंगाना",
            districts = mapOf(
                "Warangal" to mapOf(
                    "Warangal" to listOf("Warangal Rural", "Bollikunta", "Enumamula", "Gorrekunta", "Mamnoor", "Nakkalapally", "Paidipally", "Thimmapur"),
                    "Narsampet" to listOf("Narsampet Town", "Akkaraju Pally", "Banjara Pally", "Chennaraopet", "Ithikyalapally", "Kamalapur", "Madannapet", "Muthojipet"),
                    "Wardhannapet" to listOf("Wardhannapet Village", "Bandautlapally", "Dharmaram", "Katrapally", "Konareddypally", "Nellikudur", "Upparapally"),
                    "Geesugonda" to listOf("Geesugonda Village", "Bodduchintalapally", "Dharmaram", "Gorrekunta", "Kommagudem", "Mogilicherla", "Viswanathpur")
                ),
                "Karimnagar" to mapOf(
                    "Karimnagar" to listOf("Karimnagar Rural", "Arepally", "Bommakal", "Chinthakunta", "Durshed", "Elgandal", "Mankammathota", "Theegalaguttapally"),
                    "Huzurabad" to listOf("Huzurabad Town", "Bornapally", "Chelpur", "Dharmarajupally", "Jammikunta", "Kaniparthi", "Peddapally", "Sirsehadu"),
                    "Choppadandi" to listOf("Choppadandi Village", "Arnikantha", "Chinnakalwala", "Gumlapur", "Katnapally", "Kolimikunta", "Raghunathpally")
                ),
                "Nalgonda" to mapOf(
                    "Nalgonda" to listOf("Nalgonda Rural", "Appajipeta", "Bandapalem", "Cherlapally", "Dandampally", "Kanagal", "Marriguda", "Panagal"),
                    "Miryalaguda" to listOf("Miryalaguda Town", "Alagadapa", "Chinthapally", "Gudur", "Keshawapuram", "Rayannagudem", "Tadakamalla", "Venkatadripet"),
                    "Devarakonda" to listOf("Devarakonda Town", "Chintapalle", "Gundlapally", "Kambalapally", "Kondamallepally", "Padamati Pally", "Seripally")
                ),
                "Khammam" to mapOf(
                    "Khammam" to listOf("Khammam Rural", "Arempula", "Danavaigudem", "Edulapuram", "Gudimalla", "Mallemadugu", "Polepally", "Tekulapally"),
                    "Madhira" to listOf("Madhira Town", "Allinagaram", "Atkuru", "Chilukur", "Dendukuru", "Khammampadu", "Nagulavancha", "Rommimpudi"),
                    "Sathupalli" to listOf("Sathupalli Town", "Bethupalli", "Gangaram", "Kistaram", "Rejarla", "Rudrakshapalli", "Sadasivunipalem", "Yellandu")
                )
            )
        ),
        StateData(
            stateName = "Karnataka",
            stateNameTelugu = "కర్ణాటక",
            stateNameHindi = "कर्नाटक",
            districts = mapOf(
                "Ballari (Bellary)" to mapOf(
                    "Ballari" to listOf("Ballari Rural", "Halakundi", "Kolagal", "Kudatini", "Moka", "Rupanagudi", "Sanganakallu"),
                    "Siruguppa" to listOf("Siruguppa Town", "Agasanur", "Deshnur", "Hatcholli", "Karur", "Raravi", "Tekkalakote")
                ),
                "Raichur" to mapOf(
                    "Raichur" to listOf("Raichur Rural", "Askihal", "Chandrabanda", "Ghatbichkod", "Kalmala", "Malleswaram", "Yermarus"),
                    "Sindhanur" to listOf("Sindhanur Town", "Alabanur", "Gorebal", "Jalihal", "Puldinni", "Salagunda", "Turvihal")
                )
            )
        ),
        StateData(
            stateName = "Maharashtra",
            stateNameTelugu = "మహారాష్ట్ర",
            stateNameHindi = "महाराष्ट्र",
            districts = mapOf(
                "Nagpur" to mapOf(
                    "Katol" to listOf("Katol Town", "Kondhali", "Metpanjra", "Paradsinga", "Pardi", "Ridhora"),
                    "Saoner" to listOf("Saoner Town", "Kelod", "Khapa", "Malegaon", "Patansawangi", "Waki")
                ),
                "Nashik" to mapOf(
                    "Niphad" to listOf("Niphad Town", "Lasalgaon", "Ozar", "Pimpalgaon Baswant", "Saykheda", "Vinchur"),
                    "Sinnar" to listOf("Sinnar Town", "Baragaon Pimpri", "Dapur", "Musalgaon", "Pandhurli", "Wavi")
                )
            )
        )
    )

    fun getState(name: String): StateData? {
        return states.find { it.stateName.equals(name, ignoreCase = true) }
    }

    fun getDistricts(stateName: String): List<String> {
        val s = getState(stateName) ?: states.first()
        return s.districts.keys.toList().sorted()
    }

    fun getMandals(stateName: String, districtName: String): List<String> {
        val s = getState(stateName) ?: states.first()
        val distMap = s.districts[districtName] ?: s.districts.values.firstOrNull() ?: return listOf("Main Mandal")
        return distMap.keys.toList().sorted()
    }

    fun getVillagesForMandal(stateName: String, districtName: String, mandalName: String): List<String> {
        val s = getState(stateName) ?: states.first()
        val distMap = s.districts[districtName] ?: s.districts.values.firstOrNull() ?: return listOf("Village Center")
        val villages = distMap[mandalName] ?: distMap.values.firstOrNull() ?: listOf("Main Village")
        return villages.sorted()
    }

    // Geocoding mapper: finds best matching state, district, mandal, village from GPS coordinates or city name
    fun findLocationByGps(latitude: Double, longitude: Double): LocationMatch {
        // Andhra Pradesh / Guntur bounding box
        return if (latitude in 15.0..17.5 && longitude in 79.5..81.5) {
            LocationMatch("Andhra Pradesh", "Guntur", "Duggirala", "Duggirala Village")
        } else if (latitude in 16.5..19.0 && longitude in 78.0..80.5) {
            LocationMatch("Telangana", "Warangal", "Warangal", "Warangal Rural")
        } else if (latitude in 14.0..16.5 && longitude in 75.5..78.0) {
            LocationMatch("Karnataka", "Ballari (Bellary)", "Ballari", "Ballari Rural")
        } else {
            // Default closest agricultural region
            LocationMatch("Andhra Pradesh", "Guntur", "Duggirala", "Duggirala Village")
        }
    }

    data class LocationMatch(
        val state: String,
        val district: String,
        val mandal: String,
        val village: String
    )
}

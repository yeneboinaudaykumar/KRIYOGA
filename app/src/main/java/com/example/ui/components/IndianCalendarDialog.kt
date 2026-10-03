package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Language

// Model for a calendar day with lunar & festival details
data class CalendarDayInfo(
    val dayNumber: Int,
    val isAmavasya: Boolean = false,
    val isPournami: Boolean = false,
    val isEkadashi: Boolean = false,
    val festivalNameTelugu: String? = null,
    val festivalNameHindi: String? = null,
    val festivalNameEnglish: String? = null,
    val tithiTelugu: String = "",
    val tithiHindi: String = "",
    val agroTipTelugu: String? = null,
    val agroTipEnglish: String? = null
)

data class MonthPanchang(
    val monthIndex: Int, // 1 to 12
    val monthNameEnglish: String,
    val monthNameTelugu: String,
    val monthNameHindi: String,
    val teluguMasam: String,
    val startDayOfWeek: Int, // 0 = Sunday, 1 = Monday, etc.
    val totalDays: Int,
    val daysInfo: Map<Int, CalendarDayInfo>
)

object IndianPanchangCalendarData {

    val monthsData: List<MonthPanchang> = listOf(
        MonthPanchang(
            monthIndex = 1,
            monthNameEnglish = "January",
            monthNameTelugu = "జనవరి",
            monthNameHindi = "जनवरी",
            teluguMasam = "పుష్య / మాఘ మాసం",
            startDayOfWeek = 4, // Thursday
            totalDays = 31,
            daysInfo = mapOf(
                13 to CalendarDayInfo(13, festivalNameTelugu = "భోగి పండుగ", festivalNameHindi = "लोहड़ी / भोगी", festivalNameEnglish = "Bhogi Festival", tithiTelugu = "శుద్ధ ఏకాదశి", agroTipTelugu = "ధాన్యపు కుప్పల నూర్పిడి, పాత వ్యర్థాల దహనం."),
                14 to CalendarDayInfo(14, festivalNameTelugu = "మకర సంక్రాంతి", festivalNameHindi = "मकर संक्रांति", festivalNameEnglish = "Makara Sankranti", tithiTelugu = "మకర సంక్రమణం", agroTipTelugu = "ధాన్య లక్ష్మి పూజ, కొత్త బియ్యం నైవేద్యం, రబీ సంబరాలు."),
                15 to CalendarDayInfo(15, festivalNameTelugu = "కనుమ (పశువుల పండుగ)", festivalNameHindi = "कनुमा (पशु पूजा)", festivalNameEnglish = "Kanuma (Cattle Day)", tithiTelugu = "బహుళ పాడ్యమి", agroTipTelugu = "వ్యవసాయ ఎద్దులు, పాడి పశువుల అలంకరణ, పూజ."),
                18 to CalendarDayInfo(18, isAmavasya = true, tithiTelugu = "పుష్య అమావాస్య 🌑", agroTipTelugu = "రాత్రి వేళల్లో పంటలకు నీటి పారుదల పరిశీలన."),
                26 to CalendarDayInfo(26, festivalNameTelugu = "రిపబ్లిక్ డే", festivalNameEnglish = "Republic Day", tithiTelugu = "శుద్ధ సప్తమి"),
                31 to CalendarDayInfo(31, isPournami = true, tithiTelugu = "మాఘ పౌర్ణమి 🌕", agroTipTelugu = "చలి తీవ్రత తగ్గి ఎండలు ఆరంభం; పప్పు ధాన్యాల రక్షణ.")
            )
        ),
        MonthPanchang(
            monthIndex = 2,
            monthNameEnglish = "February",
            monthNameTelugu = "ఫిబ్రవరి",
            monthNameHindi = "फरवरी",
            teluguMasam = "మాఘ / ఫాల్గుణ మాసం",
            startDayOfWeek = 0, // Sunday
            totalDays = 28,
            daysInfo = mapOf(
                12 to CalendarDayInfo(12, festivalNameTelugu = "రథసప్తమి (సూర్య జయంతి)", festivalNameHindi = "रथ सप्तमी", festivalNameEnglish = "Ratha Saptami", tithiTelugu = "మాఘ శుద్ధ సప్తమి", agroTipTelugu = "సూర్య భగవానుడి ఆరాధన; వేసవి పంటల ప్రణాళిక."),
                16 to CalendarDayInfo(16, isAmavasya = true, tithiTelugu = "మాఘ అమావాస్య 🌑"),
                26 to CalendarDayInfo(26, festivalNameTelugu = "మహా శివరాత్రి", festivalNameHindi = "महाशिवरात्रि", festivalNameEnglish = "Maha Shivaratri", tithiTelugu = "మాఘ బహుళ చతుర్దశి", agroTipTelugu = "రబీ పంటల కోతలకు అనుకూలమైన వాతావరణం."),
                28 to CalendarDayInfo(28, isPournami = true, tithiTelugu = "ఫాల్గుణ పౌర్ణమి 🌕", agroTipTelugu = "వేసవి దుక్కులకు పొలాన్ని సిద్ధం చేసుకోవడం.")
            )
        ),
        MonthPanchang(
            monthIndex = 3,
            monthNameEnglish = "March",
            monthNameTelugu = "మార్చి",
            monthNameHindi = "मार्च",
            teluguMasam = "ఫాల్గుణ / చైత్ర మాసం",
            startDayOfWeek = 0,
            totalDays = 31,
            daysInfo = mapOf(
                14 to CalendarDayInfo(14, festivalNameTelugu = "హోలీ (కామదహనం)", festivalNameHindi = "होली", festivalNameEnglish = "Holi", tithiTelugu = "ఫాల్గుణ పూర్ణిమ 🌕", agroTipTelugu = "వసంత ఋతువు ఆరంభం; పంట మార్పిడి చర్యలు."),
                19 to CalendarDayInfo(19, isAmavasya = true, tithiTelugu = "ఫాల్గుణ అమావాస్య 🌑"),
                30 to CalendarDayInfo(30, festivalNameTelugu = "ఉగాది (తెలుగు నూతన సంవత్సరం)", festivalNameHindi = "गुड़ी पड़वा", festivalNameEnglish = "Ugadi / Gudi Padwa", tithiTelugu = "చైత్ర శుద్ధ పాడ్యమి", agroTipTelugu = "నూతన పంచాంగ శ్రవణం; రాబోయే వర్షాలు, పంటల దిగుబడుల అంచనా.")
            )
        ),
        MonthPanchang(
            monthIndex = 4,
            monthNameEnglish = "April",
            monthNameTelugu = "ఏప్రిల్",
            monthNameHindi = "अप्रैल",
            teluguMasam = "చైత్ర / వైశాఖ మాసం",
            startDayOfWeek = 3,
            totalDays = 30,
            daysInfo = mapOf(
                7 to CalendarDayInfo(7, festivalNameTelugu = "శ్రీరామనవమి", festivalNameHindi = "श्री राम नवमी", festivalNameEnglish = "Sri Rama Navami", tithiTelugu = "చైత్ర శుద్ధ నవమి"),
                14 to CalendarDayInfo(14, festivalNameTelugu = "తమిళ న్యూ ఇయర్ / బైశాఖి", festivalNameHindi = "बैसाखी", festivalNameEnglish = "Baisakhi Harvest", tithiTelugu = "మేష సంక్రమణం", agroTipTelugu = "పంజాబ్, హర్యానాలో గోధుమ కోతల పండుగ."),
                17 to CalendarDayInfo(17, isAmavasya = true, tithiTelugu = "చైత్ర అమావాస్య 🌑"),
                29 to CalendarDayInfo(29, isPournami = true, tithiTelugu = "వైశాఖ పౌర్ణమి (బుద్ధ పూర్ణిమ) 🌕", agroTipTelugu = "బోరుబావుల నీటి మట్టాల పర్యవేక్షణ.")
            )
        ),
        MonthPanchang(
            monthIndex = 5,
            monthNameEnglish = "May",
            monthNameTelugu = "మే",
            monthNameHindi = "मई",
            teluguMasam = "వైశాఖ / జ్యేష్ఠ మాసం",
            startDayOfWeek = 5,
            totalDays = 31,
            daysInfo = mapOf(
                10 to CalendarDayInfo(10, festivalNameTelugu = "హనుమాన్ జయంతి", festivalNameEnglish = "Hanuman Jayanti", tithiTelugu = "వైశాఖ బహుళ దశమి"),
                16 to CalendarDayInfo(16, isAmavasya = true, tithiTelugu = "వైశాఖ అమావాస్య 🌑"),
                28 to CalendarDayInfo(28, isPournami = true, tithiTelugu = "జ్యేష్ఠ పౌర్ణమి 🌕", agroTipTelugu = "రోహిణి కార్తె ఎండలు; విత్తన శుద్ధికి ఎండబెట్టడం.")
            )
        ),
        MonthPanchang(
            monthIndex = 6,
            monthNameEnglish = "June",
            monthNameTelugu = "జూన్",
            monthNameHindi = "जून",
            teluguMasam = "జ్యేష్ఠ / ఆషాఢ మాసం",
            startDayOfWeek = 1,
            totalDays = 30,
            daysInfo = mapOf(
                11 to CalendarDayInfo(11, festivalNameTelugu = "ఏరువాక పున్నమి (నాగలి పూజ)", festivalNameHindi = "एरुवाका पूर्णिमा (हल पूजा)", festivalNameEnglish = "Eruvaka Punnami (Kisan Tilling Day)", isPournami = true, tithiTelugu = "జ్యేష్ఠ శుద్ధ పౌర్ణమి 🌕", agroTipTelugu = "ఖరీఫ్ సీజన్ తొలి దుక్కి! నాగలిని పూజించి నైరుతి రుతుపవన విత్తనాలు వేసే పవిత్ర దినం."),
                15 to CalendarDayInfo(15, isAmavasya = true, tithiTelugu = "జ్యేష్ఠ అమావాస్య 🌑", agroTipTelugu = "తొలకరి వర్షాల కోసం భూమిని సిద్ధం చేసుకోవడం."),
                21 to CalendarDayInfo(21, festivalNameTelugu = "అంతర్జాతీయ యోగా దినోత్సవం", festivalNameEnglish = "International Yoga Day", tithiTelugu = "ఆషాఢ శుద్ధ షష్ఠి")
            )
        ),
        MonthPanchang(
            monthIndex = 7,
            monthNameEnglish = "July",
            monthNameTelugu = "జూలై",
            monthNameHindi = "जुलाई",
            teluguMasam = "ఆషాఢ / శ్రావణ మాసం",
            startDayOfWeek = 3,
            totalDays = 31,
            daysInfo = mapOf(
                10 to CalendarDayInfo(10, festivalNameTelugu = "తొలి ఏకాదశి (శయన ఏకాదశి)", festivalNameHindi = "देवशयनी एकादशी", festivalNameEnglish = "Tholi Ekadashi", isEkadashi = true, tithiTelugu = "ఆషాఢ శుద్ధ ఏకాదశి", agroTipTelugu = "వరి నాట్లకు అత్యంత అనుకూలమైన పుణ్య కాలం."),
                14 to CalendarDayInfo(14, festivalNameTelugu = "గురు పౌర్ణమి", festivalNameEnglish = "Guru Purnima", isPournami = true, tithiTelugu = "ఆషాఢ పౌర్ణమి 🌕"),
                15 to CalendarDayInfo(15, isAmavasya = true, tithiTelugu = "ఆషాఢ అమావాస్య 🌑")
            )
        ),
        MonthPanchang(
            monthIndex = 8,
            monthNameEnglish = "August",
            monthNameTelugu = "ఆగస్టు",
            monthNameHindi = "अगस्त",
            teluguMasam = "శ్రావణ / భాద్రపద మాసం",
            startDayOfWeek = 6,
            totalDays = 31,
            daysInfo = mapOf(
                8 to CalendarDayInfo(8, festivalNameTelugu = "వరలక్ష్మీ వ్రతం", festivalNameHindi = "वरलक्ष्मी व्रत", festivalNameEnglish = "Varalakshmi Vratam", tithiTelugu = "శ్రావణ శుద్ధ శుక్రవారం"),
                14 to CalendarDayInfo(14, isAmavasya = true, tithiTelugu = "శ్రావణ అమావాస్య / పోలా పండుగ 🌑", festivalNameTelugu = "పోలా పండుగ (ఎద్దుల పూజ)", agroTipTelugu = "మహారాష్ట్ర, తెలంగాణలో ఎద్దులను పూజించి వ్యవసాయానికి విశ్రాంతి."),
                15 to CalendarDayInfo(15, festivalNameTelugu = "స్వాతంత్ర్య దినోత్సవం", festivalNameEnglish = "Independence Day", tithiTelugu = "భాద్రపద శుద్ధ పాడ్యమి"),
                20 to CalendarDayInfo(20, festivalNameTelugu = "రాఖీ పౌర్ణమి / జంధ్యాల పూర్ణిమ", festivalNameHindi = "रक्षाबंधन", festivalNameEnglish = "Raksha Bandhan", isPournami = true, tithiTelugu = "శ్రావణ పూర్ణిమ 🌕", agroTipTelugu = "పంటలపై మొదటి దశ కలుపు నివారణ మరియు ఎరువుల మోతాదు.")
            )
        ),
        MonthPanchang(
            monthIndex = 9,
            monthNameEnglish = "September",
            monthNameTelugu = "సెప్టెంబరు",
            monthNameHindi = "सितंबर",
            teluguMasam = "భాద్రపద / ఆశ్వయుజ మాసం",
            startDayOfWeek = 2,
            totalDays = 30,
            daysInfo = mapOf(
                4 to CalendarDayInfo(4, festivalNameTelugu = "శ్రీకృష్ణ జన్మాష్టమి", festivalNameHindi = "कृष्ण जन्माष्टमी", festivalNameEnglish = "Janmashtami", tithiTelugu = "శ్రావణ బహుళ అష్టమి"),
                7 to CalendarDayInfo(7, festivalNameTelugu = "వినాయక చవితి (గణేష్ చతుర్థి)", festivalNameHindi = "गणेश चतुर्थी", festivalNameEnglish = "Vinayaka Chavithi", tithiTelugu = "భాద్రపద శుద్ధ చవితి", agroTipTelugu = "పత్తి, మిర్చి పూత దశ; కాయ తొలుచు పురుగుల రక్షణ సమయం."),
                12 to CalendarDayInfo(12, isAmavasya = true, tithiTelugu = "మహాలయ అమావాస్య (పెత్రమాస) 🌑", agroTipTelugu = "ఖరీఫ్ మధ్యంతర నీటి యాజమాన్యం."),
                18 to CalendarDayInfo(18, isPournami = true, tithiTelugu = "భాద్రపద పూర్ణిమ 🌕")
            )
        ),
        MonthPanchang(
            monthIndex = 10,
            monthNameEnglish = "October",
            monthNameTelugu = "అక్టోబరు",
            monthNameHindi = "अक्टूबर",
            teluguMasam = "ఆశ్వయుజ / కార్తీక మాసం",
            startDayOfWeek = 4,
            totalDays = 31,
            daysInfo = mapOf(
                2 to CalendarDayInfo(2, festivalNameTelugu = "గాంధీ జయంతి / దేవీ నవరాత్రులు ఆరంభం", festivalNameEnglish = "Gandhi Jayanti / Navratri Start", tithiTelugu = "ఆశ్వయుజ శుద్ధ పాడ్యమి"),
                11 to CalendarDayInfo(11, festivalNameTelugu = "ఆయుధ పూజ (ట్రాక్టర్, పనిముట్ల పూజ)", festivalNameHindi = "आयुध पूजा", festivalNameEnglish = "Ayudha Pooja", tithiTelugu = "ఆశ్వయుజ నవమి", agroTipTelugu = "రైతులు ట్రాక్టర్లు, స్ప్రేయర్లు, మోటార్లు, వ్యవసాయ పనిముట్లకు పూజ చేస్తారు."),
                12 to CalendarDayInfo(12, festivalNameTelugu = "విజయదశమి (దసరా)", festivalNameHindi = "दशहरा", festivalNameEnglish = "Dasara / Vijayadashami", tithiTelugu = "ఆశ్వయుజ దశమి", agroTipTelugu = "పంట కోతలకు ముందస్తు విజయ సంబరం."),
                13 to CalendarDayInfo(13, isAmavasya = true, tithiTelugu = "ఆశ్వయుజ అమావాస్య 🌑"),
                17 to CalendarDayInfo(17, isPournami = true, tithiTelugu = "శరద్ పూర్ణిమ 🌕"),
                31 to CalendarDayInfo(31, festivalNameTelugu = "నరక చతుర్దశి & దీపావళి లక్ష్మీ పూజ", festivalNameHindi = "दीपावली / लक्ष्मी पूजन", festivalNameEnglish = "Deepavali (Diwali)", isAmavasya = true, tithiTelugu = "ఆశ్వయుజ అమావాస్య 🌑", agroTipTelugu = "ధాన్య లక్ష్మి పూజ; రబీ శనగ, మినుము విత్తనాలు వేసే సమయం.")
            )
        ),
        MonthPanchang(
            monthIndex = 11,
            monthNameEnglish = "November",
            monthNameTelugu = "నవంబరు",
            monthNameHindi = "नवंबर",
            teluguMasam = "కార్తీక / మార్గశిర మాసం",
            startDayOfWeek = 0,
            totalDays = 30,
            daysInfo = mapOf(
                5 to CalendarDayInfo(5, festivalNameTelugu = "నాగుల చవితి", festivalNameEnglish = "Nagula Chavithi", tithiTelugu = "కార్తీక శుద్ధ చవితి", agroTipTelugu = "పొలాల్లో ఎలుకలను అరికట్టే పాములను పూజించే రైతు ఆచారం."),
                11 to CalendarDayInfo(11, isAmavasya = true, tithiTelugu = "కార్తీక అమావాస్య 🌑"),
                15 to CalendarDayInfo(15, festivalNameTelugu = "కార్తీక పౌర్ణమి (దీపాల పండుగ)", festivalNameHindi = "कार्तिक पूर्णिमा", festivalNameEnglish = "Kartika Pournami", isPournami = true, tithiTelugu = "కార్తీక పూర్ణిమ 🌕", agroTipTelugu = "శీతాకాల పంటల తేమ సంరక్షణ, చీడపీడల నివారణ."),
                23 to CalendarDayInfo(23, festivalNameTelugu = "సుబ్రహ్మణ్య షష్ఠి", festivalNameEnglish = "Subrahmanya Sashti", tithiTelugu = "మార్గశిర శుద్ధ షష్ఠి")
            )
        ),
        MonthPanchang(
            monthIndex = 12,
            monthNameEnglish = "December",
            monthNameTelugu = "డిసెంబరు",
            monthNameHindi = "दिसंबर",
            teluguMasam = "మార్గశిర / పుష్య మాసం",
            startDayOfWeek = 2,
            totalDays = 31,
            daysInfo = mapOf(
                11 to CalendarDayInfo(11, isAmavasya = true, tithiTelugu = "మార్గశిర అమావాస్య 🌑"),
                15 to CalendarDayInfo(15, isPournami = true, tithiTelugu = "మార్గశిర పౌర్ణమి (దత్తాత్రేయ జయంతి) 🌕"),
                23 to CalendarDayInfo(23, festivalNameTelugu = "కిసాన్ దివస్ (జాతీయ రైతు దినోత్సవం)", festivalNameHindi = "राष्ट्रीय किसान दिवस", festivalNameEnglish = "National Farmer Day (Kisan Diwas)", tithiTelugu = "పుష్య శుద్ధ తదియ", agroTipTelugu = "భారత మాజీ ప్రధాని చౌదరి చరణ్ సింగ్ జయంతి సందర్భంగా జాతీయ రైతు దినోత్సవం!"),
                25 to CalendarDayInfo(25, festivalNameTelugu = "క్రిస్మస్ (Christmas)", festivalNameEnglish = "Christmas", tithiTelugu = "పుష్య శుద్ధ పంచమి")
            )
        )
    )
}

@Composable
fun IndianCalendarDialog(
    userState: String,
    currentLanguage: Language,
    onDismiss: () -> Unit
) {
    // Current month selector (defaults to current month: October = index 9)
    var currentMonthIndex by remember { mutableIntStateOf(9) } // 0-based index for October
    val currentMonth = IndianPanchangCalendarData.monthsData[currentMonthIndex]

    // Selected date for displaying bottom details
    var selectedDayNumber by remember { mutableIntStateOf(11) } // Default highlight
    val selectedDayInfo = currentMonth.daysInfo[selectedDayNumber]

    val weekDays = when (currentLanguage) {
        Language.TELUGU -> listOf("ఆది", "సోమ", "మంగళ", "బుధ", "గురు", "శుక్ర", "శని")
        Language.HINDI -> listOf("रवि", "सोम", "मंगल", "बुध", "गुरु", "शुक्र", "शनि")
        Language.ENGLISH -> listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFB300)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = null, tint = Color(0xFF1B5E20), modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = when (currentLanguage) {
                                Language.TELUGU -> "భారతీయ పంచాంగ క్యాలెండర్"
                                Language.HINDI -> "भारतीय पंचांग कैलेंडर"
                                Language.ENGLISH -> "Indian Calendar"
                            },
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "$userState Edition",
                                fontSize = 11.sp,
                                color = Color(0xFF2E7D32),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 520.dp)
                    .testTag("indian_calendar_month_view")
            ) {
                // Month Navigation Header [ < Month Name > ]
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFE8F5E9),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2E7D32).copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 6.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                if (currentMonthIndex > 0) {
                                    currentMonthIndex--
                                    selectedDayNumber = 1
                                }
                            },
                            enabled = currentMonthIndex > 0,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Prev Month")
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${when (currentLanguage) {
                                    Language.TELUGU -> currentMonth.monthNameTelugu
                                    Language.HINDI -> currentMonth.monthNameHindi
                                    Language.ENGLISH -> currentMonth.monthNameEnglish
                                }} 2026",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 15.sp,
                                color = Color(0xFF1B5E20)
                            )
                            Text(
                                text = currentMonth.teluguMasam,
                                fontSize = 11.sp,
                                color = Color(0xFFE65100),
                                fontWeight = FontWeight.Bold
                            )
                        }

                        IconButton(
                            onClick = {
                                if (currentMonthIndex < IndianPanchangCalendarData.monthsData.size - 1) {
                                    currentMonthIndex++
                                    selectedDayNumber = 1
                                }
                            },
                            enabled = currentMonthIndex < IndianPanchangCalendarData.monthsData.size - 1,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next Month")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Weekday Labels Row (Sun, Mon, Tue, ...)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    weekDays.forEachIndexed { index, dayName ->
                        Text(
                            text = dayName,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (index == 0) Color(0xFFC62828) else Color(0xFF37474F),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Month Dates Grid with Festivals, Amavasya, Pournami below dates
                val totalCells = currentMonth.startDayOfWeek + currentMonth.totalDays
                val rows = (totalCells + 6) / 7

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFFAFAFA))
                        .padding(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    for (rowIndex in 0 until rows) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            for (colIndex in 0 until 7) {
                                val cellIndex = rowIndex * 7 + colIndex
                                val dayNum = cellIndex - currentMonth.startDayOfWeek + 1

                                if (dayNum in 1..currentMonth.totalDays) {
                                    val dayInfo = currentMonth.daysInfo[dayNum]
                                    val isSelected = selectedDayNumber == dayNum
                                    val isSunday = colIndex == 0

                                    // Determine day cell badge/style
                                    val isAmavasya = dayInfo?.isAmavasya == true
                                    val isPournami = dayInfo?.isPournami == true
                                    val hasFestival = dayInfo?.festivalNameTelugu != null

                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = when {
                                            isSelected -> Color(0xFF2E7D32)
                                            isAmavasya -> Color(0xFF263238) // Dark Slate for Amavasya
                                            isPournami -> Color(0xFFFFF8E1) // Golden glow for Pournami
                                            hasFestival -> Color(0xFFFFF3E0) // Warm festive amber
                                            else -> Color.White
                                        },
                                        border = if (isPournami && !isSelected) androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFB300)) else null,
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(52.dp)
                                            .padding(1.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable { selectedDayNumber = dayNum }
                                            .testTag("cal_day_$dayNum")
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center,
                                            modifier = Modifier.padding(2.dp)
                                        ) {
                                            // Date Number
                                            Text(
                                                text = "$dayNum",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp,
                                                color = when {
                                                    isSelected -> Color.White
                                                    isAmavasya -> Color.White
                                                    isSunday -> Color(0xFFC62828)
                                                    else -> Color.Black
                                                }
                                            )

                                            // Under Date: Festival / Amavasya / Pournami Badge
                                            when {
                                                isAmavasya -> {
                                                    Text(
                                                        text = "🌑 అమావాస్య",
                                                        fontSize = 8.sp,
                                                        color = Color(0xFFFFD54F),
                                                        fontWeight = FontWeight.Bold,
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis
                                                    )
                                                }
                                                isPournami -> {
                                                    Text(
                                                        text = "🌕 పౌర్ణమి",
                                                        fontSize = 8.sp,
                                                        color = if (isSelected) Color.White else Color(0xFFE65100),
                                                        fontWeight = FontWeight.Bold,
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis
                                                    )
                                                }
                                                hasFestival -> {
                                                    val festLabel = when (currentLanguage) {
                                                        Language.TELUGU -> dayInfo?.festivalNameTelugu ?: ""
                                                        Language.HINDI -> dayInfo?.festivalNameHindi ?: dayInfo?.festivalNameTelugu ?: ""
                                                        Language.ENGLISH -> dayInfo?.festivalNameEnglish ?: dayInfo?.festivalNameTelugu ?: ""
                                                    }
                                                    Text(
                                                        text = festLabel,
                                                        fontSize = 8.sp,
                                                        color = if (isSelected) Color.White else Color(0xFFD84315),
                                                        fontWeight = FontWeight.Bold,
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis
                                                    )
                                                }
                                                else -> {
                                                    Spacer(modifier = Modifier.height(10.dp))
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    Spacer(modifier = Modifier.weight(1f).height(52.dp))
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Detailed Selected Date Card with Panchangam and Agro Advice
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2E7D32).copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "తేదీ: $selectedDayNumber ${currentMonth.monthNameTelugu} 2026",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color(0xFF1B5E20)
                            )
                            if (selectedDayInfo?.isAmavasya == true) {
                                Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFF263238)) {
                                    Text("🌑 అమావాస్య (Amavasya)", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                            } else if (selectedDayInfo?.isPournami == true) {
                                Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFFFB300)) {
                                    Text("🌕 పౌర్ణమి (Pournami)", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                            }
                        }

                        if (selectedDayInfo?.festivalNameTelugu != null) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Celebration, contentDescription = null, tint = Color(0xFFE65100), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "పండుగ: ${selectedDayInfo.festivalNameTelugu}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color(0xFFBF360C)
                                )
                            }
                        }

                        if (selectedDayInfo?.tithiTelugu?.isNotBlank() == true) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.NightsStay, contentDescription = null, tint = Color.DarkGray, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "తిథి: ${selectedDayInfo.tithiTelugu}",
                                    fontSize = 11.sp,
                                    color = Color.DarkGray
                                )
                            }
                        }

                        if (selectedDayInfo?.agroTipTelugu != null) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color.White,
                                modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                            ) {
                                Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.Top) {
                                    Icon(imageVector = Icons.Default.WbSunny, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(14.dp).padding(top = 2.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "రైతు సలహా: ${selectedDayInfo.agroTipTelugu}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF1B5E20),
                                        lineHeight = 15.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("సరే / OK")
            }
        }
    )
}

package com.example.data.ai

import android.graphics.Bitmap
import android.util.Base64
import com.example.BuildConfig
import com.example.model.Language
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit

object GeminiClient {
    private const val MODEL_NAME = "gemini-3.5-flash"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    fun isApiKeyAvailable(): Boolean {
        return try {
            val key = BuildConfig.GEMINI_API_KEY
            !key.isNullOrBlank() && key != "MY_GEMINI_API_KEY"
        } catch (e: Throwable) {
            false
        }
    }

    /**
     * Ask Krishi Guru AI with language context (Telugu, Hindi, English)
     */
    suspend fun askKrishiGuru(prompt: String, language: Language): String = withContext(Dispatchers.IO) {
        if (!isApiKeyAvailable()) {
            return@withContext AgroExpertEngine.getOfflineChatResponse(prompt, language)
        }

        try {
            val systemInstructions = when (language) {
                Language.TELUGU -> "మీరు భారతీయ రైతులకు సహాయపడే 'కృషి మిత్ర AI' నిపుణుల సలహాదారు. తెలుగులో స్పష్టంగా, మర్యాదగా మరియు ఆచరణాత్మక వ్యవసాయ సలహాలు (విత్తనాలు, తెగుళ్లు, ఎరువులు, సేంద్రీయ పద్ధతులు, మార్కెట్ ధరలు) ఇవ్వండి. సమాధానాన్ని చదవడానికి సులభంగా ఉండేలా సంక్షిప్తంగా మరియు బుల్లెట్ పాయింట్లలో ఇవ్వండి."
                Language.HINDI -> "आप भारतीय किसानों के मार्गदर्शक 'कृषि मित्र AI' विशेषज्ञ हैं। किसानों के सवालों का सरल, व्यावहारिक और आदरणीय हिंदी में उत्तर दें (फसल रोग, खाद, सिंचाई, जैविक उपाय, मंडी भाव)। उत्तर को स्पष्ट, बुलेट पॉइंट्स में और आसानी से समझने योग्य रखें।"
                Language.ENGLISH -> "You are 'KrishiMitra AI', an expert agricultural advisor for Indian farmers. Provide practical, localized agronomy guidance (soil nutrition, pest control, organic remedies, irrigation, government schemes like PM-KISAN, crop calendar). Keep responses concise, structured with bullet points, and easy for farmers to understand."
            }

            val requestJson = JSONObject().apply {
                val contents = JSONArray().apply {
                    put(JSONObject().apply {
                        put("role", "user")
                        put("parts", JSONArray().apply {
                            put(JSONObject().put("text", "$systemInstructions\n\nFarmer Question: $prompt"))
                        })
                    })
                }
                put("contents", contents)

                put("generationConfig", JSONObject().apply {
                    put("temperature", 0.4)
                    put("topP", 0.9)
                })
            }

            val url = "$BASE_URL/$MODEL_NAME:generateContent?key=${BuildConfig.GEMINI_API_KEY}"
            val request = Request.Builder()
                .url(url)
                .post(requestJson.toString().toRequestBody(jsonMediaType))
                .build()

            val response = okHttpClient.newCall(request).execute()
            if (!response.isSuccessful) {
                return@withContext AgroExpertEngine.getOfflineChatResponse(prompt, language)
            }

            val responseBody = response.body?.string().orEmpty()
            val responseJson = JSONObject(responseBody)
            val candidates = responseJson.optJSONArray("candidates")
            val candidate = candidates?.optJSONObject(0)
            val content = candidate?.optJSONObject("content")
            val parts = content?.optJSONArray("parts")
            val text = parts?.optJSONObject(0)?.optString("text")

            if (!text.isNullOrBlank()) {
                text.trim()
            } else {
                AgroExpertEngine.getOfflineChatResponse(prompt, language)
            }
        } catch (e: Exception) {
            AgroExpertEngine.getOfflineChatResponse(prompt, language)
        }
    }

    /**
     * AI Multimodal Diagnosis from Leaf/Crop Image
     */
    suspend fun diagnoseCropImage(bitmap: Bitmap, cropHint: String, language: Language): DiagnosisResult = withContext(Dispatchers.IO) {
        if (!isApiKeyAvailable()) {
            return@withContext AgroExpertEngine.getOfflineDiagnosis(cropHint, language)
        }

        try {
            val base64Data = bitmap.toBase64()
            val prompt = """
                Analyze this crop leaf/plant photo for diseases, pests, nutrient deficiencies, or fungal infections in Indian agriculture.
                Crop hint: $cropHint.
                Respond with structured JSON strictly in this format:
                {
                   "diseaseName": "Disease Name in English",
                   "diseaseNameTelugu": "తెలుగులో పేరు",
                   "diseaseNameHindi": "हिंदी में नाम",
                   "severityScore": 65,
                   "symptoms": "Detailed visual symptoms observed",
                   "organicRemedy": "Eco-friendly, neem-based or bio-fungicide remedy",
                   "chemicalRemedy": "Standard CIBRC-approved pesticide/fungicide with exact dosage per liter",
                   "preventiveCare": "Good agricultural practices to prevent recurrence"
                }
            """.trimIndent()

            val requestJson = JSONObject().apply {
                val contents = JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().put("text", prompt))
                            put(JSONObject().apply {
                                put("inlineData", JSONObject().apply {
                                    put("mimeType", "image/jpeg")
                                    put("data", base64Data)
                                })
                            })
                        })
                    })
                }
                put("contents", contents)
                put("generationConfig", JSONObject().apply {
                    put("responseMimeType", "application/json")
                    put("temperature", 0.2)
                })
            }

            val url = "$BASE_URL/$MODEL_NAME:generateContent?key=${BuildConfig.GEMINI_API_KEY}"
            val request = Request.Builder()
                .url(url)
                .post(requestJson.toString().toRequestBody(jsonMediaType))
                .build()

            val response = okHttpClient.newCall(request).execute()
            if (!response.isSuccessful) {
                return@withContext AgroExpertEngine.getOfflineDiagnosis(cropHint, language)
            }

            val responseBody = response.body?.string().orEmpty()
            val responseJson = JSONObject(responseBody)
            val candidates = responseJson.optJSONArray("candidates")
            val candidate = candidates?.optJSONObject(0)
            val content = candidate?.optJSONObject("content")
            val parts = content?.optJSONArray("parts")
            val rawJsonText = parts?.optJSONObject(0)?.optString("text").orEmpty()

            val cleanJson = if (rawJsonText.startsWith("```json")) {
                rawJsonText.substringAfter("```json").substringBeforeLast("```").trim()
            } else if (rawJsonText.startsWith("```")) {
                rawJsonText.substringAfter("```").substringBeforeLast("```").trim()
            } else {
                rawJsonText.trim()
            }

            val diagObj = JSONObject(cleanJson)
            DiagnosisResult(
                diseaseName = diagObj.optString("diseaseName", "Leaf Spot & Fungal Infection"),
                diseaseNameTelugu = diagObj.optString("diseaseNameTelugu", "ఆకు మచ్చ మరియు శిలీంధ్ర తెగులు"),
                diseaseNameHindi = diagObj.optString("diseaseNameHindi", "पत्ती धब्बा एवं कवक रोग"),
                severityScore = diagObj.optInt("severityScore", 60),
                symptoms = diagObj.optString("symptoms", "Dark necrotic lesions with chlorotic yellow halo on leaf lamina."),
                organicRemedy = diagObj.optString("organicRemedy", "Spray 5ml Neem Oil (10,000 ppm) + Trichoderma viride @ 5g/L water."),
                chemicalRemedy = diagObj.optString("chemicalRemedy", "Spray Mancozeb 75% WP @ 2.5g/L or Azoxystrobin 23% SC @ 1ml/L."),
                preventiveCare = diagObj.optString("preventiveCare", "Avoid overhead sprinkler irrigation, maintain proper plant spacing, and clear weed hosts.")
            )
        } catch (e: Exception) {
            AgroExpertEngine.getOfflineDiagnosis(cropHint, language)
        }
    }

    private fun Bitmap.toBase64(): String {
        val stream = ByteArrayOutputStream()
        // Compress efficiently to stay within reasonable payload limits
        val scaled = if (width > 1024 || height > 1024) {
            val ratio = Math.min(1024f / width, 1024f / height)
            Bitmap.createScaledBitmap(this, (width * ratio).toInt(), (height * ratio).toInt(), true)
        } else {
            this
        }
        scaled.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val bytes = stream.toByteArray()
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }
}

data class DiagnosisResult(
    val diseaseName: String,
    val diseaseNameTelugu: String,
    val diseaseNameHindi: String,
    val severityScore: Int,
    val symptoms: String,
    val organicRemedy: String,
    val chemicalRemedy: String,
    val preventiveCare: String
)

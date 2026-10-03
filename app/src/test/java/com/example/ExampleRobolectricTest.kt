package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.ai.AgroExpertEngine
import com.example.model.Language
import com.example.model.SoilType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("KRIYOGA", appName)
    }

    @Test
    fun `test agro expert fertilizer dose calculation`() {
        val dose = AgroExpertEngine.calculateFertilizerDose("Paddy", 2.0, 240, 20, 260, 6.8)
        assertTrue(dose.ureaBags > 0)
        assertTrue(dose.dapBags > 0)
        assertTrue(dose.mopBags > 0)
    }

    @Test
    fun `test yield prediction`() {
        val yield = AgroExpertEngine.predictYield("Paddy", 2.5, SoilType.BLACK_COTTON, "Canal / River")
        assertTrue(yield.totalQuintals > 0)
        assertTrue(yield.confidencePercentage in 70..99)
    }

    @Test
    fun `test multilingual chat engine offline fallback`() {
        val teluguResponse = AgroExpertEngine.getOfflineChatResponse("వరి లో తెగులు", Language.TELUGU)
        assertTrue(teluguResponse.contains("వరి"))

        val hindiResponse = AgroExpertEngine.getOfflineChatResponse("धान में झुलसा", Language.HINDI)
        assertTrue(hindiResponse.contains("धान") || hindiResponse.contains("गेहूं"))
    }
}

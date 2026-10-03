package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onFinish: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Animation states
    val scale = remember { Animatable(0.4f) }
    val alphaLogo = remember { Animatable(0f) }
    val alphaTitle = remember { Animatable(0f) }
    val alphaSubtitle = remember { Animatable(0f) }
    val progressAnim = remember { Animatable(0f) }

    // Pulsing glow transition
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    LaunchedEffect(Unit) {
        // Step 1: Logo zoom and fade in (0 - 800ms)
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
        )
        alphaLogo.animateTo(1f, animationSpec = tween(400))

        // Step 2: App Name large alphabets fade and slide (800 - 1500ms)
        alphaTitle.animateTo(1f, animationSpec = tween(700))

        // Step 3: "ITS FARMERS TRUST APP" badge appear (1500 - 2200ms)
        alphaSubtitle.animateTo(1f, animationSpec = tween(600))

        // Step 4: Progress bar filling (2000 - 3600ms)
        progressAnim.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1400, easing = LinearEasing)
        )

        // Hold briefly and transition
        delay(200)
        onFinish()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F3D1F), // Deep rich emerald
                        Color(0xFF0B2B16),
                        Color(0xFF05170B)  // Forest night
                    )
                )
            )
            .testTag("kriyoga_splash_screen")
    ) {
        // Skip Button top right so user can enter immediately if in a hurry
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White.copy(alpha = 0.15f),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 48.dp, end = 20.dp)
                .clip(RoundedCornerShape(20.dp))
                .clickable { onFinish() }
                .testTag("splash_skip_button")
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "SKIP",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.9f),
                    modifier = Modifier.size(14.dp)
                )
            }
        }

        // Central Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Animated Icon Emblem with glowing ring
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .scale(scale.value * pulseScale)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0xFFFFB74D), // Golden amber glow
                                Color(0xFF2E7D32),
                                Color(0xFF1B5E20)
                            )
                        )
                    )
                    .border(3.dp, Color(0xFFFFD54F), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Agriculture,
                    contentDescription = "KRIYOGA Emblem",
                    tint = Color.White,
                    modifier = Modifier.size(68.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // LARGE ALPHABETS OF APP NAME: K R I Y O G A
            Text(
                text = "K R I Y O G A",
                fontSize = 42.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFFFFF9C4), // Warm golden wheat
                letterSpacing = 6.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .alpha(alphaTitle.value)
                    .scale(scale.value)
                    .testTag("splash_app_title")
            )

            Spacer(modifier = Modifier.height(14.dp))

            // PROMINENT TRUST BADGE MESSAGE
            // "ITS FARMERS TRUST APP"
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = Color(0xFF1E5128).copy(alpha = 0.85f),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFFFB300)),
                modifier = Modifier
                    .alpha(alphaSubtitle.value)
                    .padding(horizontal = 8.dp)
                    .testTag("splash_trust_message")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Verified Trust",
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "ITS FARMERS TRUST APP",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 1.2.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Secondary localized motto
            Text(
                text = "భారతీయ రైతులకు అత్యంత విశ్వసనీయమైన వేదిక • किसानों का सच्चा साथी",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.75f),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .alpha(alphaSubtitle.value)
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Developer Credit for Yeneboina Udaykumar
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.Black.copy(alpha = 0.5f),
                border = BorderStroke(1.2.dp, Color(0xFFFFD54F).copy(alpha = 0.85f)),
                modifier = Modifier
                    .alpha(alphaSubtitle.value)
                    .testTag("splash_developer_credit")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Code,
                        contentDescription = "Developer",
                        tint = Color(0xFFFFD54F),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Developed by Yeneboina Udaykumar",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFECB3),
                        letterSpacing = 0.6.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(44.dp))

            // Progress bar and loading status
            Column(
                modifier = Modifier
                    .width(240.dp)
                    .alpha(alphaSubtitle.value),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LinearProgressIndicator(
                    progress = { progressAnim.value },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(CircleShape),
                    color = Color(0xFFFFB300),
                    trackColor = Color.White.copy(alpha = 0.2f)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Connecting Satellite & Agro Intelligence...",
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.65f)
                )
            }
        }

        // Bottom Footer
        Text(
            text = "AI Powered • Real-time Mandi • Disease Scanner • 100% Secure",
            fontSize = 10.sp,
            color = Color.White.copy(alpha = 0.5f),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
        )
    }
}

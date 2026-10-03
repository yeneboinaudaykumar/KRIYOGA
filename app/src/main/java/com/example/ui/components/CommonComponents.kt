package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.FarmerProfileEntity
import com.example.model.KrishiStrings
import com.example.model.Language
import com.example.model.WeatherInfo
import com.example.ui.AppNavTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KrishiTopAppBar(
    farmerProfile: FarmerProfileEntity?,
    currentLanguage: Language,
    onLanguageSelected: (Language) -> Unit,
    onProfileClick: () -> Unit,
    onFinanceClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var langMenuExpanded by remember { mutableStateOf(false) }
    val strings = KrishiStrings.get(currentLanguage)

    TopAppBar(
        modifier = modifier.testTag("krishi_top_app_bar"),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onProfileClick() }
                    .padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Agriculture,
                        contentDescription = "Farmer Logo",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = strings.appName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "${farmerProfile?.name ?: "Farmer"} • ${farmerProfile?.village ?: "Guntur"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.85f),
                        maxLines = 1
                    )
                }
            }
        },
        actions = {
            // Finance Quick Shortcut
            IconButton(
                onClick = onFinanceClick,
                modifier = Modifier.testTag("top_bar_finance_button")
            ) {
                Icon(
                    imageVector = Icons.Default.AttachMoney,
                    contentDescription = strings.navFinance,
                    tint = MaterialTheme.colorScheme.secondaryContainer
                )
            }

            // Language Selector Button
            Box {
                Surface(
                    onClick = { langMenuExpanded = true },
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .testTag("language_switch_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Select Language",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = currentLanguage.nativeName,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                DropdownMenu(
                    expanded = langMenuExpanded,
                    onDismissRequest = { langMenuExpanded = false }
                ) {
                    Language.values().forEach { lang ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "${lang.displayName} (${lang.nativeName})",
                                    fontWeight = if (lang == currentLanguage) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            onClick = {
                                onLanguageSelected(lang)
                                langMenuExpanded = false
                            },
                            modifier = Modifier.testTag("lang_option_${lang.code}")
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun WeatherAlertBanner(
    weather: WeatherInfo,
    currentLanguage: Language,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (!weather.isAlertActive) return
    val strings = KrishiStrings.get(currentLanguage)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("weather_alert_banner"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF3E0),
            contentColor = Color(0xFFBF360C)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFCC80)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Weather Alert",
                    tint = Color(0xFFD84315),
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = strings.weatherAlertTitle,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFBF360C)
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = strings.weatherAlertDesc,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF4E342E),
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
fun KrishiBottomNavigationBar(
    currentTab: AppNavTab,
    currentLanguage: Language,
    onTabSelected: (AppNavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val strings = KrishiStrings.get(currentLanguage)

    NavigationBar(
        modifier = modifier.testTag("bottom_nav_bar"),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        val items = listOf(
            Triple(AppNavTab.HOME, strings.navHome, Icons.Default.Home),
            Triple(AppNavTab.CROPS_HEALTH, strings.navCrops, Icons.Default.Grass),
            Triple(AppNavTab.ADVISORY_SOIL, strings.navSoil, Icons.Default.Science),
            Triple(AppNavTab.MANDI_MARKET, strings.navMandi, Icons.Default.Storefront),
            Triple(AppNavTab.AI_GURU, strings.navGuru, Icons.Default.Chat)
        )

        items.forEach { (tab, label, icon) ->
            val selected = currentTab == tab
            NavigationBarItem(
                modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}"),
                selected = selected,
                onClick = { onTabSelected(tab) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                        maxLines = 1
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.outline,
                    unselectedTextColor = MaterialTheme.colorScheme.outline
                )
            )
        }
    }
}

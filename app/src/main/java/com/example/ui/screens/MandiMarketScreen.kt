package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.MandiPriceEntity
import com.example.data.local.MarketplaceListingEntity
import com.example.model.ColdStorageItem
import com.example.model.KrishiStrings
import com.example.model.Language
import com.example.model.PostHarvestGuide

@Composable
fun MandiMarketScreen(
    mandiPrices: List<MandiPriceEntity>,
    marketplaceListings: List<MarketplaceListingEntity>,
    coldStorageList: List<ColdStorageItem>,
    postHarvestGuides: List<PostHarvestGuide>,
    currentLanguage: Language,
    onAddListingClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val strings = KrishiStrings.get(currentLanguage)

    var selectedTab by remember { mutableIntStateOf(0) } // 0: Live Mandi Bhav, 1: Farmer-to-Buyer Market, 2: Cold Chain & Storage
    var searchQuery by remember { mutableStateOf("") }
    var selectedCommodityFilter by remember { mutableStateOf("All") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("mandi_market_screen")
    ) {
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text(strings.tabLiveMandi, fontWeight = FontWeight.Bold) },
                modifier = Modifier.testTag("tab_mandi_prices")
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text(strings.tabMarketplace, fontWeight = FontWeight.Bold) },
                modifier = Modifier.testTag("tab_farmer_market")
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text(strings.tabColdStorage, fontWeight = FontWeight.Bold) },
                modifier = Modifier.testTag("tab_cold_storage")
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            when (selectedTab) {
                0 -> {
                    // TAB 0: Live Mandi Bhav & 30-Day Trends
                    item {
                        // AI Best Time to Sell advisory
                        Card(
                            modifier = Modifier.fillMaxWidth().testTag("sell_timing_advisory_card"),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.TrendingUp, contentDescription = null, tint = Color(0xFFF57F17))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(strings.priceTimingTitle, fontWeight = FontWeight.Bold, color = Color(0xFFE65100))
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = strings.priceTimingDesc,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF5D4037)
                                )
                            }
                        }
                    }

                    // Search Field
                    item {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text(strings.searchMandiPlaceholder) },
                            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search") },
                            modifier = Modifier.fillMaxWidth().testTag("mandi_search_input"),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    // Commodity quick chips
                    item {
                        val commodities = listOf("All", "Chilli", "Paddy", "Cotton", "Wheat", "Soybean", "Tomato")
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(commodities) { comm ->
                                val label = when (comm) {
                                    "All" -> if (currentLanguage == Language.TELUGU) "అన్నీ" else if (currentLanguage == Language.HINDI) "सभी" else "All"
                                    "Chilli" -> if (currentLanguage == Language.TELUGU) "మిర్చి" else if (currentLanguage == Language.HINDI) "मिर्च" else "Chilli"
                                    "Paddy" -> if (currentLanguage == Language.TELUGU) "వరి" else if (currentLanguage == Language.HINDI) "धान" else "Paddy"
                                    "Cotton" -> if (currentLanguage == Language.TELUGU) "పత్తి" else if (currentLanguage == Language.HINDI) "कपास" else "Cotton"
                                    "Wheat" -> if (currentLanguage == Language.TELUGU) "గోధుమ" else if (currentLanguage == Language.HINDI) "गेहूं" else "Wheat"
                                    "Soybean" -> if (currentLanguage == Language.TELUGU) "సోయాబీన్" else if (currentLanguage == Language.HINDI) "सोयाबीन" else "Soybean"
                                    "Tomato" -> if (currentLanguage == Language.TELUGU) "టమోటా" else if (currentLanguage == Language.HINDI) "टमाटर" else "Tomato"
                                    else -> comm
                                }
                                FilterChip(
                                    selected = selectedCommodityFilter == comm,
                                    onClick = { selectedCommodityFilter = comm },
                                    label = { Text(label) }
                                )
                            }
                        }
                    }

                    // Mandi Price Cards
                    val filteredPrices = mandiPrices.filter { price ->
                        val matchesSearch = price.commodity.contains(searchQuery, ignoreCase = true) ||
                                price.marketName.contains(searchQuery, ignoreCase = true) ||
                                price.state.contains(searchQuery, ignoreCase = true)
                        val matchesFilter = selectedCommodityFilter == "All" || price.commodity.contains(selectedCommodityFilter, ignoreCase = true)
                        matchesSearch && matchesFilter
                    }

                    items(filteredPrices) { price ->
                        MandiPriceCard(price = price, currentLanguage = currentLanguage)
                    }
                }

                1 -> {
                    // TAB 1: Direct Farmer-to-Buyer Marketplace
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(strings.directHubTitle, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text(strings.directHubSubtitle, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = onAddListingClick,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.testTag("add_produce_button")
                            ) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(strings.listProduceBtn)
                            }
                        }
                    }

                    items(marketplaceListings) { listing ->
                        MarketplaceCard(
                            listing = listing,
                            currentLanguage = currentLanguage,
                            onCall = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${listing.contactPhone}"))
                                context.startActivity(intent)
                            }
                        )
                    }
                }

                2 -> {
                    // TAB 2: Cold Storage & Warehouses + Post-Harvest Guides
                    item {
                        Text(strings.nearbyColdStorageTitle, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(strings.coldStorageSubtitle, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }

                    items(coldStorageList) { item ->
                        ColdStorageCard(
                            facility = item,
                            currentLanguage = currentLanguage,
                            onCall = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${item.contactPhone}"))
                                context.startActivity(intent)
                            }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(strings.postHarvestTitle, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }

                    items(postHarvestGuides) { guide ->
                        PostHarvestGuideCard(guide = guide, currentLanguage = currentLanguage)
                    }
                }
            }
        }
    }
}

@Composable
fun MandiPriceCard(price: MandiPriceEntity, currentLanguage: Language) {
    val strings = KrishiStrings.get(currentLanguage)

    Card(
        modifier = Modifier.fillMaxWidth().testTag("mandi_card_${price.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = price.commodity, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(
                        text = "${price.marketName} • ${price.district}, ${price.state}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "₹${price.modalPricePerQuintal.toInt()}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(text = strings.perQuintal, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${strings.priceRange}: ₹${price.minPricePerQuintal.toInt()} - ₹${price.maxPricePerQuintal.toInt()}",
                    style = MaterialTheme.typography.bodySmall
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (price.priceChange24h >= 0) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                        contentDescription = null,
                        tint = if (price.priceChange24h >= 0) Color(0xFF2E7D32) else Color(0xFFC62828),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${if (price.priceChange24h >= 0) "+" else ""}₹${price.priceChange24h.toInt()}",
                        color = if (price.priceChange24h >= 0) Color(0xFF2E7D32) else Color(0xFFC62828),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            if (price.mspGovernmentPrice > 0) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${strings.govtMsp}: ₹${price.mspGovernmentPrice.toInt()}/Qtl",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun MarketplaceCard(listing: MarketplaceListingEntity, currentLanguage: Language, onCall: () -> Unit) {
    val strings = KrishiStrings.get(currentLanguage)

    ElevatedCard(
        modifier = Modifier.fillMaxWidth().testTag("market_listing_${listing.id}"),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (listing.isBuyerDemand) Color(0xFFE3F2FD) else Color(0xFFE8F5E9)
                ) {
                    Text(
                        text = if (listing.isBuyerDemand) strings.buyerDemandBadge else strings.farmerSellingBadge,
                        color = if (listing.isBuyerDemand) Color(0xFF0D47A1) else Color(0xFF1B5E20),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Text(listing.location, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(listing.cropName, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    Text("${listing.variety} • ${listing.quantityQuintals} ${strings.quintals}", style = MaterialTheme.typography.bodySmall)
                }
                Text("₹${listing.expectedPricePerQuintal.toInt()}/Q", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)
            }

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(listing.farmerName, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
                Button(
                    onClick = onCall,
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Icon(imageVector = Icons.Default.Call, contentDescription = "Call", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(strings.callNowBtn, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun ColdStorageCard(facility: ColdStorageItem, currentLanguage: Language, onCall: () -> Unit) {
    val strings = KrishiStrings.get(currentLanguage)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(facility.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                    Text("${facility.location} • ${facility.distanceKm} km", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFE0F2F1)
                ) {
                    Text("${facility.availableSpaceMt} MT ${strings.availableSpace}", color = Color(0xFF00695C), fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text("${strings.temperature}: ${facility.temperatureRange}", style = MaterialTheme.typography.bodySmall)
            Text("${strings.commodities}: ${facility.commoditiesAccepted}", style = MaterialTheme.typography.bodySmall, color = Color(0xFF37474F))

            Spacer(modifier = Modifier.height(6.dp))
            Text(facility.governmentSubsidyInfo, style = MaterialTheme.typography.labelSmall, color = Color(0xFF2E7D32), fontWeight = FontWeight.SemiBold)

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onCall,
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Icon(imageVector = Icons.Default.Call, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(strings.callFacilityBtn, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun PostHarvestGuideCard(guide: PostHarvestGuide, currentLanguage: Language) {
    val strings = KrishiStrings.get(currentLanguage)

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = "${guide.crop}: ${guide.title}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = guide.lossReductionTip, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("${strings.targetMoisture}: ${guide.moistureTarget}", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                Text("${strings.packaging}: ${guide.recommendedPackaging}", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }
        }
    }
}

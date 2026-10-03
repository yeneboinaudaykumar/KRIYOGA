package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.FarmExpenseEntity
import com.example.model.KrishiStrings
import com.example.model.Language

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinanceScreen(
    expenses: List<FarmExpenseEntity>,
    financialSummary: Pair<Triple<Double, Double, Double>, Double>,
    currentLanguage: Language,
    onAddExpenseClick: () -> Unit,
    onDeleteExpense: (Int) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val strings = KrishiStrings.get(currentLanguage)
    val (spent, revenue, netProfit) = financialSummary.first
    val costPerAcre = financialSummary.second
    var selectedCategoryFilter by remember { mutableStateOf("All") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.financeScreenTitle, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddExpenseClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                modifier = Modifier.testTag("fab_add_expense")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = strings.recordExpenseIncomeBtn)
            }
        },
        modifier = modifier.testTag("finance_screen")
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Financial Summary KPI Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("finance_kpi_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(strings.financeOverviewTitle, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text(strings.totalRevenue, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                Text("₹${revenue.toInt()}", fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32), fontSize = 18.sp)
                            }
                            Column {
                                Text(strings.inputCosts, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                Text("₹${spent.toInt()}", fontWeight = FontWeight.Bold, color = Color(0xFFC62828), fontSize = 18.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text(strings.farmProfit, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                Text(
                                    text = "${if (netProfit >= 0) "+" else ""}₹${netProfit.toInt()}",
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (netProfit >= 0) Color(0xFF2E7D32) else Color(0xFFC62828),
                                    fontSize = 20.sp
                                )
                            }
                            Column {
                                Text(strings.costPerAcre, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                Text("₹${costPerAcre.toInt()}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            }
                        }
                    }
                }
            }

            // Category Filter Chips
            item {
                val categories = listOf("All", "Seeds", "Fertilizers", "Labor", "Pesticides", "Machinery", "Revenue")
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(categories) { cat ->
                        val catLabel = when (cat) {
                            "All" -> if (currentLanguage == Language.TELUGU) "అన్నీ" else if (currentLanguage == Language.HINDI) "सभी" else "All"
                            "Seeds" -> if (currentLanguage == Language.TELUGU) "విత్తనాలు" else if (currentLanguage == Language.HINDI) "बीज" else "Seeds"
                            "Fertilizers" -> if (currentLanguage == Language.TELUGU) "ఎరువులు" else if (currentLanguage == Language.HINDI) "खाद" else "Fertilizers"
                            "Labor" -> if (currentLanguage == Language.TELUGU) "కూలీలు" else if (currentLanguage == Language.HINDI) "मजदूरी" else "Labor"
                            "Pesticides" -> if (currentLanguage == Language.TELUGU) "మందులు" else if (currentLanguage == Language.HINDI) "कीटनाशक" else "Pesticides"
                            "Machinery" -> if (currentLanguage == Language.TELUGU) "ట్రాక్టర్/యంత్రాలు" else if (currentLanguage == Language.HINDI) "मशीनरी" else "Machinery"
                            "Revenue" -> if (currentLanguage == Language.TELUGU) "పంట ఆదాయం" else if (currentLanguage == Language.HINDI) "आमदनी" else "Revenue"
                            else -> cat
                        }
                        FilterChip(
                            selected = selectedCategoryFilter == cat,
                            onClick = { selectedCategoryFilter = cat },
                            label = { Text(catLabel) }
                        )
                    }
                }
            }

            // Expense Items
            val filtered = expenses.filter {
                if (selectedCategoryFilter == "All") true
                else if (selectedCategoryFilter == "Revenue") it.isRevenue
                else it.category.contains(selectedCategoryFilter, ignoreCase = true)
            }

            items(filtered) { item ->
                ExpenseItemCard(item = item, onDelete = { onDeleteExpense(item.id) })
            }
        }
    }
}

@Composable
fun ExpenseItemCard(item: FarmExpenseEntity, onDelete: () -> Unit) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (item.isRevenue) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                    modifier = Modifier.size(38.dp)
                ) {
                    Icon(
                        imageVector = if (item.isRevenue) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                        contentDescription = null,
                        tint = if (item.isRevenue) Color(0xFF2E7D32) else Color(0xFFC62828),
                        modifier = Modifier.padding(8.dp)
                    )
                }

                Column(modifier = Modifier.padding(start = 12.dp)) {
                    Text(item.category, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                    Text(item.description, style = MaterialTheme.typography.bodySmall, color = Color.Gray, maxLines = 1)
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${if (item.isRevenue) "+" else "-"}₹${item.amountRupees.toInt()}",
                    fontWeight = FontWeight.Bold,
                    color = if (item.isRevenue) Color(0xFF2E7D32) else Color(0xFFC62828),
                    fontSize = 16.sp
                )
                IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color.LightGray, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

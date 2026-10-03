package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.data.local.FarmPlotEntity
import com.example.data.local.FarmerProfileEntity
import com.example.model.IrrigationSource
import com.example.model.KrishiStrings
import com.example.model.Language
import com.example.model.SoilType

@Composable
fun FarmerProfileDialog(
    profile: FarmerProfileEntity?,
    currentLanguage: Language,
    onDismiss: () -> Unit,
    onSave: (name: String, phone: String, village: String, district: String, state: String, acres: Double) -> Unit,
    onSignOut: () -> Unit
) {
    val strings = KrishiStrings.get(currentLanguage)

    var name by remember { mutableStateOf(profile?.name ?: "") }
    var phone by remember { mutableStateOf(profile?.phone ?: "") }
    var village by remember { mutableStateOf(profile?.village ?: "") }
    var district by remember { mutableStateOf(profile?.district ?: "") }
    var state by remember { mutableStateOf(profile?.state ?: "") }
    var acreageText by remember { mutableStateOf(profile?.totalAcreage?.toString() ?: "3.0") }
    var showSignOutConfirm by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(strings.farmerProfileTitle, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(strings.fullName) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("profile_name_input")
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text(strings.phone) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("profile_phone_input")
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = village,
                        onValueChange = { village = it },
                        label = { Text(strings.village) },
                        modifier = Modifier.weight(1f).testTag("profile_village_input")
                    )
                    OutlinedTextField(
                        value = district,
                        onValueChange = { district = it },
                        label = { Text(strings.district) },
                        modifier = Modifier.weight(1f).testTag("profile_district_input")
                    )
                }
                OutlinedTextField(
                    value = state,
                    onValueChange = { state = it },
                    label = { Text(strings.state) },
                    modifier = Modifier.fillMaxWidth().testTag("profile_state_input")
                )
                OutlinedTextField(
                    value = acreageText,
                    onValueChange = { acreageText = it },
                    label = { Text(strings.landHolding) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth().testTag("profile_acreage_input")
                )

                // Sign Out Button inside Profile Dialog
                Spacer(modifier = Modifier.size(6.dp))
                OutlinedButton(
                    onClick = { showSignOutConfirm = true },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFC62828)),
                    border = BorderStroke(1.2.dp, Color(0xFFC62828)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().testTag("profile_sign_out_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "Sign Out",
                        tint = Color(0xFFC62828),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = when (currentLanguage) {
                            Language.TELUGU -> "లాగ్ అవుట్ చేయండి (Sign Out)"
                            Language.HINDI -> "लॉग आउट करें (Sign Out)"
                            Language.ENGLISH -> "Sign Out"
                        },
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFC62828)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val acres = acreageText.toDoubleOrNull() ?: 4.0
                    onSave(name, phone, village, district, state, acres)
                    onDismiss()
                },
                modifier = Modifier.testTag("save_profile_button")
            ) {
                Text(strings.saveProfile)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(strings.cancel)
            }
        }
    )

    if (showSignOutConfirm) {
        AlertDialog(
            onDismissRequest = { showSignOutConfirm = false },
            title = {
                Text(
                    text = when (currentLanguage) {
                        Language.TELUGU -> "లాగ్ అవుట్ నిర్ధారణ"
                        Language.HINDI -> "लॉग आउट की पुष्टि"
                        Language.ENGLISH -> "Sign Out Confirmation"
                    },
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = when (currentLanguage) {
                        Language.TELUGU -> "మీరు నిజంగా KRIYOGA నుండి లాగ్ అవుట్ అవ్వాలనుకుంటున్నారా? తదుపరి లాగిన్ కోసం మొదటి పేజీ కనిపిస్తుంది."
                        Language.HINDI -> "क्या आप वास्तव में KRIYOGA से लॉग आउट करना चाहते हैं?"
                        Language.ENGLISH -> "Are you sure you want to sign out? You will need to enter your details again on next login."
                    }
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSignOutConfirm = false
                        onDismiss()
                        onSignOut()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828))
                ) {
                    Text(
                        text = when (currentLanguage) {
                            Language.TELUGU -> "అవును, లాగ్ అవుట్"
                            Language.HINDI -> "हाँ, लॉग आउट"
                            Language.ENGLISH -> "Yes, Sign Out"
                        }
                    )
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showSignOutConfirm = false }) {
                    Text(strings.cancel)
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPlotDialog(
    currentLanguage: Language,
    onDismiss: () -> Unit,
    onAdd: (FarmPlotEntity) -> Unit
) {
    val strings = KrishiStrings.get(currentLanguage)

    var plotName by remember { mutableStateOf("") }
    var acreage by remember { mutableStateOf("2.0") }
    var crop by remember { mutableStateOf(if (currentLanguage == Language.TELUGU) "వరి" else if (currentLanguage == Language.HINDI) "धान" else "Paddy") }
    var variety by remember { mutableStateOf("BPT 5204") }
    var selectedSoil by remember { mutableStateOf(SoilType.BLACK_COTTON.label) }
    var selectedIrrigation by remember { mutableStateOf(IrrigationSource.BOREWELL.label) }

    var soilDropdownExpanded by remember { mutableStateOf(false) }
    var irrigationDropdownExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(strings.addPlotTitle, fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = plotName,
                    onValueChange = { plotName = it },
                    label = { Text(strings.plotName) },
                    modifier = Modifier.fillMaxWidth().testTag("plot_name_input")
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = acreage,
                        onValueChange = { acreage = it },
                        label = { Text(strings.acresField) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f).testTag("plot_acres_input")
                    )
                    OutlinedTextField(
                        value = crop,
                        onValueChange = { crop = it },
                        label = { Text(strings.cropField) },
                        modifier = Modifier.weight(1f).testTag("plot_crop_input")
                    )
                }
                OutlinedTextField(
                    value = variety,
                    onValueChange = { variety = it },
                    label = { Text("విత్తన రకం / Variety") },
                    modifier = Modifier.fillMaxWidth().testTag("plot_variety_input")
                )

                // Soil Type Dropdown
                ExposedDropdownMenuBox(
                    expanded = soilDropdownExpanded,
                    onExpandedChange = { soilDropdownExpanded = !soilDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedSoil,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(strings.soilType) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = soilDropdownExpanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable, true).testTag("plot_soil_dropdown")
                    )
                    ExposedDropdownMenu(
                        expanded = soilDropdownExpanded,
                        onDismissRequest = { soilDropdownExpanded = false }
                    ) {
                        SoilType.values().forEach { st ->
                            DropdownMenuItem(
                                text = { Text(st.label) },
                                onClick = {
                                    selectedSoil = st.label
                                    soilDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                // Irrigation Source Dropdown
                ExposedDropdownMenuBox(
                    expanded = irrigationDropdownExpanded,
                    onExpandedChange = { irrigationDropdownExpanded = !irrigationDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedIrrigation,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(strings.irrigationSource) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = irrigationDropdownExpanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable, true).testTag("plot_irrigation_dropdown")
                    )
                    ExposedDropdownMenu(
                        expanded = irrigationDropdownExpanded,
                        onDismissRequest = { irrigationDropdownExpanded = false }
                    ) {
                        IrrigationSource.values().forEach { ir ->
                            DropdownMenuItem(
                                text = { Text(ir.label) },
                                onClick = {
                                    selectedIrrigation = ir.label
                                    irrigationDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (plotName.isNotBlank()) {
                        val acres = acreage.toDoubleOrNull() ?: 1.0
                        onAdd(
                            FarmPlotEntity(
                                plotName = plotName,
                                acreage = acres,
                                soilType = selectedSoil,
                                primaryCrop = crop,
                                cropVariety = variety,
                                irrigationSource = selectedIrrigation,
                                sowingDateEpoch = System.currentTimeMillis() - 86400000L * 15
                            )
                        )
                        onDismiss()
                    }
                },
                modifier = Modifier.testTag("confirm_add_plot_button")
            ) {
                Text(strings.addPlotConfirm)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text(strings.cancel) }
        }
    )
}

@Composable
fun AddExpenseDialog(
    currentLanguage: Language,
    onDismiss: () -> Unit,
    onAdd: (category: String, amount: Double, desc: String, isIncome: Boolean) -> Unit
) {
    val strings = KrishiStrings.get(currentLanguage)

    var category by remember { mutableStateOf(if (currentLanguage == Language.TELUGU) "ఎరువులు" else if (currentLanguage == Language.HINDI) "खाद" else "Fertilizers") }
    var amountText by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isRevenue by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isRevenue) strings.recordRevenueTitle else strings.recordExpenseTitle, fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(if (isRevenue) strings.recordRevenueTitle else strings.recordExpenseTitle, fontWeight = FontWeight.Medium)
                    Switch(
                        checked = isRevenue,
                        onCheckedChange = { isRevenue = it },
                        modifier = Modifier.testTag("expense_income_toggle")
                    )
                }

                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text(strings.amountRupees) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("expense_amount_input")
                )

                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text(strings.category) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("expense_category_input")
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(strings.description) },
                    modifier = Modifier.fillMaxWidth().testTag("expense_desc_input")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = amountText.toDoubleOrNull() ?: 0.0
                    if (amount > 0) {
                        onAdd(category, amount, description, isRevenue)
                        onDismiss()
                    }
                },
                modifier = Modifier.testTag("save_expense_button")
            ) {
                Text(strings.saveProfile)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text(strings.cancel) }
        }
    )
}

@Composable
fun AddProduceDialog(
    currentLanguage: Language,
    onDismiss: () -> Unit,
    onAdd: (crop: String, variety: String, quintals: Double, price: Double, location: String, isDemand: Boolean) -> Unit
) {
    val strings = KrishiStrings.get(currentLanguage)

    var crop by remember { mutableStateOf(if (currentLanguage == Language.TELUGU) "మిర్చి" else if (currentLanguage == Language.HINDI) "मिर्च" else "Chilli") }
    var variety by remember { mutableStateOf("Teja Dry") }
    var quintalsText by remember { mutableStateOf("50") }
    var priceText by remember { mutableStateOf("21000") }
    var location by remember { mutableStateOf("Guntur, AP") }
    var isDemand by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isDemand) strings.postDemandTitle else strings.listProduceTitle, fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(if (isDemand) strings.buyerDemandBadge else strings.farmerSellingBadge, fontWeight = FontWeight.Medium)
                    Switch(
                        checked = isDemand,
                        onCheckedChange = { isDemand = it },
                        modifier = Modifier.testTag("produce_demand_toggle")
                    )
                }

                OutlinedTextField(
                    value = crop,
                    onValueChange = { crop = it },
                    label = { Text(strings.cropField) },
                    modifier = Modifier.fillMaxWidth().testTag("produce_crop_input")
                )
                OutlinedTextField(
                    value = variety,
                    onValueChange = { variety = it },
                    label = { Text("రకం / Variety") },
                    modifier = Modifier.fillMaxWidth().testTag("produce_variety_input")
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = quintalsText,
                        onValueChange = { quintalsText = it },
                        label = { Text(strings.quantityQuintals) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f).testTag("produce_qty_input")
                    )
                    OutlinedTextField(
                        value = priceText,
                        onValueChange = { priceText = it },
                        label = { Text(strings.expectedPrice) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f).testTag("produce_price_input")
                    )
                }
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text(strings.pickupLocation) },
                    modifier = Modifier.fillMaxWidth().testTag("produce_location_input")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val q = quintalsText.toDoubleOrNull() ?: 10.0
                    val p = priceText.toDoubleOrNull() ?: 2000.0
                    onAdd(crop, variety, q, p, location, isDemand)
                    onDismiss()
                },
                modifier = Modifier.testTag("confirm_list_produce_button")
            ) {
                Text(strings.publishListing)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text(strings.cancel) }
        }
    )
}

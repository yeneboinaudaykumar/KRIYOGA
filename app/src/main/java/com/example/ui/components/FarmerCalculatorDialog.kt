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
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Language

@Composable
fun FarmerCalculatorDialog(
    currentLanguage: Language,
    onDismiss: () -> Unit
) {
    // Calculator internal state
    var displayValue by remember { mutableStateOf("0") }
    var expressionHistory by remember { mutableStateOf("") }
    var firstOperand by remember { mutableStateOf<Double?>(null) }
    var activeOperator by remember { mutableStateOf<String?>(null) }
    var resetOnNextDigit by remember { mutableStateOf(false) }

    fun onDigit(d: String) {
        if (displayValue == "0" || resetOnNextDigit) {
            displayValue = d
            resetOnNextDigit = false
        } else {
            if (displayValue.length < 12) {
                displayValue += d
            }
        }
    }

    fun onDecimal() {
        if (resetOnNextDigit) {
            displayValue = "0."
            resetOnNextDigit = false
        } else if (!displayValue.contains(".")) {
            displayValue += "."
        }
    }

    fun onOperator(op: String) {
        val current = displayValue.toDoubleOrNull() ?: 0.0
        val previous = firstOperand

        if (previous != null && activeOperator != null && !resetOnNextDigit) {
            // Compute intermediate result
            val result = when (activeOperator) {
                "+" -> previous + current
                "-" -> previous - current
                "×" -> previous * current
                "÷" -> if (current != 0.0) previous / current else 0.0
                else -> current
            }
            displayValue = if (result % 1.0 == 0.0) result.toLong().toString() else "%.2f".format(result)
            firstOperand = result
        } else {
            firstOperand = current
        }

        activeOperator = op
        expressionHistory = "${displayValue} $op"
        resetOnNextDigit = true
    }

    fun onEquals() {
        val current = displayValue.toDoubleOrNull() ?: 0.0
        val previous = firstOperand
        val op = activeOperator

        if (previous != null && op != null) {
            expressionHistory = "${if (previous % 1.0 == 0.0) previous.toLong().toString() else "%.2f".format(previous)} $op $displayValue ="
            val result = when (op) {
                "+" -> previous + current
                "-" -> previous - current
                "×" -> previous * current
                "÷" -> if (current != 0.0) previous / current else 0.0
                else -> current
            }
            displayValue = if (result % 1.0 == 0.0) result.toLong().toString() else "%.2f".format(result)
            firstOperand = null
            activeOperator = null
            resetOnNextDigit = true
        }
    }

    fun onPercentage() {
        val current = displayValue.toDoubleOrNull() ?: return
        val percentVal = current / 100.0
        displayValue = if (percentVal % 1.0 == 0.0) percentVal.toLong().toString() else "%.2f".format(percentVal)
        resetOnNextDigit = true
    }

    fun onBackspace() {
        if (displayValue.length > 1 && !resetOnNextDigit) {
            displayValue = displayValue.substring(0, displayValue.length - 1)
        } else {
            displayValue = "0"
        }
    }

    fun onClearAll() {
        displayValue = "0"
        expressionHistory = ""
        firstOperand = null
        activeOperator = null
        resetOnNextDigit = false
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
                            .background(Color(0xFF2E7D32)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Calculate,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = when (currentLanguage) {
                                Language.TELUGU -> "సాధారణ కాలిక్యులేటర్"
                                Language.HINDI -> "साधारण कैलकुलेटर"
                                Language.ENGLISH -> "Calculator"
                            },
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "లెక్కలు / Basic Calculations",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
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
                    .testTag("normal_calculator_dialog"),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Calculator Screen Display
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFF1E272C),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        // Expression History Line
                        Text(
                            text = if (expressionHistory.isNotEmpty()) expressionHistory else " ",
                            fontSize = 13.sp,
                            color = Color(0xFFFFB300),
                            fontWeight = FontWeight.Medium,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        // Main Digits Display
                        Text(
                            text = displayValue,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 1,
                            textAlign = TextAlign.End,
                            modifier = Modifier.testTag("calc_display_value")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Standard Keypad Grid (5 Rows x 4 Columns)
                val keypadRows = listOf(
                    listOf("AC", "⌫", "%", "÷"),
                    listOf("7", "8", "9", "×"),
                    listOf("4", "5", "6", "-"),
                    listOf("1", "2", "3", "+"),
                    listOf("0", "00", ".", "=")
                )

                keypadRows.forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        row.forEach { key ->
                            val isOperator = key in listOf("÷", "×", "-", "+")
                            val isEquals = key == "="
                            val isClear = key == "AC"
                            val isBack = key == "⌫"

                            val buttonColor = when {
                                isEquals -> Color(0xFF2E7D32)     // Forest Green
                                isOperator -> Color(0xFFFF8F00)   // Amber Orange
                                isClear -> Color(0xFFFFEBEE)      // Soft Red
                                isBack -> Color(0xFFFFF3E0)       // Soft Amber
                                else -> Color(0xFFF5F5F5)         // Light Gray for digits
                            }

                            val textColor = when {
                                isEquals -> Color.White
                                isOperator -> Color.White
                                isClear -> Color(0xFFC62828)
                                isBack -> Color(0xFFE65100)
                                else -> Color.Black
                            }

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = buttonColor,
                                shadowElevation = if (isEquals || isOperator) 2.dp else 1.dp,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable {
                                        when (key) {
                                            "AC" -> onClearAll()
                                            "⌫" -> onBackspace()
                                            "%" -> onPercentage()
                                            "÷", "×", "-", "+" -> onOperator(key)
                                            "=" -> onEquals()
                                            "." -> onDecimal()
                                            "00" -> {
                                                if (displayValue != "0" && !resetOnNextDigit) {
                                                    displayValue += "00"
                                                }
                                            }
                                            else -> onDigit(key)
                                        }
                                    }
                                    .testTag("calc_key_$key")
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    if (key == "⌫") {
                                        Icon(
                                            imageVector = Icons.Default.Backspace,
                                            contentDescription = "Backspace",
                                            tint = textColor,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    } else {
                                        Text(
                                            text = key,
                                            fontSize = if (isOperator || isEquals) 20.sp else 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = textColor
                                        )
                                    }
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
                Text(
                    text = when (currentLanguage) {
                        Language.TELUGU -> "ముగించు (Close)"
                        Language.HINDI -> "बंद करें"
                        Language.ENGLISH -> "Close"
                    }
                )
            }
        }
    )
}

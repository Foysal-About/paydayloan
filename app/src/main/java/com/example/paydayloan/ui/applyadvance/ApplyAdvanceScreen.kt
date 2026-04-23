package com.example.paydayloan.ui.applyadvance

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.paydayloan.Employee
import java.util.Locale
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import com.example.paydayloan.ui.components.AppNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplyAdvanceScreen(navController: NavController, employee: Employee) {
    var requestedAmount by remember { mutableStateOf("20000") }
    var expanded by remember { mutableStateOf(false) }
    val purposes = listOf("Short of cash", "Medical emergency", "Family support", "Education")
    var selectedPurpose by remember { mutableStateOf(purposes[0]) }

    val amount = requestedAmount.toDoubleOrNull() ?: 0.0
    val maxEligible = employee.eligibleAmount
    val availableLimit = employee.availableLimit
    
    // Calculation Logic
    val serviceCharge = if (amount > 0) maxOf(amount * 0.02, 200.0) else 0.0
    val netAmount = if (amount > 0) amount - serviceCharge else 0.0

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Apply for Advance",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A237E)
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            AppNavigationBar(navController)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF8F9FA))
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Your Eligibility Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Your Eligibility", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    EligibilityRow("Monthly Salary", employee.salary)
                    EligibilityRow("Maximum Eligible (80%)", maxEligible)
                    EligibilityRow("Available Limit", availableLimit, isHighlight = true)
                }
            }

            // Enter Requested Amount
            Column {
                Text(
                    "Enter Requested Amount",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF1A237E)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = requestedAmount,
                    onValueChange = { requestedAmount = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    leadingIcon = { Text("৳", style = MaterialTheme.typography.titleLarge) },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Minimum ৳ 1,000  •  Maximum ৳ ${String.format(Locale.US, "%,.0f", availableLimit)}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            // Purpose Dropdown
            Column {
                Text(
                    "Purpose (Optional)",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box {
                    OutlinedTextField(
                        value = selectedPurpose,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        trailingIcon = {
                            Icon(
                                Icons.Default.ArrowDropDown,
                                "Dropdown",
                                Modifier.clickable { expanded = true }
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White
                        )
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        purposes.forEach { purpose ->
                            DropdownMenuItem(
                                text = { Text(purpose) },
                                onClick = {
                                    selectedPurpose = purpose
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            // Estimated Charges Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F3F4))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Estimated Charges & Disbursement",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    ChargeRow("Requested Amount", amount)
                    ChargeRow(
                        label = "Service Charge (2% or 200)",
                        amount = serviceCharge,
                        hasInfo = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "You Will Receive",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF00695C)
                        )
                        Text(
                            "৳ ${String.format(Locale.US, "%,.2f", netAmount)}",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF00695C)
                        )
                    }
                }
            }

            // Repayment Info
            Surface(
                color = Color(0xFFF8F9FA),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Outlined.Shield,
                        contentDescription = null,
                        tint = Color(0xFF00695C),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Repayment will be done from your salary on 30 May 2024",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val encodedPurpose = URLEncoder.encode(selectedPurpose, StandardCharsets.UTF_8.toString())
                    navController.navigate("loan_summary/$amount/$serviceCharge/$netAmount/$encodedPurpose")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00695C)),
                shape = RoundedCornerShape(12.dp),
                enabled = amount >= 1000 && amount <= availableLimit
            ) {
                Text("Continue", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun EligibilityRow(label: String, amount: Double, isHighlight: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label,
            color = if (isHighlight) Color(0xFF00695C) else Color.Gray,
            fontSize = 14.sp,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            "৳ ${String.format(Locale.US, "%,.2f", amount)}",
            color = if (isHighlight) Color(0xFF00695C) else Color.Black,
            fontSize = 14.sp,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun ChargeRow(label: String, amount: Double, hasInfo: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(label, color = Color.Gray, fontSize = 14.sp)
            if (hasInfo) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    Icons.Default.Info,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color.Gray
                )
            }
        }
        Text(
            "৳ ${String.format(Locale.US, "%,.2f", amount)}",
            color = Color.Black,
            fontSize = 14.sp
        )
    }
}

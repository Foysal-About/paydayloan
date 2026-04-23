package com.example.paydayloan.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.util.Locale
import com.example.paydayloan.Employee
import com.example.paydayloan.Loan

import com.example.paydayloan.ui.components.AppNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController, employee: Employee) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            "Pay Day Loan",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1A237E)
                            )
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.Info, contentDescription = "Info")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            AppNavigationBar(navController)
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF8F9FA))
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            // Main Eligibility Card
            item {
                EligibilityCard(employee.eligibleAmount)
            }

            // Salary and Limit Info Cards
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    InfoCard(
                        title = "Monthly Salary",
                        amount = employee.salary,
                        icon = Icons.Default.WorkOutline,
                        modifier = Modifier.weight(1f)
                    )
                    InfoCard(
                        title = "Available Limit",
                        amount = employee.availableLimit,
                        icon = Icons.AutoMirrored.Filled.TrendingUp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Active Loan Card
            item {
                ActiveLoanCard(employee.activeLoan)
            }

            // Recent Loans Section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Recent Loans",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    TextButton(onClick = { /* TODO */ }) {
                        Text("View All", color = Color(0xFF00695C))
                    }
                }
            }

            items(employee.lastLoanHistory.take(2)) { loan ->
                RecentLoanItem(loan)
            }

            item {
                Button(
                    onClick = { navController.navigate("apply_advance") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00695C)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Apply for Advance", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun EligibilityCard(amount: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFF00695C), Color(0xFF004D40))
                    )
                )
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Eligible for Advance",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "৳ ${String.format(Locale.US, "%,.2f", amount)}",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "80% of your monthly salary",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp
                    )
                }
                // Placeholder for wallet illustration
                Icon(
                    imageVector = Icons.Default.AccountBalanceWallet,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = Color.White.copy(alpha = 0.3f)
                )
            }
        }
    }
}

@Composable
fun InfoCard(title: String, amount: Double, icon: ImageVector, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, color = Color.Gray, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "৳ ${String.format(Locale.US, "%,.2f", amount)}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFF1A237E)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF00695C),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun ActiveLoanCard(activeLoan: Loan?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Active Loan", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                if (activeLoan == null) {
                    Text("No active loan", color = Color(0xFF00695C), fontWeight = FontWeight.Medium)
                    Text("You have no active payday loan.", color = Color.Gray, fontSize = 12.sp)
                } else {
                    Text("৳ ${activeLoan.amount}", color = Color(0xFF00695C), fontWeight = FontWeight.Bold)
                    Text("Repayment on ${activeLoan.repaymentDate}", color = Color.Gray, fontSize = 12.sp)
                }
            }
            Icon(
                imageVector = Icons.Default.Description,
                contentDescription = null,
                tint = Color(0xFFA5D6A7),
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

@Composable
fun RecentLoanItem(loan: Loan) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(loan.requestDate, color = Color.Gray, fontSize = 12.sp)
                Surface(
                    color = Color(0xFFE8F5E9),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        "Repaid",
                        color = Color(0xFF2E7D32),
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "৳ ${String.format(Locale.US, "%,.2f", loan.amount)}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            Text("Repaid on ${loan.repaymentDate}", color = Color.Gray, fontSize = 12.sp)
        }
    }
    HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 1.dp)
}

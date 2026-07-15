package com.example.paydayloan

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpCenter
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.paydayloan.ui.applyadvance.ApplyAdvanceScreen
import com.example.paydayloan.ui.dashboard.DashboardScreen
import com.example.paydayloan.ui.applyadvance.LoanStatusScreen
import com.example.paydayloan.ui.applyadvance.LoanSummaryScreen
import com.example.paydayloan.ui.auth.LoginScreen
import com.example.paydayloan.ui.history.HistoryScreen
import com.example.paydayloan.ui.notification.NotificationScreen
import com.example.paydayloan.ui.support.SupportScreen
import com.example.paydayloan.ui.settings.SettingsScreen
import com.example.paydayloan.ui.components.AppNavigationBar
import com.example.paydayloan.ui.theme.*
import kotlinx.coroutines.launch

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            paydayloanTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()

                // Define screens where the navigation bar should be visible
                val showNavBar = currentRoute in listOf(
                    "dashboard", 
                    "history", 
                    "support", 
                    "settings"
                )

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    gesturesEnabled = showNavBar, // Only enable gestures when nav bar is shown
                    drawerContent = {
                        ModalDrawerSheet(
                            drawerContainerColor = Color.White,
                            drawerShape = RoundedCornerShape(topEnd = 32.dp, bottomEnd = 32.dp),
                            modifier = Modifier.width(320.dp),
                            windowInsets = WindowInsets(0) // Full height drawer
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 24.dp, vertical = 48.dp)
                            ) {
                                // Profile Section
                                Box(
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(CircleShape)
                                        .background(CityMaroon.copy(alpha = 0.08f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.Person,
                                        contentDescription = null,
                                        tint = CityMaroon,
                                        modifier = Modifier.size(44.dp)
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(24.dp))
                                
                                Text(
                                    "Syed Foysal",
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = CityTextDark,
                                        letterSpacing = (-0.5).sp
                                    )
                                )
                                Text(
                                    "Software Engineer",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = CityTextGray
                                )

                                Spacer(modifier = Modifier.height(32.dp))
                                HorizontalDivider(color = CityBackground, thickness = 1.dp)
                                Spacer(modifier = Modifier.height(24.dp))

                                // Navigation Items
                                GlobalDrawerItem(Icons.Default.AccountBalance, "Bank Accounts") { scope.launch { drawerState.close() } }
                                Spacer(modifier = Modifier.height(8.dp))
                                GlobalDrawerItem(Icons.Default.Description, "Loan Statements") { scope.launch { drawerState.close() } }
                                Spacer(modifier = Modifier.height(8.dp))
                                GlobalDrawerItem(Icons.Default.VerifiedUser, "Privacy & Security") { scope.launch { drawerState.close() } }
                                Spacer(modifier = Modifier.height(8.dp))
                                GlobalDrawerItem(Icons.AutoMirrored.Filled.HelpCenter, "FAQs") { scope.launch { drawerState.close() } }

                                Spacer(modifier = Modifier.weight(1f))

                                // Premium Logout Button matching the image
                                Surface(
                                    onClick = {
                                        scope.launch { drawerState.close() }
                                        navController.navigate("login") {
                                            popUpTo("dashboard") { inclusive = true }
                                        }
                                    },
                                    color = CityMaroon.copy(alpha = 0.08f),
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .padding(vertical = 16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            Icons.AutoMirrored.Filled.Logout, 
                                            contentDescription = null, 
                                            tint = CityMaroon,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(
                                            "Logout", 
                                            color = CityMaroon, 
                                            fontWeight = FontWeight.Bold, 
                                            fontSize = 16.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        NavHost(
                            navController = navController, 
                            startDestination = "login",
                            modifier = Modifier.fillMaxSize()
                        ) {
                            composable("login") {
                                LoginScreen(navController)
                            }
                            composable("dashboard") {
                                DashboardScreen(navController, onOpenDrawer = { scope.launch { drawerState.open() } })
                            }
                            composable("apply_advance") {
                                ApplyAdvanceScreen(navController)
                            }
                            composable("loan_summary/{amount}/{serviceCharge}/{netAmount}/{purpose}/{salary}/{eligibility}") { backStackEntry ->
                                val amount = backStackEntry.arguments?.getString("amount")?.toDoubleOrNull() ?: 0.0
                                val serviceCharge = backStackEntry.arguments?.getString("serviceCharge")?.toDoubleOrNull() ?: 0.0
                                val netAmount = backStackEntry.arguments?.getString("netAmount")?.toDoubleOrNull() ?: 0.0
                                val purpose = backStackEntry.arguments?.getString("purpose") ?: ""
                                val salary = backStackEntry.arguments?.getString("salary")?.toDoubleOrNull() ?: 0.0
                                val eligibility = backStackEntry.arguments?.getString("eligibility")?.toDoubleOrNull() ?: 0.0
                                LoanSummaryScreen(navController, amount, serviceCharge, netAmount, purpose, salary, eligibility)
                            }
                            composable("loan_status") {
                                LoanStatusScreen(navController)
                            }
                            composable("history") {
                                HistoryScreen(navController)
                            }
                            composable("support") {
                                SupportScreen(navController)
                            }
                            composable("settings") {
                                SettingsScreen(navController)
                            }
                            composable("notifications") {
                                NotificationScreen(navController)
                            }
                        }

                        // Persistent Navigation Bar that stays across screen transitions
                        if (showNavBar) {
                            AppNavigationBar(
                                navController = navController,
                                modifier = Modifier.align(Alignment.BottomCenter)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GlobalDrawerItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        color = Color.Transparent,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon, 
                contentDescription = null, 
                tint = CityTextDark.copy(alpha = 0.7f), 
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                label,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = CityTextDark,
                    fontSize = 16.sp
                )
            )
        }
    }
}

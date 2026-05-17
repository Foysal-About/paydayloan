package com.example.paydayloan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.paydayloan.ui.support.SupportScreen
import com.example.paydayloan.ui.settings.SettingsScreen
import com.example.paydayloan.ui.components.AppNavigationBar
import com.example.paydayloan.ui.theme.paydayloanTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            paydayloanTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                // Define screens where the navigation bar should be visible
                val showNavBar = currentRoute in listOf(
                    "dashboard", 
                    "history", 
                    "support", 
                    "settings", 
                    "apply_advance", 
                    "loan_status"
                )

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
                            DashboardScreen(navController)
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

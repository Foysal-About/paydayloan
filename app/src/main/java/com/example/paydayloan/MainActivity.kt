package com.example.paydayloan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.paydayloan.ui.applyadvance.ApplyAdvanceScreen
import com.example.paydayloan.ui.dashboard.DashboardScreen
import com.example.paydayloan.ui.applyadvance.LoanStatusScreen
import com.example.paydayloan.ui.applyadvance.LoanSummaryScreen
import com.example.paydayloan.ui.theme.paydayloanTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            paydayloanTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "dashboard") {
                    composable("dashboard") {
                        DashboardScreen(navController, dummyEmployee)
                    }
                    composable("apply_advance") {
                        ApplyAdvanceScreen(navController, dummyEmployee)
                    }
                    composable("loan_summary/{amount}/{serviceCharge}/{netAmount}/{purpose}") { backStackEntry ->
                        val amount = backStackEntry.arguments?.getString("amount")?.toDoubleOrNull() ?: 0.0
                        val serviceCharge = backStackEntry.arguments?.getString("serviceCharge")?.toDoubleOrNull() ?: 0.0
                        val netAmount = backStackEntry.arguments?.getString("netAmount")?.toDoubleOrNull() ?: 0.0
                        val purpose = backStackEntry.arguments?.getString("purpose") ?: ""
                        LoanSummaryScreen(navController, amount, serviceCharge, netAmount, purpose)
                    }
                    composable("loan_status") {
                        LoanStatusScreen(navController)
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    paydayloanTheme {
        Greeting("Android")
    }
}
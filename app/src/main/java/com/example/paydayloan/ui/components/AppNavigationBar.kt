package com.example.paydayloan.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun AppNavigationBar(navController: NavController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("Home") },
            selected = currentRoute == "dashboard",
            onClick = {
                if (currentRoute != "dashboard") {
                    navController.navigate("dashboard") {
                        popUpTo("dashboard") { inclusive = true }
                    }
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF00695C),
                selectedTextColor = Color(0xFF00695C),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = null) },
            label = { Text("Accounts") },
            selected = false,
            onClick = {},
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            icon = { 
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(
                            if (currentRoute == "apply_advance" || currentRoute?.startsWith("loan") == true) 
                                Color(0xFF00695C) else Color(0xFFE0E0E0), 
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Payments, 
                        contentDescription = null, 
                        tint = if (currentRoute == "apply_advance" || currentRoute?.startsWith("loan") == true) 
                            Color.White else Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            label = { 
                Text(
                    "Pay Day Loan", 
                    color = if (currentRoute == "apply_advance" || currentRoute?.startsWith("loan") == true) 
                        Color(0xFF00695C) else Color.Gray
                ) 
            },
            selected = currentRoute == "apply_advance" || currentRoute?.startsWith("loan") == true,
            onClick = {
                if (currentRoute != "apply_advance") {
                    navController.navigate("apply_advance")
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Transparent,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.SyncAlt, contentDescription = null) },
            label = { Text("Transfers") },
            selected = false,
            onClick = {},
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.MoreHoriz, contentDescription = null) },
            label = { Text("More") },
            selected = false,
            onClick = {},
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )
    }
}

package com.example.paydayloan.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.HeadsetMic
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

import com.example.paydayloan.ui.theme.CityMaroon
import com.example.paydayloan.ui.theme.CityTextGray
import com.example.paydayloan.ui.theme.CityTextDark

@Composable
fun AppNavigationBar(navController: NavController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 12.dp,
        modifier = Modifier.background(Color.White)
    ) {
        // Home
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Home, contentDescription = null, modifier = Modifier.size(26.dp)) },
            label = { Text("Home", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
            selected = currentRoute == "dashboard",
            onClick = {
                if (currentRoute != "dashboard") {
                    navController.navigate("dashboard") {
                        popUpTo("dashboard") { inclusive = true }
                    }
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CityMaroon,
                selectedTextColor = CityMaroon,
                unselectedIconColor = CityTextGray,
                unselectedTextColor = CityTextGray,
                indicatorColor = Color.Transparent
            )
        )

        // History
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Schedule, contentDescription = null, modifier = Modifier.size(26.dp)) },
            label = { Text("History", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
            selected = false,
            onClick = { /* TODO */ },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = CityTextGray,
                unselectedTextColor = CityTextGray,
                indicatorColor = Color.Transparent
            )
        )

        // Pay Day Loan (Center Item)
        NavigationBarItem(
            icon = {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .background(CityMaroon, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.Payments,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            },
            label = {
                Text(
                    "Apply",
                    color = CityMaroon,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp)
                )
            },
            selected = currentRoute == "apply_advance" || currentRoute?.startsWith("loan") == true,
            onClick = {
                if (currentRoute != "apply_advance") {
                    navController.navigate("apply_advance")
                }
            },
            alwaysShowLabel = true,
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Transparent,
                indicatorColor = Color.Transparent
            )
        )

        // Support
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.HeadsetMic, contentDescription = null, modifier = Modifier.size(26.dp)) },
            label = { Text("Support", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
            selected = false,
            onClick = { /* TODO */ },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = CityTextGray,
                unselectedTextColor = CityTextGray,
                indicatorColor = Color.Transparent
            )
        )

        // Settings
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Settings, contentDescription = null, modifier = Modifier.size(26.dp)) },
            label = { Text("Settings", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
            selected = false,
            onClick = { /* TODO */ },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = CityTextGray,
                unselectedTextColor = CityTextGray,
                indicatorColor = Color.Transparent
            )
        )
    }
}

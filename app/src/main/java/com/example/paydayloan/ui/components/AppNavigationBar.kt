package com.example.paydayloan.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HeadsetMic
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.paydayloan.R
import com.example.paydayloan.ui.theme.CityMaroon
import com.example.paydayloan.ui.theme.CityTextGray

@Composable
fun AppNavigationBar(navController: NavController, modifier: Modifier = Modifier) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(75.dp),
        color = Color.White,
        shadowElevation = 20.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.Top // Align to top to position indicators correctly
        ) {
            // Home
            CustomNavItem(
                label = "Home",
                iconRes = R.drawable.home,
                isSelected = currentRoute == "dashboard",
                onClick = {
                    if (currentRoute != "dashboard") {
                        navController.navigate("dashboard") {
                            popUpTo("dashboard") { inclusive = true }
                        }
                    }
                }
            )

            // History
            CustomNavItem(
                label = "History",
                iconRes = R.drawable.history,
                isSelected = currentRoute == "history",
                onClick = {
                    if (currentRoute != "history") {
                        navController.navigate("history") {
                            popUpTo("dashboard") { inclusive = false }
                        }
                    }
                }
            )

            // Support
            CustomNavItem(
                label = "Support",
                iconVector = Icons.Default.HeadsetMic,
                isSelected = currentRoute == "support",
                onClick = {
                    if (currentRoute != "support") {
                        navController.navigate("support") {
                            popUpTo("dashboard") { inclusive = false }
                        }
                    }
                }
            )

            // Settings
            CustomNavItem(
                label = "Settings",
                iconRes = R.drawable.setting,
                isSelected = currentRoute == "settings",
                onClick = {
                    if (currentRoute != "settings") {
                        navController.navigate("settings") {
                            popUpTo("dashboard") { inclusive = false }
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun CustomNavItem(
    label: String,
    iconVector: ImageVector? = null,
    iconRes: Int? = null,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val contentColor = if (isSelected) CityMaroon else CityTextGray

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .width(80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Indicator Bar matching the reference image
        Box(
            modifier = Modifier
                .width(45.dp)
                .height(3.dp)
                .background(
                    if (isSelected) CityMaroon else Color.Transparent,
                    RoundedCornerShape(bottomStart = 4.dp, bottomEnd = 4.dp)
                )
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        if (iconVector != null) {
            Icon(
                imageVector = iconVector,
                contentDescription = label,
                tint = contentColor,
                modifier = Modifier.size(24.dp)
            )
        } else if (iconRes != null) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                tint = contentColor,
                modifier = Modifier.size(24.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = label,
            color = contentColor,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
        
        Spacer(modifier = Modifier.height(12.dp))
    }
}

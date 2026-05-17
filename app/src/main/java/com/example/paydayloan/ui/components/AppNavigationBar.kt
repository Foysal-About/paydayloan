package com.example.paydayloan.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.HeadsetMic
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
import com.example.paydayloan.ui.theme.CityTextDark
import com.example.paydayloan.ui.theme.CityTextGray

@Composable
fun AppNavigationBar(navController: NavController, modifier: Modifier = Modifier) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    // Container for the glass-effect floating capsule
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent) // Explicitly transparent
            .padding(start = 24.dp, end = 24.dp, bottom = 32.dp, top = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            shape = RoundedCornerShape(35.dp),
            // Glass effect: Semi-transparent white with a very subtle tint
            color = Color.White.copy(alpha = 0.85f),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.5f)),
            tonalElevation = 0.dp,
            shadowElevation = 12.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
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
                    iconVector = Icons.Outlined.Schedule,
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
                    iconVector = Icons.Outlined.HeadsetMic,
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
                    iconVector = Icons.Outlined.Settings,
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
}

@Composable
fun CustomNavItem(
    label: String,
    iconVector: ImageVector? = null,
    iconRes: Int? = null,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    // Animate colors for the premium pill effect
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) CityMaroon else Color.Transparent,
        animationSpec = tween(durationMillis = 400), label = "bg"
    )
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else CityTextGray,
        animationSpec = tween(durationMillis = 400), label = "content"
    )

    Box(
        modifier = Modifier
            .height(48.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(backgroundColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = if (isSelected) 18.dp else 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.animateContentSize(animationSpec = tween(durationMillis = 50)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (iconVector != null) {
                Icon(
                    imageVector = iconVector,
                    contentDescription = label,
                    tint = contentColor,
                    modifier = Modifier.size(22.dp)
                )
            } else if (iconRes != null) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = label,
                    tint = contentColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            AnimatedVisibility(visible = isSelected) {
                Row {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = label,
                        color = contentColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

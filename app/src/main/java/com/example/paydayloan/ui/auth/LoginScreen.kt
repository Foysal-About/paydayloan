package com.example.paydayloan.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.paydayloan.ui.theme.*

@Composable
fun LoginScreen(navController: NavController) {
    var userId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var rememberUserId by remember { mutableStateOf(true) }
    var selectedTab by remember { mutableIntStateOf(0) } // 0 for Log In, 1 for Self Registration

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBlue)
    ) {
        // Top Section: Logo and Bank Name
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Placeholder for Logo
            Icon(
                imageVector = Icons.Default.Lock, // Just a placeholder for the geometric logo
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Leads PAYDAY",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )
            Text(
                text = "YOUR SALARY, ANYTIME",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 10.sp,
                letterSpacing = 2.sp
            )
        }

        // Bottom Section: Login Form
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                // Tab Switcher
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(TabBackground, RoundedCornerShape(12.dp))
                        .padding(4.dp)
                ) {
                    TabItem(
                        text = "Log In",
                        isSelected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        modifier = Modifier.weight(1f)
                    )
                    TabItem(
                        text = "Self Registration",
                        isSelected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // User ID Field
                Text("User ID", color = Color.Gray, fontSize = 14.sp)
                OutlinedTextField(
                    value = userId,
                    onValueChange = { userId = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Enter your username", color = Color.LightGray) },
                    shape = RoundedCornerShape(12.dp),
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = null, tint = Color.Gray)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.LightGray,
                        unfocusedBorderColor = Color.LightGray
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Password Field
                Text("Password", color = Color.Gray, fontSize = 14.sp)
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Enter your password", color = Color.LightGray) },
                    shape = RoundedCornerShape(12.dp),
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = Color.Gray)
                    },
                    trailingIcon = {
                        Text(
                            text = if (isPasswordVisible) "Hide" else "Show",
                            color = PrimaryBlue,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .clickable { isPasswordVisible = !isPasswordVisible }
                        )
                    },
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.LightGray,
                        unfocusedBorderColor = Color.LightGray
                    )
                )

                Text(
                    "FORGOT Password?",
                    color = PrimaryBlue,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .clickable { /* Handle forgot password */ }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Remember User ID Checkbox
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = rememberUserId,
                        onCheckedChange = { rememberUserId = it },
                        colors = CheckboxDefaults.colors(checkedColor = Color.DarkGray)
                    )
                    Text(
                        text = buildAnnotatedString {
                            append("Remember ")
                            withStyle(style = SpanStyle(color = PrimaryBlue, fontWeight = FontWeight.Bold)) {
                                append("User ID")
                            }
                        },
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Biometric Login Button
                    OutlinedButton(
                        onClick = { /* Handle biometric login */ },
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = borderStroke(),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(Icons.Default.Fingerprint, contentDescription = null, tint = Color.Black)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Biometric Login", color = PrimaryBlue, fontSize = 13.sp, maxLines = 1)
                        }
                    }

                    // Login Button
                    Button(
                        onClick = { navController.navigate("dashboard") },
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                    ) {
                        Text("Login", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun borderStroke() = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray)

@Composable
fun TabItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxHeight()
            .clickable { onClick() },
        color = if (isSelected) Color.White else Color.Transparent,
        shape = RoundedCornerShape(8.dp),
        shadowElevation = if (isSelected) 2.dp else 0.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                color = if (isSelected) PrimaryBlue else Color.Gray,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                fontSize = 14.sp
            )
        }
    }
}

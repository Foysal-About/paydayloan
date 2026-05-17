package com.example.paydayloan.ui.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.paydayloan.ui.theme.*

@Composable
fun LoginScreen(navController: NavController) {
    var employeeId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = CityBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Premium Header Section with City Bank Branding
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Welcome to",
                        color = CityTextGray,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "City PayDay",
                        color = CityMaroon,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-1).sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Access your salary advance instantly with the trust of City Bank PLC.",
                        color = CityTextGray,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }

                // Premium Illustration Placeholder
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .padding(start = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(CityMaroon, CityMaroonDark)
                                ),
                                shape = RoundedCornerShape(20.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountBalanceWallet,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    Surface(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(32.dp),
                        shape = CircleShape,
                        color = Color.White,
                        shadowElevation = 4.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                tint = CityGold,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Login Card with improved Material 3 styling
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.1f))
            ) {
                Column(
                    modifier = Modifier.padding(28.dp)
                ) {
                    Text(
                        text = "Login to Your Account",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = CityTextDark
                    )
                    Text(
                        text = "Enter your official credentials to proceed",
                        fontSize = 14.sp,
                        color = CityTextGray
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Employee ID Field
                    Text(
                        "Employee ID",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = CityTextDark,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = employeeId,
                        onValueChange = { employeeId = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("ID: 1002345", color = Color.LightGray) },
                        shape = RoundedCornerShape(16.dp),
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = null, tint = CityMaroon.copy(alpha = 0.6f))
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = CityBackground,
                            focusedBorderColor = CityMaroon,
                            unfocusedContainerColor = CityBackground,
                            focusedContainerColor = CityBackground
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Password Field
                    Text(
                        "Password",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = CityTextDark,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("••••••••", color = Color.LightGray) },
                        shape = RoundedCornerShape(16.dp),
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = CityMaroon.copy(alpha = 0.6f))
                        },
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(
                                    imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = null,
                                    tint = CityTextGray
                                )
                            }
                        },
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = CityBackground,
                            focusedBorderColor = CityMaroon,
                            unfocusedContainerColor = CityBackground,
                            focusedContainerColor = CityBackground
                        )
                    )

                    Text(
                        text = "Forgot Password?",
                        color = CityMaroon,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                            .clickable { /* Handle forgot password */ }
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Premium Actions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = { navController.navigate("dashboard") },
                            modifier = Modifier
                                .weight(1f)
                                .height(60.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = CityMaroon),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Outlined.Shield, contentDescription = null, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Text("Sign In", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        OutlinedButton(
                            onClick = { /* Biometric */ },
                            modifier = Modifier.size(60.dp),
                            shape = RoundedCornerShape(16.dp),
                            contentPadding = PaddingValues(0.dp),
                            border = BorderStroke(1.dp, CityMaroon.copy(alpha = 0.2f))
                        ) {
                            Icon(Icons.Default.Fingerprint, contentDescription = null, tint = CityMaroon, modifier = Modifier.size(32.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Bank Security Badge
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = CitySuccess.copy(alpha = 0.05f),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, CitySuccess.copy(alpha = 0.1f))
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(CitySuccess, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Outlined.Shield, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            "Secured by City Bank PLC",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = CityTextDark
                        )
                        Text(
                            "Bank-grade encryption for your peace of mind.",
                            fontSize = 12.sp,
                            color = CityTextGray
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

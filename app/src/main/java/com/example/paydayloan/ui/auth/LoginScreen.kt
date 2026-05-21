package com.example.paydayloan.ui.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.paydayloan.R
import com.example.paydayloan.ui.theme.*

@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    var employeeId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    val executor = remember { ContextCompat.getMainExecutor(context) }
    val biometricPrompt = remember {
        BiometricPrompt(
            context as FragmentActivity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    // Handle error
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    navController.navigate("dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            }
        )
    }

    val promptInfo = remember {
        BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Login")
            .setSubtitle("Log in using your biometric credential")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
            .build()
    }

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

            // Standard Header Section with City Bank Logo
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Welcome to",
                        color = CityTextGray,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "City PayDay",
                        color = CityMaroon,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Access your salary advance instantly with the trust of City Bank PLC.",
                        color = CityTextGray,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }

                // Official City Bank Logo from city_logo.png
                Box(
                    modifier = Modifier
                        .padding(top = 4.dp, start = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.city_logo),
                        contentDescription = "City Bank Logo",
                        modifier = Modifier
                            .size(80.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Standard Login Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(32.dp),
                color = Color.White,
                shadowElevation = 2.dp
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
                    TextField(
                        value = employeeId,
                        onValueChange = { employeeId = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("ID: 1002345", color = Color.LightGray) },
                        shape = RoundedCornerShape(16.dp),
                        leadingIcon = {
                            Icon(
                                Icons.Default.Person, 
                                contentDescription = null, 
                                tint = CityMaroon.copy(alpha = 0.5f),
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFF5F6F8),
                            unfocusedContainerColor = Color(0xFFF5F6F8),
                            disabledContainerColor = Color(0xFFF5F6F8),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Password Field
                    Text(
                        "Password",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = CityTextDark,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("••••••••", color = Color.LightGray) },
                        shape = RoundedCornerShape(16.dp),
                        leadingIcon = {
                            Icon(
                                Icons.Default.Lock, 
                                contentDescription = null, 
                                tint = CityMaroon.copy(alpha = 0.5f),
                                modifier = Modifier.size(20.dp)
                            )
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
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFF5F6F8),
                            unfocusedContainerColor = Color(0xFFF5F6F8),
                            disabledContainerColor = Color(0xFFF5F6F8),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                        Text(
                            text = "Forgot Password?",
                            color = CityMaroon,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { /* Handle forgot password */ }
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Actions Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = { navController.navigate("dashboard") },
                            modifier = Modifier
                                .weight(1f)
                                .height(64.dp)
                                .shadow(8.dp, RoundedCornerShape(16.dp), spotColor = CityMaroon),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = CityMaroon)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Outlined.Shield, contentDescription = null, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Text("Sign In", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Surface(
                            onClick = { 
                                try {
                                    biometricPrompt.authenticate(promptInfo)
                                } catch (e: Exception) {
                                    // Fallback or error handling
                                }
                            },
                            modifier = Modifier.size(64.dp),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.4f)),
                            color = Color.White
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.Fingerprint, 
                                    contentDescription = null, 
                                    tint = CityMaroon, 
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Bank Security Badge
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFEDF2F1),
                shape = RoundedCornerShape(24.dp)
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
                        Icon(
                            Icons.Outlined.Shield, 
                            contentDescription = null, 
                            tint = Color.White, 
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            "Secured by City Bank PLC",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
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

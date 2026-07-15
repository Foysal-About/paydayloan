package com.example.paydayloan.ui.auth

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
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

    Box(modifier = Modifier.fillMaxSize().background(CityBackground)) {
        // Background - Soft Animated Liquid Effect for Glassmorphism
        Box(modifier = Modifier.fillMaxSize()) {
            val transition = rememberInfiniteTransition(label = "liquid")
            val animX by transition.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(15000, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ), label = "x"
            )
            val animY by transition.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(20000, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ), label = "y"
            )

            Canvas(modifier = Modifier.fillMaxSize().blur(80.dp)) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(CityMaroon.copy(alpha = 0.08f), Color.Transparent),
                    ),
                    center = Offset(size.width * (0.1f + 0.3f * animX), size.height * (0.2f + 0.4f * animY)),
                    radius = size.width * 1.5f
                )
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFF0EA5E9).copy(alpha = 0.1f), Color.Transparent),
                    ),
                    center = Offset(size.width * (0.9f - 0.4f * animY), size.height * (0.8f - 0.3f * animX)),
                    radius = size.width * 1.2f
                )
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(CityMaroon.copy(alpha = 0.05f), Color.Transparent),
                    ),
                    center = Offset(size.width * (0.5f + 0.2f * animY), size.height * (0.4f + 0.2f * animX)),
                    radius = size.width * 1.0f
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Navigation / Language
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = Color.White.copy(alpha = 0.4f),
                    border = BorderStroke(
                        width = 1.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.6f),
                                Color.White.copy(alpha = 0.1f)
                            )
                        )
                    ),
                    modifier = Modifier.clickable { }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Language, contentDescription = null, tint = CityTextDark, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("ENGLISH", color = CityTextDark, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(60.dp))

            // Brand Section
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.city_logo),
                    contentDescription = "City Bank Logo",
                    modifier = Modifier.size(100.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    Text("CITY ", color = CityTextDark, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
                    Text("PAYDAY", color = CityMaroon, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Login Inputs - Clean Light Glass Effect
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Employee ID Input
                TextField(
                    value = employeeId,
                    onValueChange = { employeeId = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            Brush.linearGradient(
                                listOf(Color.White.copy(alpha = 0.6f), Color.White.copy(alpha = 0.2f))
                            ),
                            RoundedCornerShape(22.dp)
                        ),
                    placeholder = { Text("Enter your ID", color = CityTextGray.copy(alpha = 0.7f)) },
                    leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null, tint = CityMaroon.copy(alpha = 0.7f)) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.9f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.6f),
                        focusedTextColor = CityTextDark,
                        unfocusedTextColor = CityTextDark,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = CityMaroon
                    ),
                    shape = RoundedCornerShape(22.dp),
                    singleLine = true
                )

                // Password Input
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            Brush.linearGradient(
                                listOf(Color.White.copy(alpha = 0.6f), Color.White.copy(alpha = 0.2f))
                            ),
                            RoundedCornerShape(22.dp)
                        ),
                    placeholder = { Text("Enter password", color = CityTextGray.copy(alpha = 0.7f)) },
                    leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null, tint = CityMaroon.copy(alpha = 0.7f)) },
                    trailingIcon = {
                        Text(
                            text = if (isPasswordVisible) "Hide" else "Show",
                            color = CityMaroon,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .clickable { isPasswordVisible = !isPasswordVisible }
                        )
                    },
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.9f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.6f),
                        focusedTextColor = CityTextDark,
                        unfocusedTextColor = CityTextDark,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = CityMaroon
                    ),
                    shape = RoundedCornerShape(22.dp),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                Text(
                    "Forgot Password/ID?",
                    color = CityMaroon,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { }
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            // Action Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { navController.navigate("dashboard") },
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CityMaroon),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Text("Login", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                }

                Surface(
                    onClick = { 
                        try { biometricPrompt.authenticate(promptInfo) } catch (_: Exception) {}
                    },
                    modifier = Modifier.size(60.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White.copy(alpha = 0.6f),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f)),
                    shadowElevation = 0.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Fingerprint,
                            contentDescription = "Biometric", 
                            tint = CityMaroon,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

        }
    }
}

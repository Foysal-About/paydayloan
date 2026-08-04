package com.example.paydayloan.ui.auth

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.paydayloan.R
import com.example.paydayloan.ui.components.UnderlineTextField
import com.example.paydayloan.ui.theme.CityMaroon
import com.example.paydayloan.ui.theme.appColors

@Composable
fun LoginScreen(navController: NavController) {
    // ... (rest of the preamble)
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isBangla by remember { mutableStateOf(false) }
    var loginError by remember { mutableStateOf<String?>(null) }

    // Hardcoded demo credentials.
    fun attemptLogin() {
        if (email.trim() == "admin1" && password == "1") {
            loginError = null
            navController.navigate("dashboard") {
                popUpTo("login") { inclusive = true }
            }
        } else {
            loginError = "Invalid Employee ID or password"
        }
    }

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

    // Theme-aware canvas: light mode gets a soft warm-white with a faint maroon
    // wash at the top; dark mode keeps the premium near-black with a maroon glow.
    val c = appColors
    val brandGradient = if (c.isDark) {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF3A0A0D), // muted maroon glow at top
                Color(0xFF1A0708), // deep maroon-black
                Color(0xFF0D0D0D)  // near-black base
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFFFBEEEC), // faint maroon-tinted white at top
                Color(0xFFFDF8F7), // soft warm white
                Color(0xFFF7F7F7)  // clean neutral base
            )
        )
    }

    Box(modifier = Modifier.fillMaxSize().background(brandGradient)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.city_logo),
                    contentDescription = "City Bank Logo",
                    modifier = Modifier.size(60.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(60.dp))

            // Large Sign in header
            Text(
                text = "Sign in",
                color = c.textPrimary,
                fontSize = 38.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(50.dp))

            // Underline Text Fields
            UnderlineTextField(
                value = email,
                onValueChange = { email = it; loginError = null },
                label = "Employee ID"
            )

            Spacer(modifier = Modifier.height(30.dp))

            UnderlineTextField(
                value = password,
                onValueChange = { password = it; loginError = null },
                label = "Password",
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = null,
                            tint = c.textSecondary
                        )
                    }
                }
            )

            loginError?.let { error ->
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(60.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Primary Sign in button (takes remaining width)
                OutlinedButton(
                    onClick = { attemptLogin() },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = CityMaroon
                    ),
                    border = BorderStroke(1.dp, CityMaroon),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Sign in",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Icon-only Face ID button beside it
                Button(
                    onClick = { biometricPrompt.authenticate(promptInfo) },
                    modifier = Modifier.size(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CityMaroon,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.face_id),
                        contentDescription = "Sign in with Face ID",
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Forgot User ID/ Password?",
                color = CityMaroon,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { /* TODO: forgot credentials flow */ }
            )

            Spacer(modifier = Modifier.weight(1f))
        }

        // Transparent language toggle pill, top-right
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(top = 20.dp, end = 20.dp)
                .clip(RoundedCornerShape(50))
                .border(
                    BorderStroke(1.dp, c.textPrimary.copy(alpha = 0.25f)),
                    RoundedCornerShape(50)
                )
                .clickable { isBangla = !isBangla }
                .animateContentSize(
                    animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing)
                )
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Language,
                contentDescription = "Change language",
                tint = c.textPrimary,
                modifier = Modifier.size(18.dp)
            )
            AnimatedContent(
                targetState = isBangla,
                transitionSpec = {
                    (fadeIn(tween(300, easing = FastOutSlowInEasing)))
                        .togetherWith(fadeOut(tween(200, easing = FastOutSlowInEasing)))
                },
                label = "languageLabel"
            ) { bangla ->
                Text(
                    text = if (bangla) "বাংলা" else "ENGLISH",
                    color = c.textPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

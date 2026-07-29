package com.example.paydayloan.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp

import com.example.paydayloan.ui.theme.CityMaroon
import com.example.paydayloan.ui.theme.appColors

@Composable
fun UnderlineTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    // Theme-aware: text/labels follow the palette, the brand maroon marks focus.
    val c = appColors
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        label = {
            Text(
                text = label,
                color = c.textSecondary,
                fontSize = 14.sp
            )
        },
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        keyboardOptions = keyboardOptions,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            cursorColor = CityMaroon,
            focusedIndicatorColor = CityMaroon,
            unfocusedIndicatorColor = c.textSecondary.copy(alpha = 0.5f),
            focusedTextColor = c.textPrimary,
            unfocusedTextColor = c.textPrimary,
            focusedLabelColor = CityMaroon,
            unfocusedLabelColor = c.textSecondary
        ),
        textStyle = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        ),
        singleLine = true
    )
}

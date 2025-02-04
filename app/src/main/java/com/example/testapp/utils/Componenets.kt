package com.example.testapp.utils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    startIcon: Painter? = null,
    endIcon: Painter? = null,
    inputType: InputType = InputType.AlphaNumeric,
    isValid: (String) -> Boolean = { true },
    errorMessage: String = ""
) {
    var text by remember { mutableStateOf(value) }
    var isFocused by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }

    val border = if (isFocused) {
        BorderStroke(1.dp, colorResource(R.color.themeColor))
    } else if (isError) {
        BorderStroke(1.dp, Color.Red)
    } else {
        BorderStroke(1.dp, Color.White)
    }
Spacer(modifier = Modifier.height(10.dp))
    TextField(
        value = text,
        onValueChange = { newText ->
            text = newText
            onValueChange(newText)
            isError = !isValid(text)
        },
        label = {
            Text(label, textAlign = TextAlign.Start)
        },
        textStyle = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Light
        ),
        modifier = modifier
            .background(Color.White, RoundedCornerShape(25.dp))
            .fillMaxWidth()
            .height(60.dp)
            .onFocusChanged {
                isFocused = it.isFocused
            },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedContainerColor = colorResource(R.color.grayColor),
            focusedContainerColor = colorResource(R.color.grayColor),
            focusedTextColor = Color.Black,
        ),
        leadingIcon = {
            if (startIcon != null) {
                Icon(
                    painter = startIcon, contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(14.dp)
                )
            }
        },
        trailingIcon = {
            if (endIcon != null) {
                Icon(
                    painter = endIcon, contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(14.dp)
                )
            }
            if (isError) {
                Icon(
                    painter = painterResource(R.drawable.circle),
                    contentDescription = null,
                    tint = Color.Red,
                    modifier = Modifier.size(16.dp)
                )
            }
        },
        visualTransformation = when (inputType) {
            InputType.Password -> PasswordVisualTransformation()
            else -> VisualTransformation.None
        },
    )
    /*    if (isError) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
        }*/
}

@Composable
fun AppButton(
    label: String,
    onClickAction: () -> Unit,
    containerColor: Color = colorResource(R.color.themeColor),
    contentColor: Color = Color.White,
    modifier: Modifier
) {
    Button(
        onClick = onClickAction,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(label, fontSize = 14.sp)
    }
}

@Composable
fun MyButton(
    label: String,
    iconId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black // Set content color to black
        ),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.DarkGray), // Add dark gray border
    ) {
        Row(
            modifier = Modifier.padding(8.dp), // Add padding around the content
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center // Center content horizontally
        ) {
            Image(
                painter = painterResource(id = iconId), // Use the provided icon ID
                contentDescription = null, // Provide a content description if needed
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp)) // Add space between icon and text
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold // Make text bold
            )
        }
    }
}
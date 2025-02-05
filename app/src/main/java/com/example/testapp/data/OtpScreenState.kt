package com.example.testapp.data

import androidx.compose.runtime.Composable

data class OtpScreenState(
    val code: List<Int?> = (1..4).map { null },
    val focusIndex: Int? = null,
    val isValid: Boolean? = null
)
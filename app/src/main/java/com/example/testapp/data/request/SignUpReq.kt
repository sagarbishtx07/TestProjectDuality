package com.example.testapp.data.request

data class SignUpReq(
    val confirmPassword: String,
    val email: String,
    val password: String
)
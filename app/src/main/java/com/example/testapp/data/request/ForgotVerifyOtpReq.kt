package com.example.testapp.data.request

data class ForgotVerifyOtpReq(
    val email: String,
    val otp: String
)
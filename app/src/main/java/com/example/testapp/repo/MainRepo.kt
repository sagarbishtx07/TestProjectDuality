package com.example.testapp.repo

import com.example.testapp.data.request.ForgotPasswordReq
import com.example.testapp.data.request.ForgotVerifyOtpReq
import com.example.testapp.data.request.LoginApiReq
import com.example.testapp.data.request.RegistrationApiReq
import com.example.testapp.data.request.SignUpReq
import com.example.testapp.data.response.ForgotPasswordRes
import com.example.testapp.data.response.ForgotVerifyOtpRes
import com.example.testapp.data.response.LoginApiRes
import com.example.testapp.data.response.RegistrationApiRes
import com.example.testapp.data.response.SignUpApiRes
import com.example.testapp.network.Api
import okhttp3.Request
import retrofit2.Response

class MainRepo(var api: Api) {
    suspend fun login(loginRequest: LoginApiReq): Response<LoginApiRes> {
        return api.login(loginRequest)
    }
    suspend fun signUp(signUpReq: SignUpReq): Response<SignUpApiRes> {
        return api.signUp(signUpReq)
    }
    suspend fun registrationApi(registerApiReq: RegistrationApiReq): Response<RegistrationApiRes> {
        return api.registerProfile(registerApiReq)
    }
    suspend fun forgotPassword(request: ForgotPasswordReq): Response<ForgotPasswordRes> {
        return api.forgotPassword(request)
    }
    suspend fun verifyOTP(request: ForgotVerifyOtpReq): Response<ForgotVerifyOtpRes> {
        return api.verifyOtp(request)
    }
}
package com.example.testapp.network

import com.example.testapp.data.request.LoginApiReq
import com.example.testapp.data.request.RegistrationApiReq
import com.example.testapp.data.request.SignUpReq
import com.example.testapp.data.response.LoginApiRes
import com.example.testapp.data.response.RegistrationApiRes
import com.example.testapp.data.response.SignUpApiRes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface Api {

    @POST("user/loginWithPhone")
    suspend fun login(
        @Body loginRequest: LoginApiReq
    ): Response<LoginApiRes>

    @POST("user/signup")
    suspend fun signUp(
        @Body signUpRequest: SignUpReq
    ):Response<SignUpApiRes>

    @POST("user/registration")
    suspend fun registerProfile(
        @Body registerApiReq: RegistrationApiReq
    ):Response<RegistrationApiRes>

}
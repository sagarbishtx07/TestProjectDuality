package com.example.testapp.network

import androidx.navigation.NavHostController
import com.example.testapp.viewmodel.MainViewModel
import com.example.testapp.MainActivity
import com.example.testapp.data.request.LoginApiReq
import com.example.testapp.data.request.RegistrationApiReq
import com.example.testapp.data.request.SignUpReq
import com.example.testapp.data.response.LoginApiRes
import com.example.testapp.data.response.RegistrationApiRes
import com.example.testapp.data.response.SignUpApiRes
import timber.log.Timber

fun loginApi(
    viewModel: MainViewModel,
    email: String,
    password: String,
    navController: NavHostController,
    callback: (msg: LoginApiRes?) -> Unit
) {
    viewModel.loginApi(
        LoginApiReq(
            email = email,
            password = password,
        )
    ).observe(navController.context as MainActivity) {
        callback(it)
        Timber.tag("LoginApi").d("${it}")
    }
}
fun signUpApi(
    viewModel: MainViewModel,
    email: String,
    password: String,
    navController: NavHostController,
    callback: (msg: SignUpApiRes?) -> Unit
) {
    viewModel.signUpApi(
        SignUpReq(
            email = email,
            password = password,
            confirmPassword = password
        )
    ).observe(navController.context as MainActivity) {
        callback(it)
        Timber.tag("SignUpApi").d("${it}")
    }
}

fun registerApi(
    viewModel: MainViewModel,
    name: String,
    gender: String,
    age:String,
    bio:String,
    navController: NavHostController,
    callback: (msg: RegistrationApiRes?) -> Unit
) {
    viewModel.registerApi(
        RegistrationApiReq(
            fullName = name,
            gender = gender,
            bio = bio,
            age= age
        )
    ).observe(navController.context as MainActivity) {
        callback(it)
        Timber.tag("SignUpApi").d("${it}")
    }
}